package com.dutycam.app

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.PendingRecording
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

private enum class CaptureMode {
    Photo,
    Video
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    DutyCamScreen()
                }
            }
        }
    }
}

@Composable
private fun DutyCamScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        hasCameraPermission = results[Manifest.permission.CAMERA] == true
        hasAudioPermission = results[Manifest.permission.RECORD_AUDIO] == true ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
    }

    var captureMode by remember { mutableStateOf(CaptureMode.Photo) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var torchEnabled by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }

    val previewView = remember(context) {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(hasCameraPermission, lensFacing) {
        if (!hasCameraPermission) return@LaunchedEffect

        val provider = context.cameraProvider()
        cameraProvider = provider

        val selector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }
        val image = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
        val recorder = Recorder.Builder()
            .setQualitySelector(
                QualitySelector.from(
                    Quality.FHD,
                    FallbackStrategy.higherQualityOrLowerThan(Quality.SD)
                )
            )
            .build()
        val video = VideoCapture.withOutput(recorder)

        provider.unbindAll()
        camera = provider.bindToLifecycle(lifecycleOwner, selector, preview, image, video)
        imageCapture = image
        videoCapture = video
    }

    LaunchedEffect(camera, torchEnabled) {
        camera?.cameraControl?.enableTorch(torchEnabled)
    }

    DisposableEffect(Unit) {
        onDispose {
            activeRecording?.stop()
            cameraProvider?.unbindAll()
        }
    }

    if (!hasCameraPermission) {
        PermissionGate(
            onRequestPermissions = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                    )
                )
            }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
                .padding(horizontal = 18.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { torchEnabled = !torchEnabled },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.4f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                ) {
                    Icon(
                        imageVector = if (torchEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Torch",
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                IconButton(
                    onClick = {
                        if (isRecording) return@IconButton
                        lensFacing =
                            if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                CameraSelector.LENS_FACING_FRONT
                            } else {
                                CameraSelector.LENS_FACING_BACK
                            }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.4f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Flip Camera",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ModeSelector(
                    mode = captureMode,
                    recording = isRecording,
                    onModeChange = { next ->
                        if (isRecording && next != CaptureMode.Video) {
                            activeRecording?.stop()
                            activeRecording = null
                        }
                        captureMode = next
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                ShutterButton(
                    mode = captureMode,
                    recording = isRecording,
                    onClick = {
                        when (captureMode) {
                            CaptureMode.Photo -> {
                                StorageUtils.capturePhoto(
                                    context = context,
                                    imageCapture = imageCapture,
                                    executor = mainExecutor,
                                    onSaved = {
                                        Toast.makeText(
                                            context,
                                            "Photo saved to DCIM/WorkMedia",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = { message ->
                                        Toast.makeText(
                                            context,
                                            "Photo failed: $message",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            CaptureMode.Video -> {
                                if (isRecording) {
                                    activeRecording?.stop()
                                } else {
                                    val recording = StorageUtils.startVideoRecording(
                                        context = context,
                                        videoCapture = videoCapture,
                                        executor = mainExecutor,
                                        withAudio = hasAudioPermission,
                                        onStart = {
                                            isRecording = true
                                            Toast.makeText(
                                                context,
                                                "Recording started",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onSaved = {
                                            isRecording = false
                                            activeRecording = null
                                            Toast.makeText(
                                                context,
                                                "Video saved to DCIM/WorkMedia",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onError = { message ->
                                            isRecording = false
                                            activeRecording = null
                                            Toast.makeText(
                                                context,
                                                "Video failed: $message",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                    activeRecording = recording
                                }
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PermissionGate(onRequestPermissions: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Camera access is required",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Grant camera and microphone permissions to capture work photos and videos.",
                color = Color.White.copy(alpha = 0.82f),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onRequestPermissions) {
                Text("Grant Permissions")
            }
        }
    }
}

@Composable
private fun TopPillButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Black.copy(alpha = 0.45f))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(30.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun ModeSelector(
    mode: CaptureMode,
    recording: Boolean,
    onModeChange: (CaptureMode) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CaptureMode.entries.forEach { entry ->
            val selected = mode == entry
            val alpha by animateColorAsState(
                targetValue = if (selected) Color.White else Color.White.copy(alpha = 0.5f),
                animationSpec = tween(300),
                label = "ModeAlpha"
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(
                    enabled = !recording || entry == CaptureMode.Video,
                    onClick = { onModeChange(entry) }
                )
            ) {
                Text(
                    text = entry.name.uppercase(),
                    color = alpha,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(if (selected) 20.dp else 0.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(1.dp))
                )
            }
        }
    }
}

@Composable
private fun ShutterButton(
    mode: CaptureMode,
    recording: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val outerCircleSize by animateDpAsState(
        targetValue = if (isPressed) 82.dp else 88.dp,
        label = "OuterCircleSize"
    )
    
    val innerCircleSize by animateDpAsState(
        targetValue = when {
            mode == CaptureMode.Video && recording -> 28.dp
            isPressed -> 62.dp
            else -> 68.dp
        },
        label = "InnerCircleSize"
    )
    
    val innerCircleCorner by animateDpAsState(
        targetValue = if (mode == CaptureMode.Video && recording) 8.dp else 100.dp,
        label = "InnerCircleCorner"
    )
    
    val innerColor by animateColorAsState(
        targetValue = if (mode == CaptureMode.Video) {
            if (recording) Color(0xFFE53935) else Color(0xFFF44336)
        } else {
            Color.White
        },
        label = "InnerColor"
    )

    Box(
        modifier = Modifier
            .size(88.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring
        Box(
            modifier = Modifier
                .size(outerCircleSize)
                .border(4.dp, Color.White, CircleShape)
        )
        
        // Inner button
        Box(
            modifier = Modifier
                .size(innerCircleSize)
                .clip(RoundedCornerShape(innerCircleCorner))
                .background(innerColor)
        )
    }
}

private suspend fun android.content.Context.cameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                try {
                    continuation.resume(cameraProviderFuture.get())
                } catch (exception: Exception) {
                    continuation.resumeWithException(exception)
                }
            },
            ContextCompat.getMainExecutor(this)
        )
    }


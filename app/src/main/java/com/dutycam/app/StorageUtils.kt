package com.dutycam.app

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.PendingRecording
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor

object StorageUtils {

    fun createPhotoContentValues(): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "DutyCam_${timestamp()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_DCIM}/WorkMedia"
            )
        }
    }

    fun createVideoContentValues(): ContentValues {
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "DutyCam_${timestamp()}.mp4")
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_DCIM}/WorkMedia"
            )
        }
    }

    fun capturePhoto(
        context: Context,
        imageCapture: ImageCapture?,
        executor: Executor,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val capture = imageCapture ?: run {
            onError("Camera not ready")
            return
        }

        val options = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            createPhotoContentValues()
        ).build()

        capture.takePicture(
            options,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    onError(exception.message ?: "Unknown error")
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onSaved()
                }
            }
        )
    }

    fun startVideoRecording(
        context: Context,
        videoCapture: VideoCapture<Recorder>?,
        executor: Executor,
        withAudio: Boolean,
        onStart: () -> Unit,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ): Recording? {
        val capture = videoCapture ?: run {
            onError("Camera not ready")
            return null
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
            .setContentValues(createVideoContentValues())
            .build()

        var pendingRecording: PendingRecording = capture.output.prepareRecording(context, outputOptions)
        if (withAudio) {
            pendingRecording = pendingRecording.withAudioEnabled()
        }

        return pendingRecording.start(executor) { event ->
            when (event) {
                is VideoRecordEvent.Start -> onStart()
                is VideoRecordEvent.Finalize -> {
                    if (event.hasError()) {
                        onError(event.error.toString())
                    } else {
                        onSaved()
                    }
                }
            }
        }
    }

    fun timestamp(): String =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
}

package com.dutycam.app

import android.provider.MediaStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StorageUtilsTest {

    @Test
    fun `timestamp returns correctly formatted string`() {
        val timestamp = StorageUtils.timestamp()
        // Format: yyyyMMdd_HHmmss (15 characters)
        assertEquals(15, timestamp.length)
        assertTrue(timestamp.matches(Regex("""\d{8}_\d{6}""")))
    }

    @Test
    fun `createPhotoContentValues has correct metadata`() {
        val values = StorageUtils.createPhotoContentValues()
        
        val displayName = values.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)
        val mimeType = values.getAsString(MediaStore.MediaColumns.MIME_TYPE)
        val relativePath = values.getAsString(MediaStore.MediaColumns.RELATIVE_PATH)

        assertTrue(displayName.startsWith("DutyCam_"))
        assertTrue(displayName.endsWith(".jpg"))
        assertEquals("image/jpeg", mimeType)
        assertEquals("DCIM/WorkMedia", relativePath)
    }

    @Test
    fun `createVideoContentValues has correct metadata`() {
        val values = StorageUtils.createVideoContentValues()
        
        val displayName = values.getAsString(MediaStore.MediaColumns.DISPLAY_NAME)
        val mimeType = values.getAsString(MediaStore.MediaColumns.MIME_TYPE)
        val relativePath = values.getAsString(MediaStore.MediaColumns.RELATIVE_PATH)

        assertTrue(displayName.startsWith("DutyCam_"))
        assertTrue(displayName.endsWith(".mp4"))
        assertEquals("video/mp4", mimeType)
        assertEquals("DCIM/WorkMedia", relativePath)
    }
}

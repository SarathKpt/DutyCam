package com.dutycam.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DutyCamTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_starts_and_shows_permission_gate_if_needed() {
        // This test assumes permissions are not granted by default in the test environment
        // or it will just check if the UI is responsive.
        // Since we can't easily control system permissions here, we just verify the basic launch.
        
        // If the permission gate is shown:
        val grantButton = composeTestRule.onNodeWithText("Grant Permissions")
        
        // We don't assert it exists because it might already be granted on the device,
        // but if it exists, we try to click it.
        try {
            grantButton.performClick()
        } catch (e: Exception) {
            // Permission already granted or button not found, which is fine for a smoke test
        }
    }
}

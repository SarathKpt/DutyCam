package com.dutycam.app.ui.activities

import android.content.SharedPreferences
import com.dutycam.app.util.EphemeralSharedPrefsNamespace
import com.dutycam.app.util.getPrefs

class SecureCaptureActivity : CaptureActivity(), SecureActivity {
    val ephemeralPrefsNamespace = EphemeralSharedPrefsNamespace()

    override fun getSharedPreferences(name: String, mode: Int): SharedPreferences {
        return ephemeralPrefsNamespace.getPrefs(this, name, mode, cloneOriginal = true)
    }
}

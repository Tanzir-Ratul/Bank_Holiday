package com.example.bankholiday.session

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    private val editor = sharedPreferences.edit()

    var accessToken: String?
        get() = sharedPreferences.getString("accessToken","")
        set(value) {
            if (value != null) {
                editor.putString("highScore", value).apply()
            }
        }

    companion object {
        const val PREF_FILE_NAME = "SHARED_PREF"

    }
}
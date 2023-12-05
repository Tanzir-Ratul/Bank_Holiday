package com.example.bankholiday.utils

import android.util.Patterns
import android.view.View

fun animateVisibility(view: View, isVisible: Boolean) {
    val animation = if (isVisible) {
        view.animate().alpha(1f).setDuration(500)
    } else {
        view.animate().alpha(0f).setDuration(500)
    }

    animation.withEndAction {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }.start()
}

 fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

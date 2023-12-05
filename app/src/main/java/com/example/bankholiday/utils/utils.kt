package com.example.bankholiday.utils

import android.view.View

fun animateVisibility(view: View, isVisible: Boolean) {
    val animation = if (isVisible) {
        view.animate().alpha(1f).setDuration(100)
    } else {
        view.animate().alpha(0f).setDuration(500)
    }

    animation.withEndAction {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }.start()
}
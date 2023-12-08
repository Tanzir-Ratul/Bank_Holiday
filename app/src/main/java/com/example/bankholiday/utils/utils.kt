package com.example.bankholiday.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Locale

fun handleUnsuccessfulAPI(ctx:Context, code: Int) {
    when (code) {
        400 -> {
            // Handle Bad Request
            Toast.makeText(ctx, "Bad Request", Toast.LENGTH_SHORT).show()
        }
        401 -> {
            // Handle Unauthorized access
            Toast.makeText(ctx, "Unauthorized access", Toast.LENGTH_SHORT).show()

        }
        403 -> {
            // Handle Forbidden
            Toast.makeText(ctx, "Forbidden", Toast.LENGTH_SHORT).show()
        }
        404 -> {
            // Handle Resource Not Found
            Toast.makeText(ctx, "Resource not found", Toast.LENGTH_SHORT).show()
        }
        500 -> {
            // Handle Internal Server Error
            Toast.makeText(ctx, "Internal Server Error", Toast.LENGTH_SHORT).show()

        }
        else -> {
            // Handle other error codes
            Toast.makeText(ctx, "Unexpected error: $code", Toast.LENGTH_SHORT).show()

        }
    }
}

fun formatDate(inputDateStr: String?,inputDatePattern:String,outputDatePattern:String="dd MMM yyyy"): String? {
    val inputDateFormat = SimpleDateFormat(inputDatePattern.trim(), Locale.US)

    val outputDateFormat = SimpleDateFormat(outputDatePattern.trim(), Locale.US)

    try {
        // Parse the input date string into a Date object
        val date = inputDateStr?.let { inputDateFormat.parse(it) }

        // Format the Date object into the desired format
        return date?.let { outputDateFormat.format(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "Invalid Date" // Return an empty string in case of an error
}
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
fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}
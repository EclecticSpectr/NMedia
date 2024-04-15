package ru.netology.nmedia.util

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
            thisRef.getString(property.name)
    }

    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }

    fun View.focusAndShowKeyboard() {
        /**
         * This is to be called when the window already has focus.
         */
        fun View.showTheKeyboardNow() {
            if (isFocused) {
                post {
                    // We still post the call, just in case we are being notified of the windows focus
                    // but InputMethodManager didn't get properly setup yet.
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        requestFocus()
        if (hasWindowFocus()) {
            // No need to wait for the window to get focus.
            showTheKeyboardNow()
        } else {
            // We need to wait until the window gets focus.
            viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        // This notification will arrive just before the InputMethodManager gets set up.
                        if (hasFocus) {
                            this@focusAndShowKeyboard.showTheKeyboardNow()
                            // It’s very important to remove this listener once we are done.
                            viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }
}
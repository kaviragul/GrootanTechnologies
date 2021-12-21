package com.grootan.technologies.Utils

import android.app.Activity
import android.content.Context
import com.grootan.technologies.R
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.view.Gravity
import android.view.View
import kotlin.jvm.Synchronized

class CustomToast private constructor(private val context: Context) {
    fun showSmallCustomToast(message: String?) {
        val inflater = (context as Activity).layoutInflater
        val layout = inflater.inflate(
            R.layout.layout_custom_toast, context.findViewById<View>(R.id.ll_toast) as? ViewGroup
        )
        val msgTv = layout.findViewById<View>(R.id.tv_msg) as TextView
        msgTv.text = message
        val toast = Toast(context)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 100)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    companion object {
        private var instance: CustomToast? = null

        @Synchronized
        fun getInstance(context: Context): CustomToast? {
            if (instance == null) {
                instance = CustomToast(context)
            }
            return instance
        }
    }
}
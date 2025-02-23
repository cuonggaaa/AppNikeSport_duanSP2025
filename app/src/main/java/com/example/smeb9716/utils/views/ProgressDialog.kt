package com.example.smeb9716.utils.views

import android.app.Dialog
import android.content.Context
import com.example.smeb9716.R

class ProgressDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.progress)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}

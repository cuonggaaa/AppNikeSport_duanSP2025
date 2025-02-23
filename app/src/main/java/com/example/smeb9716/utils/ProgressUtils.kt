package com.example.smeb9716.utils

import android.app.Activity
import android.view.View
import android.view.WindowManager
import com.example.smeb9716.databinding.ProgressBinding

class ProgressUtils {

    companion object {
        /**
        * プログレスバー表示
        *
        * @param progress プログレスバー
        * @param activity Activity
        */
        fun startProgress(progress: ProgressBinding, activity: Activity?) {
            progress.progressView.visibility = View.VISIBLE
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        /**
        * プログレスバー非表示
        *
        * @param progress プログレスバー
        * @param activity Activity
        */
        fun stopProgress(progress: ProgressBinding, activity: Activity?) {
            progress.progressView.visibility = View.GONE
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}
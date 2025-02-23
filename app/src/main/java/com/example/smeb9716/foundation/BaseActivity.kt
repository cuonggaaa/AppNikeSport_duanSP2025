package com.example.smeb9716.foundation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.utils.views.ProgressDialog
import es.dmoral.toasty.Toasty

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    // ViewBinding instance
    lateinit var binding: T

    protected var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set layout
        binding = getViewBinding()
        setContentView(binding.root)

//        // Hide Status Bar
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }

        setLayoutBelowSystemBar(binding.root)
        enableUIDrawOnSystemBar()
        setWhiteStatusBar()
        initViews()
        initEvents()
        initObservers()
    }

    protected fun setLayoutBelowSystemBar(layoutAppBar: View) {
        ViewCompat.setOnApplyWindowInsetsListener(layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    protected fun enableUIDrawOnSystemBar() {
        val decorView: View = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    protected fun setWhiteStatusBar() {
        val decorView: View = window.decorView
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(android.R.color.holo_orange_light)
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showLoading(isShow: Boolean) {
        if (isShow) {
            loadingDialog?.dismiss()
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

    fun showMessage(context: Context, message: String, bgType: BGType) {
        when (bgType) {
            BGType.BG_TYPE_NORMAL -> Toasty.normal(context, message, 10000).show()
            BGType.BG_TYPE_SUCCESS -> Toasty.success(context, message, 10000, true).show()
            BGType.BG_TYPE_WARNING -> Toasty.warning(context, message, 10000, true).show()
            BGType.BG_TYPE_ERROR -> Toasty.error(context, message, 10000, true).show()
        }
    }

    protected abstract fun getViewBinding(): T
    protected abstract fun initViews()
    protected abstract fun initEvents()
    protected abstract fun initObservers()
}
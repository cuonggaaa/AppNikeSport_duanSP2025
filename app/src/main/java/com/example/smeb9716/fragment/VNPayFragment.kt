package com.example.smeb9716.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.example.smeb9716.databinding.FragmentVNPayBinding
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.ext.goBackUtilHomeFragment
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import timber.log.Timber

@AndroidEntryPoint
class VNPayFragment(private val url: String) : BaseFragment<FragmentVNPayBinding>() {
    private var mPaymentRedirectedUrl = ""

    enum class PaymentStatus {
        SUCCESS, FAILED, PENDING
    }

    private var isOrderSuccess = MutableLiveData(PaymentStatus.PENDING)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentVNPayBinding {
        return FragmentVNPayBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
    }

    override fun initObservers() {
        isOrderSuccess.observe(viewLifecycleOwner) {
            if (it == PaymentStatus.SUCCESS) {
                Toasty.success(requireContext(), "Thanh toán thành công").show()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    goBackUtilHomeFragment()
//                }, 2000)
                goBackUtilHomeFragment()
            } else if (it == PaymentStatus.FAILED) {
                Toasty.error(requireContext(), "Thanh toán thất bại").show()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    goBackFragment()
//                }, 2000)
                goBackFragment()
            }
        }
    }

    private fun exitFragmentWithDelay(func: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            func()
        }, 2000) // 2 seconds delay
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews(binding: FragmentVNPayBinding) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.visibility = View.VISIBLE
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                val convertedUrl = convertLocalHost(url ?: "")
                Timber.d("On page started: $convertedUrl")
                super.onPageStarted(view, convertedUrl, favicon)
                mPaymentRedirectedUrl = convertedUrl
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                val convertedUrl = convertLocalHost(url ?: "")
                super.onPageFinished(view, convertedUrl)
                Timber.d("On page finished: $convertedUrl")
                mPaymentRedirectedUrl = convertedUrl
                handlePaymentResult()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val convertedUrl = convertLocalHost(url ?: "")
                mPaymentRedirectedUrl = convertedUrl
                handlePaymentResult()
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        binding.webView.loadUrl(url)
    }

    private fun handlePaymentResult() {
        Timber.d("mPaymentRedirectedUrl: $mPaymentRedirectedUrl")
        if (mPaymentRedirectedUrl.contains("vnp_TransactionStatus=00")) {
            isOrderSuccess.postValue(PaymentStatus.SUCCESS)
        } else if (mPaymentRedirectedUrl.contains("vnp_TransactionStatus=02")) {
            isOrderSuccess.postValue(PaymentStatus.FAILED)
        } else if (mPaymentRedirectedUrl.contains("Error")) {
            isOrderSuccess.postValue(PaymentStatus.FAILED)
        }
    }

    private fun convertLocalHost(url: String): String {
//        if (url.contains("http://localhost")) {
//            return url.replace("http://localhost", "http://10.0.2.2")
//        }
        return url
    }
}

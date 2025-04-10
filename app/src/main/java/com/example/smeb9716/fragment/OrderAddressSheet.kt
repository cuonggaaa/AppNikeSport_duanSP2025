package com.example.smeb9716.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.LayoutBottomSheetEditOrderAddressBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.utils.validator.validateNotEmpty
import com.example.smeb9716.viewmodel.OrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderAddressSheet(val viewModel: OrderViewModel) : BottomSheetDialogFragment() {

    private val binding: LayoutBottomSheetEditOrderAddressBinding by lazy {
        inflateBinding(layoutInflater, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setWhiteStatusBar()
        initEvents()
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): LayoutBottomSheetEditOrderAddressBinding {
        return LayoutBottomSheetEditOrderAddressBinding.inflate(inflater, container, false)
    }

    fun initEvents() {
        binding.btnSend.setOnClickListener {
            if (validateInput()) {
                viewModel.addressLiveData.postValue(binding.edtComment.text.toString())
                dismiss()
            }
        }

        binding.edtComment.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateNotEmpty()
            binding.tilComment.error = errMsg
        }
    }

    fun initObservers() {
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                (requireActivity() as BaseActivity<*>).showLoading(it)
            }
        }
        lifecycleScope.launch {
            viewModel.responseMessage.collect {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), it.message, it.bgType
                )
            }
        }
    }

    private fun validateInput(): Boolean {
        val comment = binding.edtComment.text.toString()

        val commentErr = comment.validateNotEmpty()

        return commentErr == null
    }

    fun initViews() {
        binding.edtComment.setText(viewModel.addressLiveData.value)
    }

    protected fun setWhiteStatusBar() {
        val decorView: View = requireActivity().window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}
package com.example.smeb9716.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.LayoutBottomSheetReviewBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.model.CartOrder
import com.example.smeb9716.model.Order
import com.example.smeb9716.utils.validator.validateNotEmpty
import com.example.smeb9716.viewmodel.OrderHistoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewSheet(private val order: Order) : BottomSheetDialogFragment() {
    private val viewModel: OrderHistoryViewModel by viewModels()

    private val binding: LayoutBottomSheetReviewBinding by lazy {
        inflateBinding(layoutInflater, null)
    }

    private var cart: CartOrder? = null

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
    ): LayoutBottomSheetReviewBinding {
        return LayoutBottomSheetReviewBinding.inflate(inflater, container, false)
    }

    fun initEvents() {
        binding.btnSend.setOnClickListener {
            if (validateInput() && cart != null) {
                viewModel.sendProductReview(
                    productId = cart!!.productId.id,
                    rating = binding.sliderRating.value.toDouble(),
                    comment = binding.edtComment.text.toString()
                )
            }
        }

        binding.sliderRating.addOnChangeListener { _, value, _ ->
            binding.tvRating.text = value.toString()
        }

        binding.edtComment.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateNotEmpty()
            binding.tilComment.error = errMsg
        }

        binding.spProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                cart = order.cartData?.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                cart = null
            }
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
        lifecycleScope.launch {
            viewModel.reviewSuccess.observe(viewLifecycleOwner) {
                if (it) {
                    dismiss()
                    viewModel.reviewSuccess.postValue(false)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val comment = binding.edtComment.text.toString()

        val commentErr = comment.validateNotEmpty()

        return commentErr == null
    }

    fun initViews() {
        binding.tvRating.text = "1.0"
        cart = order.cartData?.firstOrNull()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            order.cartData?.map { it.productId.name.toString() }?.toList() ?: listOf()
        )
        binding.spProduct.adapter = adapter
    }

    protected fun setWhiteStatusBar() {
        val decorView: View = requireActivity().window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}
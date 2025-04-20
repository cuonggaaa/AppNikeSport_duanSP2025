package com.example.smeb9716.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.LayoutBottomSheetAddCartBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Order
import com.example.smeb9716.model.Product
import com.example.smeb9716.utils.PreferManager
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.viewmodel.ProductDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddCartSheet(private val product: Product) : BottomSheetDialogFragment() {
    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var preferManager: PreferManager
    private var stock: Int = 0
    private val binding: LayoutBottomSheetAddCartBinding by lazy {
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
    ): LayoutBottomSheetAddCartBinding {
        return LayoutBottomSheetAddCartBinding.inflate(inflater, container, false)
    }

    fun initEvents() {
        val productSize = product.sizes
        val sizeOptions = productSize?.map { it.size } ?: emptyList()
        val spinner = binding.spinnerSize

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sizeOptions
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.adapter = adapter

        viewModel.quantity = 1

        binding.ivMinus.setOnClickListener {
            viewModel.decreaseQuantity()
        }

        binding.ivPlus.setOnClickListener {
            viewModel.increaseQuantity(stock)
        }

        binding.tvProductTitle.text = product.name

        binding.btnAddToCart.setOnClickListener {
            if (binding.spinnerSize.selectedItem == null) {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), "Vui lòng chọn size", BGType.BG_TYPE_ERROR
                )
                return@setOnClickListener
            }
            val selectedSize = binding.spinnerSize.selectedItem.toString()
            viewModel.addCart(product, viewModel.quantity, selectedSize)
//            dismiss()
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

        viewModel.quantityLiveData.observe(viewLifecycleOwner) {
            binding.tvQuantity.text = it.toString()
        }

        binding.spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedSize = product.sizes?.get(position)
                binding.tvStock.text = "Tồn kho: " + selectedSize?.quantity.toString()
                stock = selectedSize?.quantity ?: 0
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewModel.addCartSuccess.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), "Thêm vào giỏ hàng thành công", BGType.BG_TYPE_SUCCESS
                )
                dismiss()
                goBackFragment()
                viewModel.addCartSuccess.postValue(false)
            }
        }
    }


    fun initViews() {
        preferManager = PreferManager.getInstance(requireContext())
    }

    protected fun setWhiteStatusBar() {
        val decorView: View = requireActivity().window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}
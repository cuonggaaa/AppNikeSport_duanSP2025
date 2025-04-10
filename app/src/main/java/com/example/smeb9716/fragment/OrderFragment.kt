package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.R
import com.example.smeb9716.adapter.OrderProductAdapter
import com.example.smeb9716.adapter.OrderVoucherAdapter
import com.example.smeb9716.databinding.FragmentOrderBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.PaymentMethodCode
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.ext.goBackUtilHomeFragment
import com.example.smeb9716.utils.ext.toMoneyFormat
import com.example.smeb9716.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OrderFragment(private val carts: List<Cart>) : BaseFragment<FragmentOrderBinding>() {
    private val viewModel: OrderViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(inflater, container, false)
    }

    private val productAdapter = OrderProductAdapter(carts.map { it.toCartOrder() })
    private val voucherAdapter by lazy { OrderVoucherAdapter() }

    override fun initEvents() {
        voucherAdapter.setOnVoucherSelectedListener { voucher, _ ->
            viewModel.applyVoucher(voucher)
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.createOrder()
        }

        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })
        binding.btnEditAddress.setOnClickListener {
            val fragment = OrderAddressSheet(viewModel)
            fragment.show(childFragmentManager, fragment.tag)
        }
    }

    override fun initObservers() {
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
        viewModel.vouchersLiveData.observe(viewLifecycleOwner) {
            Timber.d("Vouchers: $it")
            voucherAdapter.setVouchers(it)
            binding.tvDiscounted.text =
                "${viewModel.discountedPrice.toString().toMoneyFormat()} VND"
        }
        viewModel.addressLiveData.observe(viewLifecycleOwner) {
            binding.tvAddress.text = it
        }
        viewModel.paymentMethod.observe(viewLifecycleOwner) {
            val spinnerAdapter = ArrayAdapter(requireContext(),
                R.layout.spinner_text,
                it.map { it1 -> it1.paymentMethod?.name ?: "" })

            spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)

            binding.spPaymentMethod.adapter = spinnerAdapter
            binding.spPaymentMethod.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?, position: Int, id: Long,
                    ) {
                        viewModel.selectPaymentMethod(it[position])
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        viewModel.selectPaymentMethod()
                    }
                }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = "${it.toString().toMoneyFormat()} VND"
        }
        viewModel.paymentValue.observe(viewLifecycleOwner) {
            binding.tvTotalPayment.text = "${it.toString().toMoneyFormat()} VND"
        }
        viewModel.createdOrderLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                Timber.d("Order: $it")
                Timber.d("Payment method: ${viewModel.selectedPaymentMethod.value}")
                if (viewModel.selectedPaymentMethod.value?.paymentMethod == PaymentMethodCode.VNPAY) {

                } else {
                    Toasty.success(requireContext(), "Đặt hàng thành công").show()
                    goBackUtilHomeFragment()
                }
            }
        }
    }

    override fun initViews(binding: FragmentOrderBinding) {
        binding.revProducts.adapter = productAdapter
        binding.revVoucher.adapter = voucherAdapter

        viewModel.carts = carts.toMutableList()

        viewModel.getVoucher()
        viewModel.getProfile()
        viewModel.getPaymentMethod()
    }
}

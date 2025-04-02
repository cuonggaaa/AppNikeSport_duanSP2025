package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smeb9716.R
import com.example.smeb9716.adapter.GiftVoucherAdapter
import com.example.smeb9716.databinding.FragmentGiftBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.viewmodel.GiftViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GiftFragment : BaseFragment<FragmentGiftBinding>() {
    private val viewModel: GiftViewModel by viewModels()
    private val adapter by lazy {
        GiftVoucherAdapter(onVoucherClick = ::onVoucherClick)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater, container, false)
    }

    private fun onVoucherClick(voucher: Voucher) {
        val fragment = CartsFragment()
        addFragment(
            R.id.frameLayoutContainer, fragment, true
        )
    }

    override fun initEvents() {
        binding.imvCart.setOnClickListener {
            openCart()
        }
        binding.tvCartCount.setOnClickListener {
            openCart()
        }
    }

    private fun openCart() {
        val fragment = CartsFragment()
        addFragment(
            R.id.frameLayoutContainer, fragment, true
        )
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
        viewModel.vouchers.observe(viewLifecycleOwner) {
            val data = (it ?: emptyList()).filter { it.status == "Active" }
            adapter.setData(data)
        }
        WholeApp.cartCount.observe(viewLifecycleOwner) {
            binding.tvCartCount.text = it.toString()
        }
    }

    override fun initViews(binding: FragmentGiftBinding) {
        val layoutManager = LinearLayoutManager(context)
        binding.revGift.layoutManager = layoutManager
        binding.revGift.adapter = adapter

        viewModel.getVoucher()
        viewModel.getCartCount()
    }
}

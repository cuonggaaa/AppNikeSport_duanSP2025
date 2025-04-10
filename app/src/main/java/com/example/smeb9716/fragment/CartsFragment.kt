package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smeb9716.R
import com.example.smeb9716.adapter.CartListAdapter
import com.example.smeb9716.databinding.FragmentCartsBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.ext.toMoneyFormat
import com.example.smeb9716.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartsFragment : BaseFragment<FragmentCartsBinding>() {
    private val viewModel: CartViewModel by viewModels()

    private val cartListAdapter by lazy {
        CartListAdapter()
    }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentCartsBinding {
        return FragmentCartsBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.cbSelectAll.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selectAll(isChecked)
        }
        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })
        binding.btnOrder.setOnClickListener {
            val selectedCarts = viewModel.getSelectedCarts()
            val fragment = OrderFragment(selectedCarts)
            addFragment(
                containerId = R.id.frameLayoutContainer,
                fragment = fragment,
                addToBackStack = true
            )
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
        viewModel.cartLiveData.observe(viewLifecycleOwner) { carts ->
            carts?.let {
                cartListAdapter.setData(it)
            } ?: run {
                cartListAdapter.setData(emptyList())
            }
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = "${it.toString().toMoneyFormat()} VND"
            binding.cbSelectAll.isChecked = viewModel.isTotalSelected
        }
        viewModel.totalQuantity.observe(viewLifecycleOwner) {
            binding.tvSelectedCount.text = "($it)"
            binding.btnOrder.isEnabled = it > 0
        }
    }

    override fun initViews(binding: FragmentCartsBinding) {

        cartListAdapter.setOnSelectListener { cart, selected ->
            viewModel.selectItem(cart, selected)
        }

        cartListAdapter.setOnIncreaseListener { cart ->
            viewModel.increaseQuantity(cart)
        }

        cartListAdapter.setOnDecreaseListener { cart ->
            viewModel.decreaseQuantity(cart)
        }

        cartListAdapter.setOnDeleteListener { cart ->
            viewModel.deleteCart(cart)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.revCarts.layoutManager = layoutManager
        binding.revCarts.adapter = cartListAdapter

        viewModel.getCart()
    }
}

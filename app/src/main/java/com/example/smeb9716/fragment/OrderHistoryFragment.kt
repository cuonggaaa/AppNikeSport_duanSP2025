package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smeb9716.adapter.OrderHistoryAdapter
import com.example.smeb9716.databinding.FragmentOrderHistoryBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.viewmodel.OrderHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding>() {
    private val viewModel: OrderHistoryViewModel by viewModels()
    private val adapter: OrderHistoryAdapter by lazy { OrderHistoryAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOrderHistoryBinding {
        return FragmentOrderHistoryBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })
        adapter.setOnReviewClick {
            val reviewSheet = ReviewSheet(it)
            reviewSheet.show(childFragmentManager, "ReviewSheet")
        }

        adapter.setOnCancelOrder {
            viewModel.cancelOrder(it.id)
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
        viewModel.orders.observe(viewLifecycleOwner) {
            adapter.setOrders(it.reversed())
        }
    }

    override fun initViews(binding: FragmentOrderHistoryBinding) {
        binding.rvOrderHistory.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvOrderHistory.layoutManager = layoutManager
        binding.rvOrderHistory.itemAnimator = null

        viewModel.getOrders()
    }


}

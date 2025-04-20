package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smeb9716.adapter.NotificationAdapter
import com.example.smeb9716.databinding.FragmentNotificationBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment: BaseFragment<FragmentNotificationBinding>() {
    private val viewModel: NotificationViewModel by viewModels()
    private val adapter: NotificationAdapter by lazy { NotificationAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        adapter.setOnNotificationClick {

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
        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    override fun initViews(binding: FragmentNotificationBinding) {
        binding.rvNotifications.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotifications.layoutManager = layoutManager
        binding.rvNotifications.itemAnimator = null

        viewModel.getNotifications()
    }
}
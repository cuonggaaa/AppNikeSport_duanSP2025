package com.example.smeb9716.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.R
import com.example.smeb9716.activities.LoginAct
import com.example.smeb9716.databinding.FragmentProfileBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.PREF_EMAIL
import com.example.smeb9716.model.PREF_PASSWORD
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.PreferManager
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.containerLogout.setOnClickListener {
            showConfirmAlert("Đăng xuất", "Bạn có chắc chắn muốn đăng xuất?", ::logout)
        }
        binding.containerInfo.setOnClickListener {
            val fragment = ProfileDetailFragment()
            addFragment(R.id.frameLayoutContainer, fragment, true)
        }
        binding.imvCart.setOnClickListener {
            val fragment = CartsFragment()
            addFragment(R.id.frameLayoutContainer, fragment, true)
        }
        binding.containerChangePassword.setOnClickListener {
            val fragment = ChangePasswordSheet()
            fragment.show(childFragmentManager, fragment.tag)
        }
        binding.containerOrders.setOnClickListener {

        }
    }

    private fun logout() {
        val preferManager = PreferManager.getInstance(requireContext())
        preferManager.write(PREF_EMAIL, null)
        preferManager.write(PREF_PASSWORD, null)

        val intent = Intent(requireContext(), LoginAct::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
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
        viewModel.profile.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.profileName.text = it.name ?: ""
            binding.profilePhoneNumber.text = it.phone ?: ""
        }
        WholeApp.cartCount.observe(viewLifecycleOwner) {
            binding.tvCartCount.text = it.toString()
        }
    }

    override fun initViews(binding: FragmentProfileBinding) {
        viewModel.getProfile()
    }
}

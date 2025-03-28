package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.FragmentProfileDetailBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.request.UpdateProfileRequest
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.validator.validateAddress
import com.example.smeb9716.utils.validator.validateName
import com.example.smeb9716.utils.validator.validatePhone
import com.example.smeb9716.utils.validator.validateUsername
import com.example.smeb9716.viewmodel.ProfileDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>() {
    private val viewModel: ProfileDetailViewModel by viewModels()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentProfileDetailBinding {
        return FragmentProfileDetailBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.btnUpdate.setOnClickListener {
            if (validateInput()) {
                showConfirmAlert(
                    "Cập nhật thông tin",
                    "Bạn có chắc chắn muốn cập nhật thông tin?"
                ) {
                    val request = UpdateProfileRequest(
                        name = binding.edtName.text.toString(),
                        phone = binding.edtPhone.text.toString(),
                        email = binding.edtEmail.text.toString(),
                        address = binding.edtAddress.text.toString()
                    )
                    viewModel.updateProfile(request)
                }
            }
        }

        binding.edtName.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateName()
            binding.tilName.error = errMsg
        }

        binding.edtPhone.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validatePhone()
            binding.tilPhone.error = errMsg
        }

        binding.edtEmail.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateUsername()
            binding.tilEmail.error = errMsg
        }

        binding.edtAddress.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateAddress()
            binding.tilAddress.error = errMsg
        }

        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })
    }

    private fun validateInput(): Boolean {
        val email = binding.edtEmail.text.toString()
        val name = binding.edtName.text.toString()
        val phone = binding.edtPhone.text.toString()
        val address = binding.edtAddress.text.toString()

        val emailErr = email.validateUsername()
        val nameErr = name.validateName()
        val phoneErr = phone.validatePhone()
        val addressErr = address.validateAddress()

        binding.tilEmail.error = emailErr
        binding.tilName.error = nameErr
        binding.tilPhone.error = phoneErr
        binding.tilAddress.error = addressErr

        return emailErr == null && nameErr == null && phoneErr == null && addressErr == null
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
        viewModel.user.observe(viewLifecycleOwner) {
            binding.edtName.setText(it.name)
            binding.edtPhone.setText(it.phone)
            binding.edtEmail.setText(it.email)
            binding.edtAddress.setText(it.address)
        }
        viewModel.updateSuccess.observe(viewLifecycleOwner) {
            if (it) {
                Toasty.success(
                    requireContext(), "Cập nhật thông tin thành công", 5000
                ).show()
                viewModel.getProfileDetail()
            }
        }
    }

    override fun initViews(binding: FragmentProfileDetailBinding) {
        viewModel.getProfileDetail()
    }
}

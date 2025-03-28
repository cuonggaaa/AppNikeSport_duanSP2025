package com.example.smeb9716.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.smeb9716.databinding.LayoutBottomSheetChangePasswordBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.model.PREF_PASSWORD
import com.example.smeb9716.utils.PreferManager
import com.example.smeb9716.utils.validator.validateConfirmPassword
import com.example.smeb9716.utils.validator.validateName
import com.example.smeb9716.utils.validator.validatePassword
import com.example.smeb9716.viewmodel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordSheet : BottomSheetDialogFragment() {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var preferManager: PreferManager
    private val binding: LayoutBottomSheetChangePasswordBinding by lazy {
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

    fun showConfirmAlert(title: String, message: String, action: () -> Unit) {
        AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                action()
            }.setNegativeButton("Huỷ hành động") { _, _ -> }.show()
    }

    fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): LayoutBottomSheetChangePasswordBinding {
        return LayoutBottomSheetChangePasswordBinding.inflate(inflater, container, false)
    }

    fun initEvents() {
        binding.btnUpdate.setOnClickListener {
            if (validateInput()) {
                showConfirmAlert("Cập nhật mật khẩu", "Bạn có chắc chắn muốn cập nhật mật khẩu?") {
                    viewModel.changePassword(
                        oldPassword = binding.edtOldPassword.text.toString(),
                        newPassword = binding.edtPassword.text.toString()
                    )
                }
            }
        }

        binding.edtOldPassword.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validateName()
            binding.tilOldPassword.error = errMsg
        }
        binding.edtPassword.doOnTextChanged { text, _, _, _ ->
            val errMsg = text.toString().validatePassword()
            binding.tilPassword.error = errMsg
        }
        binding.edtConfirmPassword.doOnTextChanged { text, _, _, _ ->
            val errMsg =
                text.toString().validateConfirmPassword(binding.edtPassword.text.toString())
            binding.tilConfirmPassword.error = errMsg
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
            viewModel.changePasswordSuccess.observe(viewLifecycleOwner) {
                if (it) {
                    preferManager.write(PREF_PASSWORD, binding.edtPassword.text.toString())
                    dismiss()
                }
            }
        }
    }

    fun validateInput(): Boolean {
        val oldPassword = binding.edtOldPassword.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()

        val oldPasswordErr = oldPassword.validatePassword()
        val passwordErr = password.validatePassword()
        val confirmPasswordErr = confirmPassword.validateConfirmPassword(password)

        binding.tilPassword.error = passwordErr
        binding.tilConfirmPassword.error = confirmPasswordErr
        binding.tilOldPassword.error = oldPasswordErr

        return passwordErr == null && confirmPasswordErr == null && oldPasswordErr == null
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
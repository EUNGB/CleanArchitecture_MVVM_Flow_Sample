package com.eungb.cleanarchapp.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eungb.cleanarchapp.R
import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.databinding.ActivitySignUpBinding
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.presentation.common.extension.isEmail
import com.eungb.cleanarchapp.presentation.common.extension.showGenericAlertDialog
import com.eungb.cleanarchapp.presentation.common.extension.showToast
import com.eungb.cleanarchapp.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        initControl()
    }

    private fun initControl() = with(binding) {
        backButton.setOnClickListener { moveBack() }
        registerButton.setOnClickListener { signUp() }
    }

    private fun initObserver() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun signUp() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (validateSignUp(name, email, password)) {
            viewModel.signUp(SignUpRequest(name, email, password))
        }
    }

    private fun validateSignUp(name: String, email: String, password: String): Boolean {
        clearErrorMessage()
        return when {
            name.isEmpty() -> {
                setNameError(getString(R.string.error_name_not_valid))
                false
            }
            !email.isEmail() -> {
                setEmailError(getString(R.string.error_email_not_valid))
                false
            }
            password.length < 8 -> {
                setPasswordError(getString(R.string.error_password_not_valid))
                false
            }
            else -> true
        }
    }

    private fun setNameError(e: String?) {
        binding.nameInput.error = e
    }

    private fun setEmailError(e: String?) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.passwordInput.error = e
    }

    private fun clearErrorMessage() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }

    private fun handleState(state: SignUpState) {
        when (state) {
            is SignUpState.IsLoading -> handleLoading((state.isLoading))
            is SignUpState.ShowToast -> handleShowToast(state.message)
            is SignUpState.SuccessSignUp -> handleSuccessSignUp(state.loginEntity)
            is SignUpState.ErrorSignUp -> handleErrorSignUp(state.error)
            else -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean) = with(binding) {
        loadingProgressBar.isEnabled = isLoading
        loadingProgressBar.isIndeterminate = isLoading
        if (!isLoading) {
            loadingProgressBar.progress = 0
        }
    }

    private fun handleShowToast(message: String) = this.showToast(message)

    private fun handleSuccessSignUp(loginEntity: LoginEntity) {
        Log.d("Success SignUp", "userToken: ${loginEntity.token} ")
        moveLogin()
    }

    private fun handleErrorSignUp(error: WrappedResponse<SignUpResponse>) = this.showGenericAlertDialog(error.message)

    private fun moveLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun moveBack() {
        this.finish()
    }

}
package com.eungb.cleanarchapp.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.eungb.cleanarchapp.R
import com.eungb.cleanarchapp.data.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.databinding.ActivityLoginBinding
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.infra.utils.SharedPrefs
import com.eungb.cleanarchapp.presentation.common.extension.isEmail
import com.eungb.cleanarchapp.presentation.common.extension.showGenericAlertDialog
import com.eungb.cleanarchapp.presentation.common.extension.showToast
import com.eungb.cleanarchapp.presentation.main.MainActivity
import com.eungb.cleanarchapp.presentation.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (prefs.getToken().isNotEmpty()) {
            goToMain()
        }

        initControl()
        login()
        observe()
    }

    private fun initControl() {
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: LoginState) {
        when (state) {
            is LoginState.ShowToast -> showToast(state.message)
            is LoginState.IsLoading -> handleLoading(state.isLoading)
            is LoginState.Init -> Unit
            is LoginState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if (!isLoading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity) {
        prefs.saveToken(loginEntity.token)
        goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<LoginResponse>) {
        showGenericAlertDialog(rawResponse.message)
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validate(email, password)) {
                viewModel.login(LoginRequest(email, password))
            }
        }
    }

    private fun setEmailError(e: String?) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.passwordInput.error = e
    }

    private fun resultAllError() {
        setEmailError(null)
        setPasswordError(null)
    }

    private fun validate(email: String, password: String): Boolean {
        resultAllError()

        if (!email.isEmail()) {
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if (password.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }
}
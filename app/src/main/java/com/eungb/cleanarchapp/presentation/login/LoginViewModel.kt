package com.eungb.cleanarchapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginRequest
import com.eungb.cleanarchapp.data.common.login.remote.dto.LoginResponse
import com.eungb.cleanarchapp.data.common.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.login.entity.LoginEntity
import com.eungb.cleanarchapp.domain.login.usecase.LoginUseCase
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val state = MutableStateFlow<LoginState>(LoginState.Init)
    val mState: StateFlow<LoginState> get() = state

    private fun setLoading() {
        state.value = LoginState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = LoginState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = LoginState.ShowToast(message)
    }

    private fun successLogin(loginEntity: LoginEntity) {
        state.value = LoginState.SuccessLogin(loginEntity)
    }

    private fun errorLogin(rawResponse: WrappedResponse<LoginResponse>) {
        state.value = LoginState.ErrorLogin(rawResponse)
    }


    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest)
                .onStart {
                    setLoading()
                }
                .catch { e ->
                    hideLoading()
                    e.printStackTrace()
                    showToast(e.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> successLogin(result.data)
                        is BaseResult.Error -> errorLogin(result.rawResponse)
                    }

                }
        }
    }
}

sealed class LoginState {
    object Init : LoginState()
    data class IsLoading(val isLoading: Boolean) : LoginState()
    data class ShowToast(val message: String) : LoginState()
    data class SuccessLogin(val loginEntity: LoginEntity) : LoginState()
    data class ErrorLogin(val rawResponse: WrappedResponse<LoginResponse>) : LoginState()
}
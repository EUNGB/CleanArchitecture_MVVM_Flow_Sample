package com.eungb.cleanarchapp.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eungb.cleanarchapp.data.remote.dto.SignUpRequest
import com.eungb.cleanarchapp.data.remote.dto.SignUpResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.LoginEntity
import com.eungb.cleanarchapp.domain.usecase.SignUpUseCase
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Init)
    val state: StateFlow<SignUpState> get() = _state

    /**
     * showLoading
     * hideLoading
     * showToast
     * SuccessSignUp
     * ErrorSignUp
     */

    private fun showLoading() {
        _state.value = SignUpState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = SignUpState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = SignUpState.ShowToast(message)
    }

    private fun successSignUp(loginEntity: LoginEntity) {
        _state.value = SignUpState.SuccessSignUp(loginEntity)
    }

    private fun errorSignUp(error: WrappedResponse<SignUpResponse>) {
        _state.value = SignUpState.ErrorSignUp(error)
    }

    fun signUp(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            signUpUseCase.invoke(signUpRequest)
                .onStart { showLoading() }
                .catch { e ->
                    hideLoading()
                    showToast(e.printStackTrace().toString())
                }
                .collect { res ->
                    hideLoading()
                    when (res) {
                        is BaseResult.Success -> successSignUp(res.data)
                        is BaseResult.Error -> errorSignUp(res.rawResponse)
                    }
                }
        }
    }

}

sealed class SignUpState {
    object Init : SignUpState()
    data class IsLoading(val isLoading: Boolean) : SignUpState()
    data class ShowToast(val message: String) : SignUpState()
    data class SuccessSignUp(val loginEntity: LoginEntity) : SignUpState()
    data class ErrorSignUp(val error: WrappedResponse<SignUpResponse>) : SignUpState()
}
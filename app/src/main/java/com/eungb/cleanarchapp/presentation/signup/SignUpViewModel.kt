package com.eungb.cleanarchapp.presentation.signup

import androidx.lifecycle.MutableLiveData
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

    private val _event = MutableSharedFlow<SignUpEvent>()
    val event: SharedFlow<SignUpEvent> get() = _event

    // 데이터 바인딩 적용
    val nameLiveData = MutableLiveData<String>()
    val name: String get() = nameLiveData.value ?: ""
    val emailLiveData = MutableLiveData<String>()
    val email: String get() = emailLiveData.value ?: ""
    val passwordLiveData = MutableLiveData<String>()
    val password: String get() = passwordLiveData.value ?: ""

    val input = object : SignUpInput {
        override fun clickSignUp(signUpRequest: SignUpRequest) {
            signUp(signUpRequest)
        }
    }

    private val output = object : SignUpOutput {
        override fun showLoading() {
            _state.value = SignUpState.IsLoading(true)
        }

        override fun hideLoading() {
            _state.value = SignUpState.IsLoading(false)
        }

        override fun showToast(message: String) {
            _state.value = SignUpState.ShowToast(message)
        }

        override fun successSignUp(loginEntity: LoginEntity) {
            _state.value = SignUpState.SuccessSignUp(loginEntity)
        }

        override fun errorSignUp(error: WrappedResponse<SignUpResponse>) {
            _state.value = SignUpState.ErrorSignUp(error)
        }
    }

    val route = object : SignUpRoute {
        override fun toBack() {
            viewModelScope.launch {
                _event.emit(SignUpEvent.MoveBack)
            }
        }

        override fun toSignIn() {
            viewModelScope.launch {
                _event.emit(SignUpEvent.MoveSignIn)
            }
        }
    }

    fun signUp(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            with(output) {
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

    interface SignUpInput {
        fun clickSignUp(signUpRequest: SignUpRequest)
//        fun clickBack()
    }

    interface SignUpOutput {
        fun showLoading()
        fun hideLoading()
        fun showToast(message: String)
        fun successSignUp(loginEntity: LoginEntity)
        fun errorSignUp(error: WrappedResponse<SignUpResponse>)
    }

    interface SignUpRoute {
        fun toBack()
        fun toSignIn()
    }

}

sealed class SignUpState {
    object Init : SignUpState()
    data class IsLoading(val isLoading: Boolean) : SignUpState()
    data class ShowToast(val message: String) : SignUpState()
    data class SuccessSignUp(val loginEntity: LoginEntity) : SignUpState()
    data class ErrorSignUp(val error: WrappedResponse<SignUpResponse>) : SignUpState()
}

sealed class SignUpEvent {
    object MoveBack : SignUpEvent()
    object MoveSignIn : SignUpEvent()
}
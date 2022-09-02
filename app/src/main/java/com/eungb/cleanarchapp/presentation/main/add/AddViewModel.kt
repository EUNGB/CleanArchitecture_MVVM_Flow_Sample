package com.eungb.cleanarchapp.presentation.main.add

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.usecase.product.CreateProductUseCase
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<AddState>()
    val state: SharedFlow<AddState> get() = _state

    private val _event = MutableSharedFlow<AddEvent>()
    val event: SharedFlow<AddEvent> get() = _event

    val productNameLiveData = MutableLiveData<String>()
    val productName: String get() = productNameLiveData.value ?: ""
    val productPriceLiveData = MutableLiveData<String>()
    val productPrice: String get() = productPriceLiveData.value ?: ""

    val loadingVisibleLiveData = MutableLiveData(View.INVISIBLE)

    val input = object : AddInput {
        override fun clickSave() {
            saveProduct(ProductRequest(productName, productPrice.toInt()))
        }
    }

    private val output = object : AddOutput {
        override fun showLoading() {
            loadingVisibleLiveData.postValue(View.VISIBLE)
        }

        override fun hideLoading() {
            loadingVisibleLiveData.postValue(View.GONE)
        }

        override fun showExceptionDialog(message: String) {
            viewModelScope.launch {
                _state.emit(AddState.Exception(message))
            }
        }

        override fun showErrorDialog(error: WrappedResponse<ProductResponse>) {
            viewModelScope.launch {
                _state.emit(AddState.Error(error))
            }
        }

        override fun successSave() {
            viewModelScope.launch {
                _state.emit(AddState.Success)
            }
        }
    }

    val route = object : AddRoute {
        override fun toBack() {
            viewModelScope.launch {
                _event.emit(AddEvent.MoveBack)
            }
        }
    }

    private fun saveProduct(productRequest: ProductRequest) {
        viewModelScope.launch {
            createProductUseCase.invoke(productRequest)
                .onStart {
                    output.showLoading()
                }
                .catch { e ->
                    output.hideLoading()
                    output.showExceptionDialog(e.message ?: "오류가 발생했습니다.")
                }
                .collect {
                    output.hideLoading()
                    when (it) {
                        is BaseResult.Success -> output.successSave()
                        is BaseResult.Error -> output.showErrorDialog(it.rawResponse)
                    }
                }
        }
    }


    interface AddInput {
        fun clickSave()
    }

    interface AddOutput {
        fun showLoading()
        fun hideLoading()
        fun showExceptionDialog(message: String)
        fun showErrorDialog(error: WrappedResponse<ProductResponse>)
        fun successSave()
    }

    interface AddRoute {
        fun toBack()
    }
}

sealed class AddState {
    data class Exception(val message: String) : AddState()
    object Success : AddState()
    data class Error(val error: WrappedResponse<ProductResponse>) : AddState()
}

sealed class AddEvent {
    object MoveBack : AddEvent()
}

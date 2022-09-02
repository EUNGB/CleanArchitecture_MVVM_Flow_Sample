package com.eungb.cleanarchapp.presentation.main.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.domain.usecase.product.GetAllProductUseCase
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductUseCase: GetAllProductUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<ProductsState>()
    val state: SharedFlow<ProductsState> get() = _state

    private val _event = MutableSharedFlow<ProductEvent>()
    val event: SharedFlow<ProductEvent> get() = _event

    val loadingVisibleLiveData = MutableLiveData(View.INVISIBLE)

    val input = object : HomeInput {
        override fun onUpdateProducts() {
            getAllProducts()
        }
    }

    private val output = object : HomeOutput {
        override fun showLoading() {
            loadingVisibleLiveData.postValue(View.VISIBLE)
        }

        override fun hideLoading() {
            loadingVisibleLiveData.postValue(View.GONE)
        }

        override fun showExceptionDialog(message: String) {
            viewModelScope.launch {
                _state.emit(ProductsState.Exception(message))
            }
        }

        override fun showErrorDialog(error: WrappedListResponse<ProductResponse>) {
            viewModelScope.launch {
                _state.emit(ProductsState.Error(error))
            }
        }

        override fun displayProducts(products: List<ProductEntity>) {
            viewModelScope.launch {
                _state.emit(ProductsState.Success(products))
            }
        }

    }

    val route = object : HomeRoute {
        override fun toAdd() {
            viewModelScope.launch {
                _event.emit(ProductEvent.MoveAdd)
            }
        }

        override fun toUpdate(id: Int) {
            viewModelScope.launch {
                _event.emit(ProductEvent.UpdateProduct(id))
            }
        }

        override fun toDetail(id: Int) {
            viewModelScope.launch {
                _event.emit(ProductEvent.MoveDetail(id))
            }
        }
    }

    init {
//        input.onUpdateProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            getAllProductUseCase.invoke()
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
                        is BaseResult.Success -> output.displayProducts(it.data)
                        is BaseResult.Error -> output.showErrorDialog(it.rawResponse)
                    }
                }
        }
    }


    interface HomeInput {
        fun onUpdateProducts()
    }

    interface HomeOutput {
        fun showLoading()
        fun hideLoading()
        fun showExceptionDialog(message: String)
        fun showErrorDialog(error: WrappedListResponse<ProductResponse>)
        fun displayProducts(products: List<ProductEntity>)
    }

    interface HomeRoute {
        fun toAdd()
        fun toUpdate(id: Int)
        fun toDetail(id: Int)
    }
}

sealed class ProductsState {
    data class Exception(val message: String) : ProductsState()
    data class Success(val products: List<ProductEntity>) : ProductsState()
    data class Error(val error: WrappedListResponse<ProductResponse>) : ProductsState()
}

sealed class ProductEvent {
    object MoveAdd : ProductEvent()
    data class UpdateProduct(val id: Int) : ProductEvent()
    data class MoveDetail(val id: Int) : ProductEvent()
}

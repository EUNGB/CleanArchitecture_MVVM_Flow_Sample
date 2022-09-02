package com.eungb.cleanarchapp.domain.usecase.product

import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.domain.repository.ProductRepository
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {

    suspend fun invoke(productRequest: ProductRequest): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return repository.createProduct(productRequest)
    }

}
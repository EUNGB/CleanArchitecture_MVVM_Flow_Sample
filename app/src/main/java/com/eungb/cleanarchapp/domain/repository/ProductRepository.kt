package com.eungb.cleanarchapp.domain.repository

import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getAllProducts(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>

    suspend fun createProduct(productRequest: ProductRequest): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
}
package com.eungb.cleanarchapp.data.remote.datasource

import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult

interface ProductDataSource {

    suspend fun getAllProducts(): BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>

    suspend fun createProduct(productRequest: ProductRequest): BaseResult<ProductEntity, WrappedResponse<ProductResponse>>
}
package com.eungb.cleanarchapp.data.repository

import com.eungb.cleanarchapp.data.remote.datasource.ProductDataSource
import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.domain.repository.ProductRepository
import com.eungb.cleanarchapp.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val dataSource: ProductDataSource
) : ProductRepository {

    override suspend fun getAllProducts(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>> {
        return flow {
            emit(dataSource.getAllProducts())
        }
    }

    override suspend fun createProduct(productRequest: ProductRequest): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            emit(dataSource.createProduct(productRequest))
        }
    }

}
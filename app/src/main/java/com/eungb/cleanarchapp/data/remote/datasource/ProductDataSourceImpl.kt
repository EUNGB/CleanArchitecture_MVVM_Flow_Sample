package com.eungb.cleanarchapp.data.remote.datasource

import com.eungb.cleanarchapp.data.remote.api.ProductApi
import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.remote.utils.RemoteResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.presentation.common.base.BaseResult

class ProductDataSourceImpl(
    private val productApi: ProductApi
) : ProductDataSource {

    override suspend fun getAllProducts(): BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>> {
        val response = productApi.getAllProducts()
        return if (response.isSuccessful) {
            val body = response.body()!!
            val products = body.data!!.map { it -> ProductEntity(it.id!!, it.productName!!, it.price!!) }
            BaseResult.Success(products)
        } else {
            val error = RemoteResponse.getListResponseError(response)
            BaseResult.Error(error)
        }
    }

    override suspend fun createProduct(productRequest: ProductRequest): BaseResult<ProductEntity, WrappedResponse<ProductResponse>> {
        val response = productApi.createProduct(productRequest)
        return if (response.isSuccessful) {
            val body = response.body()!!
            val products = ProductEntity(body.data!!.id!!, body.data!!.productName!!, body.data!!.price!!)
            BaseResult.Success(products)
        } else {
            val error = RemoteResponse.getResponseError(response)
            BaseResult.Error(error)
        }
    }

}
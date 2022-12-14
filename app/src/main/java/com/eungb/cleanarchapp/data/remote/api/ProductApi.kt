package com.eungb.cleanarchapp.data.remote.api

import com.eungb.cleanarchapp.data.remote.dto.ProductRequest
import com.eungb.cleanarchapp.data.remote.dto.ProductResponse
import com.eungb.cleanarchapp.data.utils.WrappedListResponse
import com.eungb.cleanarchapp.data.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    @GET("/api/product")
    suspend fun getAllProducts(): Response<WrappedListResponse<ProductResponse>>

    @POST("/api/product")
    suspend fun createProduct(
        @Body productRequest: ProductRequest
    ): Response<WrappedResponse<ProductResponse>>

}
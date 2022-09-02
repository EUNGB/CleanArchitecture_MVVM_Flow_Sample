package com.eungb.cleanarchapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("product_name") val productName: String? = null,
    @SerializedName("price") val price: Int? = null,
    @SerializedName("user") val user: UserDTO? = null
)

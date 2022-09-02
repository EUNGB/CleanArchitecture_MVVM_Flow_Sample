package com.eungb.cleanarchapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("token") val token: String? = null
)

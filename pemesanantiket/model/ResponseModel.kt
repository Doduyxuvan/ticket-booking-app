package com.donisw.pemesanantiket.model

data class ResponseModel(
    val status: Boolean,
    val message: String,
    val data: UserModel?
)

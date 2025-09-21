package com.donisw.pemesanantiket.model

data class ResponsePengiriman(
    val success: Boolean,
    val data: List<PengirimanModel>?
)

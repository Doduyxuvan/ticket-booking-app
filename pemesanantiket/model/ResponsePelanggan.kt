package com.donisw.pemesanantiket.model

data class ResponsePelanggan(
    val status: Boolean,
    val message: String,
    val data: List<PelangganModel>
)

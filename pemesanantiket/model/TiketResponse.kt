package com.donisw.pemesanantiket.model

data class TiketResponse(
    val success: Boolean,
    val data: List<Tiket>
)

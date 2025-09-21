package com.donisw.pemesanantiket.model

data class ResponseTiket10(
    val sukses: Int,
    val message: String,
    val tiket: List<Tiket10>
)

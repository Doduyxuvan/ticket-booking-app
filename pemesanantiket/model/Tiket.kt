package com.donisw.pemesanantiket.model

data class Tiket(
    val id: Int,
    val user_id: Int,
    val tujuan: String,
    val tanggal: String,
    val jumlah: Int,
    val status: String,
    val created_at: String
)

package com.donisw.pemesanantiket.model

data class PelangganModel(
    val id: Int,
    val nama_lengkap: String,
    val username: String,
    val email: String,
    val no_hp: String,
    val role: String,
    val saldo: Int
)

package com.donisw.pemesanantiket.model

data class TiketModel(
    val id: Int,
    val nama: String,
    val asal: String,
    val tujuan: String,
    val tanggal: String,
    val telp: String,
    val nomorBangku: Int,
    val hargaTotal: Int,
    val kelas: String,
    val status: String,
    val createdAt: String,
    val statusPembayaran: String // ❌ ubah ke String
)



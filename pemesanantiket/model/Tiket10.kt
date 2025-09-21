package com.donisw.pemesanantiket.model

import com.google.gson.annotations.SerializedName

data class Tiket10(
    @SerializedName("id")
    val id: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("asal")
    val asal: String,

    @SerializedName("tujuan")
    val tujuan: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("telp")
    val telp: String,

    @SerializedName("nomor_bangku")
    val nomorBangku: String,   // ✅ Kotlin pakai camelCase

    @SerializedName("harga_total")
    val hargaTotal: String,    // ✅ Kotlin pakai camelCase

    @SerializedName("kelas")
    val kelas: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("metode")
    val metode: String,

    @SerializedName("status_pembayaran")
    val statusPembayaran: String  // ✅ camelCase di Kotlin
)
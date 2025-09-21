package com.donisw.pemesanantiket.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable   // ✅ supaya bisa dilempar via Intent

data class PengirimanModel(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("user_id")
    val user_id: Int?,

    @SerializedName("nama_pengirim")
    val nama_pengirim: String?,

    @SerializedName("no_hp")
    val no_hp: String?,

    @SerializedName("alamat_pengirim")
    val alamat_pengirim: String?,

    @SerializedName("alamat_penerima")
    val alamat_penerima: String?,

    @SerializedName("berat_barang")
    val berat_barang: Float?,   // ✅ sudah nullable

    @SerializedName("jenis_barang")
    val jenis_barang: String?,

    @SerializedName("total_biaya")
    val total_biaya: Int?,      // ✅ sudah nullable

    @SerializedName("created_at")
    val created_at: String?,

    @SerializedName("no_resi")
    val no_resi: String?,

    @SerializedName("lokasi_barang")
    val lokasi_barang: String?,

    @SerializedName("qr_string")
    val qr_string: String?,

    @SerializedName("status_pembayaran")
    val status_pembayaran: String?
) : Serializable   // ✅ penting: biar bisa dikirim antar-activity

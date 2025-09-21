// ApiService.kt
package com.donisw.pemesanantiket.api

import com.donisw.pemesanantiket.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // 🔐 Login
    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseModel>

    // 📝 Register akun
    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("no_hp") noHp: String,
        @Field("email") email: String
    ): Call<ResponseModel>

    // ✏️ Update profil user
    @FormUrlEncoded
    @POST("update_profil.php")
    fun updateProfil(
        @Field("id") id: Int,
        @Field("nama") nama: String,
        @Field("username") username: String,
        @Field("no_hp") noHp: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("saldo") saldo: String
    ): Call<ResponseModel>

    // ➕ Tambah tiket travel
    @FormUrlEncoded
    @POST("insert_travel.php")
    fun insertTravel(
        @Field("nama") nama: String,
        @Field("asal") asal: String,
        @Field("tujuan") tujuan: String,
        @Field("tanggal") tanggal: String,
        @Field("telp") telp: String,
        @Field("nomor_bangku") nomorBangku: String,
        @Field("harga_total") hargaTotal: Int,
        @Field("kelas") kelas: String,
        @Field("status") status: String
    ): Call<ResponseModel>

    // 📦 Kirim data pengiriman barang
    @FormUrlEncoded
    @POST("insert_pengiriman.php")
    fun kirimPengiriman(
        @Field("nama_pengirim") namaPengirim: String,
        @Field("no_hp") noHp: String,
        @Field("alamat_pengirim") alamatPengirim: String,
        @Field("alamat_penerima") alamatPenerima: String,
        @Field("berat_barang") beratBarang: String,
        @Field("jenis_barang") jenisBarang: String
    ): Call<ApiResponse>

    // 📦 Ambil semua data pengiriman
    @GET("get_pengiriman.php")
    fun getPengiriman(): Call<ResponsePengiriman>

    // 📦 Ambil detail pengiriman berdasarkan ID
    @GET("get_detail_pengiriman.php")
    fun getDetailPengiriman(
        @Query("id_pengiriman") id: Int
    ): Call<DetailPengirimanResponse>

    // ✏️ Edit pengiriman
    @FormUrlEncoded
    @POST("edit_pengiriman.php")
    fun editPengiriman(
        @Field("id") id: Int,
        @Field("nama_pengirim") namaPengirim: String,
        @Field("no_hp") noHp: String,
        @Field("alamat_pengirim") alamatPengirim: String,
        @Field("alamat_penerima") alamatPenerima: String,
        @Field("berat_barang") beratBarang: String,
        @Field("jenis_barang") jenisBarang: String,
        @Field("no_resi") noResi: String,
        @Field("lokasi_barang") lokasi: String,
        @Field("status_pembayaran") statusPembayaran: String   // ✅ tambahan
    ): Call<ResponseAction>


    // ✅ Ambil tiket berdasarkan user
    @GET("api/get_tiket_user.php")
    fun getTiketUser(
        @Query("user_id") userId: Int
    ): Call<TiketResponse>

    // ✅ Bayar otomatis tiket (potong saldo)
    @FormUrlEncoded
    @POST("api/bayar_otomatis.php")
    fun bayarOtomatis(
        @Field("user_id") userId: Int,
        @Field("tujuan") tujuan: String,
        @Field("tanggal") tanggal: String,
        @Field("jumlah") jumlah: Int
    ): Call<ResponseBody>

    // ✅ Ambil saldo user
    @GET("api/get_saldo.php")
    fun getSaldo(
        @Query("user_id") userId: Int
    ): Call<SaldoResponse>

    // ================================
    // 🧑‍🤝‍🧑 Data Pelanggan
    // ================================

    // ✅ Ambil semua pelanggan
    @GET("get_pelanggan.php")
    fun getAllPelanggan(): Call<ResponsePelanggan>

    // ✅ Ambil detail pelanggan berdasarkan ID
    @GET("get_detail_pelanggan.php")
    fun getDetailPelanggan(
        @Query("id") id: Int
    ): Call<PelangganModel>

    // ================================
    // 🎫 Tiket API Versi 10
    // ================================

    // Ambil semua tiket (versi 10)
    @GET("get_tiket10.php?action=list")
    fun getAllTiket10(): Call<ResponseTiket10>

    // Ambil detail tiket (versi 10)
    @GET("get_tiket10.php?action=detail")
    fun getTiketDetail10(
        @Query("id") id: Int
    ): Call<Tiket10>

    // Update tiket (versi 10)
    @FormUrlEncoded
    @POST("get_tiket10.php?action=update")
    fun updateTiket10(
        @Field("id") id: Int,
        @Field("nama") nama: String,
        @Field("asal") asal: String,
        @Field("tujuan") tujuan: String,
        @Field("tanggal") tanggal: String,
        @Field("telp") telp: String,
        @Field("nomor_bangku") nomorBangku: String,
        @Field("harga_total") hargaTotal: String,
        @Field("kelas") kelas: String,
        @Field("status") status: String,
        @Field("status_pembayaran") statusPembayaran: String   // ✅ tambahan
    ): Call<ResponseAction>
}

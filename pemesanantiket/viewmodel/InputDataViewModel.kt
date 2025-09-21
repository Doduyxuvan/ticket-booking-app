package com.donisw.pemesanantiket.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.api.ApiService
import com.donisw.pemesanantiket.database.DatabaseClient.Companion.getInstance
import com.donisw.pemesanantiket.database.dao.DatabaseDao
import com.donisw.pemesanantiket.model.ModelDatabase
import com.donisw.pemesanantiket.model.ResponseModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputDataViewModel(application: Application) : AndroidViewModel(application) {

    var databaseDao: DatabaseDao? = null

    fun addDataPemesan(
        nama_penumpang: String,
        keberangkatan: String,
        tujuan: String,
        tanggal: String,
        nomor_telepon: String,
        nomor_bangku: String,   // 🔥 ganti dari anak_anak/dewasa
        harga_tiket: Int,
        kelas: String,
        status: String
    ) {
        // ✅ Simpan ke database lokal (Room)
        Completable.fromAction {
            val modelDatabase = ModelDatabase().apply {
                namaPenumpang = nama_penumpang
                this.keberangkatan = keberangkatan
                this.tujuan = tujuan
                this.tanggal = tanggal
                this.nomorTelepon = nomor_telepon
                this.nomorBangku = nomor_bangku   // 🔥 pakai nomor bangku
                this.hargaTiket = harga_tiket
                this.kelas = kelas
                this.status = status
            }
            databaseDao?.insertData(modelDatabase)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        // ✅ Kirim ke server melalui Retrofit
        val api = ApiClient.apiService
        api.insertTravel(
            nama_penumpang,
            keberangkatan,
            tujuan,
            tanggal,
            nomor_telepon,
            nomor_bangku,   // 🔥 kirim nomor bangku
            harga_tiket,
            kelas,
            status
        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(getApplication(), "✅ Data berhasil dikirim ke server", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "❌ Gagal menyimpan ke server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(getApplication(), "⚠️ Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    init {
        try {
            databaseDao = getInstance(application)?.appDatabase?.databaseDao()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

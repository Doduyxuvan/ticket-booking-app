package com.donisw.pemesanantiket.view.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityAdminDashboardBinding
import com.donisw.pemesanantiket.model.ResponseTiket10
import com.donisw.pemesanantiket.utils.SharedPreferencesHelper
import com.donisw.pemesanantiket.view.auth.LoginActivity
import com.donisw.pemesanantiket.view.admin.LaporanActivity
import com.donisw.pemesanantiket.view.admin.DataPelangganActivity
import com.donisw.pemesanantiket.view.admin.DataPengirimanActivity1   // ✅ untuk edit
import com.donisw.pemesanantiket.view.admin.DataPengirimanActivity2   // ✅ untuk hanya lihat
import com.donisw.pemesanantiket.view.admin.DataTiketActivity2       // ✅ daftar tiket + search
import com.donisw.pemesanantiket.view.ticket.Ticket1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var sharedPref: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPreferencesHelper(this)

        // ✅ Cek login
        if (!sharedPref.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // 🔍 Tes API getAllTiket10 (sementara, buat debug aja)
        testApiGetAllTiket10()

        // 🧭 Navigasi
        binding.cardDataPelanggan.setOnClickListener {
            startActivity(Intent(this, DataPelangganActivity::class.java))
        }

        binding.cvLihatTiket.setOnClickListener {
            startActivity(Intent(this, Ticket1::class.java))
        }

        binding.cardListPengiriman.setOnClickListener {
            startActivity(Intent(this, DataPengirimanActivity2::class.java))
        }

        binding.cardEditTiket.setOnClickListener {
            startActivity(Intent(this, DataTiketActivity2::class.java))
        }

        binding.cardEditListPengiriman.setOnClickListener {
            startActivity(Intent(this, DataPengirimanActivity1::class.java))
        }

        binding.cardLaporan.setOnClickListener {
            startActivity(Intent(this, LaporanActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            sharedPref.clearUser()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun testApiGetAllTiket10() {
        ApiClient.apiService.getAllTiket10().enqueue(object : Callback<ResponseTiket10> {
            override fun onResponse(
                call: Call<ResponseTiket10>,
                response: Response<ResponseTiket10>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("API_TEST", "Sukses: ${data?.tiket?.size} tiket ditemukan")
                    data?.tiket?.forEach {
                        Log.d("API_TEST", "Tiket: ${it.nama} dari ${it.asal} ke ${it.tujuan}")
                    }
                } else {
                    Log.e("API_TEST", "Gagal response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseTiket10>, t: Throwable) {
                Log.e("API_TEST", "Error: ${t.message}", t)
            }
        })
    }
}

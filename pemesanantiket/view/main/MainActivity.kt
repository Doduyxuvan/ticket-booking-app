package com.donisw.pemesanantiket.view.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiService
import com.donisw.pemesanantiket.model.SaldoResponse
import com.donisw.pemesanantiket.view.auth.LoginActivity
import com.donisw.pemesanantiket.view.history.HistoryActivity
import com.donisw.pemesanantiket.view.input.*
import com.donisw.pemesanantiket.view.pengiriman.ListPengirimanActivity
import com.donisw.pemesanantiket.view.ticket.Ticket1
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.donisw.pemesanantiket.utils.SharedPreferencesHelper
import com.donisw.pemesanantiket.view.input.DataTravelActivity


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferencesHelper
    private lateinit var tvSaldo: TextView
    private val baseUrl = "http://192.168.18.4/php_api_login/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = SharedPreferencesHelper(this)
        val user = sharedPref.getUser()

        if (user == null) {
            Toast.makeText(this, "User tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userId = user.id
        findViewById<TextView>(R.id.tvNamaUser).text = user.nama
        tvSaldo = findViewById(R.id.tvSaldo)

        Log.d("DEBUG_SESSION", "user_name: ${user.nama} | user_id: $userId")

        setAutoGreeting()
        setStatusBar()
        setupNavigation()

        // ✅ Tampilkan saldo saat masuk
        // if (userId > 0) loadSaldo(userId) // TODO: Aktifkan kembali jika saldo digunakan

        // 🔘 BAYAR OTOMATIS
        findViewById<Button>(R.id.btnBayarOtomatis).setOnClickListener {
            Log.d("DEBUG_BTN", "Tombol Bayar Otomatis diklik")

            if (userId <= 0) {
                Toast.makeText(this, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
                Log.e("DEBUG_USER_ID", "User ID tidak valid: $userId")
                return@setOnClickListener
            }

            val tujuan = "Padangsidimpuan"
            val tanggal = "2025-08-01"
            val jumlah = 2

            bayarOtomatis(userId, tujuan, tanggal, jumlah)
        }

        // 🔘 LOGOUT
        findViewById<CardView>(R.id.cvLogout).setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun bayarOtomatis(userId: Int, tujuan: String, tanggal: String, jumlah: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.bayarOtomatis(userId, tujuan, tanggal, jumlah)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()?.string()
                if (response.isSuccessful && body != null) {
                    val json = org.json.JSONObject(body)
                    val message = json.getString("message")
                    val success = json.getBoolean("success")

                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                    // if (success) loadSaldo(userId) // TODO: Aktifkan kembali jika saldo digunakan
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@MainActivity, "Gagal: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadSaldo(userId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.getSaldo(userId)

        call.enqueue(object : Callback<SaldoResponse> {
            override fun onResponse(call: Call<SaldoResponse>, response: Response<SaldoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val saldo = response.body()?.saldo ?: 0
                    tvSaldo.text = "Saldo: Rp $saldo"
                } else {
                    Log.e("LoadSaldo", "Gagal mendapatkan saldo")
                }
            }

            override fun onFailure(call: Call<SaldoResponse>, t: Throwable) {
                Log.e("LoadSaldo", "Koneksi gagal: ${t.message}")
            }
        })
    }

    private fun setAutoGreeting() {
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greetingRes = when (currentHour) {
            in 5..11 -> R.string.greeting_morning
            in 12..16 -> R.string.greeting_afternoon
            in 17..20 -> R.string.greeting_evening
            else -> R.string.greeting_night
        }
        findViewById<TextView>(R.id.tvGreeting).text = getString(greetingRes)
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.imageProfile).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<CardView>(R.id.cvTravel).setOnClickListener {
            startActivity(Intent(this, DataTravelActivity::class.java))
        }

        findViewById<CardView>(R.id.cvPengiriman).setOnClickListener {
            startActivity(Intent(this, DataPengirimanActivity::class.java))
        }

        findViewById<CardView>(R.id.cvLihatPengiriman).setOnClickListener {
            startActivity(Intent(this, ListPengirimanActivity::class.java))
        }

        findViewById<CardView>(R.id.cvLihatTiket).setOnClickListener {
            startActivity(Intent(this, Ticket1::class.java))
        }
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }

        if (Build.VERSION.SDK_INT in 19..20) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { _, _ ->
                sharedPref.logout() // ✅ Gunakan nama fungsi yang benar
                Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            layoutParams.flags = if (on) {
                layoutParams.flags or bits
            } else {
                layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }
}

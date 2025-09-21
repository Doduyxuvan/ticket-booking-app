package com.donisw.pemesanantiket.view.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityLoginBinding
import com.donisw.pemesanantiket.model.ResponseModel
import com.donisw.pemesanantiket.model.UserModel
import com.donisw.pemesanantiket.view.user.AdminDashboardActivity
import com.donisw.pemesanantiket.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.donisw.pemesanantiket.utils.SharedPreferencesHelper


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SharedPreferencesHelper
        sharedPref = SharedPreferencesHelper(this)

        // ✅ Cek apakah user sudah login
        if (sharedPref.isLoggedIn()) {
            val user = sharedPref.getUser()
            val destination = if (user?.role.equals("admin", ignoreCase = true)) {
                AdminDashboardActivity::class.java
            } else {
                MainActivity::class.java
            }
            startActivity(Intent(this, destination))
            finish()
            return
        }

        // 🔒 Tombol Login ditekan
        binding.btnMasuk.setOnClickListener {
            val username = binding.etUsernameLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            if (username.isBlank() || password.isBlank()) {
                showToast("Username dan password harus diisi")
                return@setOnClickListener
            }

            if (username.length < 3) {
                showToast("Username minimal 3 karakter")
                return@setOnClickListener
            }

            val loading = ProgressDialog(this)
            loading.setMessage("Mohon tunggu...")
            loading.setCancelable(false)
            loading.show()

            val api = ApiClient.apiService
            api.login(username, password).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    loading.dismiss()
                    val res = response.body()

                    if (res?.status == true && res.data != null) {
                        val user: UserModel = res.data
                        sharedPref.saveLogin(user)

                        Log.d("LoginDebug", "User role: ${user.role}")

                        val destination = if (user.role.equals("admin", ignoreCase = true)) {
                            AdminDashboardActivity::class.java
                        } else {
                            MainActivity::class.java
                        }

                        startActivity(Intent(this@LoginActivity, destination))
                        finish()
                    } else {
                        showToast(res?.message ?: "Login gagal. Silakan coba lagi.")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.dismiss()
                    showToast("Koneksi gagal: ${t.message}")
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}

package com.donisw.pemesanantiket.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.api.ApiService
import com.donisw.pemesanantiket.databinding.ActivityRegisterBinding
import com.donisw.pemesanantiket.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val namaLengkap = binding.edtNama.text.toString().trim()
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val noHp = binding.edtNoHp.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()

            if (namaLengkap.isEmpty() || username.isEmpty() || password.isEmpty() || noHp.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val api = ApiClient.apiService
            api.register(namaLengkap, username, password, noHp, email)
                .enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                        val res = response.body()
                        if (res != null && res.status) {
                            Toast.makeText(this@RegisterActivity, "Berhasil daftar", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, res?.message ?: "Gagal daftar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Gagal: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}

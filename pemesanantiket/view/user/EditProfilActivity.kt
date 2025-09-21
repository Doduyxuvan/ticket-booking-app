package com.donisw.pemesanantiket.view.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityEditProfilBinding
import com.donisw.pemesanantiket.model.PelangganModel
import com.donisw.pemesanantiket.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfilBinding
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Ambil id dari intent
        userId = intent.getIntExtra("id", -1)

        if (userId != -1) {
            loadDetailPelanggan(userId)
        } else {
            Toast.makeText(this, "ID pelanggan tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Tombol Simpan → update profil
        binding.btnSimpan.setOnClickListener {
            updateProfil()
        }
    }

    // ✅ Load detail pelanggan berdasarkan id
    private fun loadDetailPelanggan(id: Int) {
        ApiClient.apiService.getDetailPelanggan(id).enqueue(object : Callback<PelangganModel> {
            override fun onResponse(call: Call<PelangganModel>, response: Response<PelangganModel>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // Isi ke field
                        binding.etId.setText(data.id.toString())
                        binding.etNama.setText(data.nama_lengkap)
                        binding.etNoHp.setText(data.no_hp)
                        binding.etEmail.setText(data.email)
                        binding.etUsername.setText(data.username)
                        binding.etSaldo.setText(data.saldo.toString())
                        // password biarkan kosong (admin bisa isi baru jika mau reset)
                    }
                } else {
                    Toast.makeText(this@EditProfilActivity, "Gagal ambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PelangganModel>, t: Throwable) {
                Toast.makeText(this@EditProfilActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ✅ Update data pelanggan
    private fun updateProfil() {
        val id = binding.etId.text.toString().toIntOrNull()
        val nama = binding.etNama.text.toString().trim()
        val noHp = binding.etNoHp.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()  // opsional
        val saldo = binding.etSaldo.text.toString().trim()

        // Validasi
        if (id == null || nama.isEmpty() || noHp.isEmpty() || email.isEmpty() || username.isEmpty() || saldo.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val call = ApiClient.apiService.updateProfil(id, nama, username, noHp, email, password, saldo)
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res.status) {
                        Toast.makeText(this@EditProfilActivity, "Profil berhasil diupdate", Toast.LENGTH_SHORT).show()
                        binding.tvResponse.text = res.message
                    } else {
                        Toast.makeText(this@EditProfilActivity, "Gagal update: ${res?.message}", Toast.LENGTH_SHORT).show()
                        binding.tvResponse.text = res?.message ?: "Unknown error"
                    }
                } else {
                    Toast.makeText(this@EditProfilActivity, "Gagal update (Server Error)", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@EditProfilActivity, "Gagal koneksi: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

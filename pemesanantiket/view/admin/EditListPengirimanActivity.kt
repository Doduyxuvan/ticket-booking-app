package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.databinding.ActivityEditListPengirimanBinding
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.ResponseAction
import com.donisw.pemesanantiket.model.PengirimanModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditListPengirimanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditListPengirimanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditListPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Isi spinner status pembayaran
        val statusOptions = resources.getStringArray(R.array.status_pembayaran_array)
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spStatusPembayaran.adapter = adapterSpinner

        // ✅ Ambil data dari Intent
        val pengiriman = intent.getSerializableExtra("pengiriman") as? PengirimanModel
        if (pengiriman != null) {
            isiForm(pengiriman)
        }

        // ✅ Tombol simpan update data
        binding.btnSimpan.setOnClickListener {
            val id = binding.etId.text.toString().toIntOrNull()
            val nama = binding.etNama.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val alamatPengirim = binding.etAlamatPengirim.text.toString()
            val alamatPenerima = binding.etAlamatPenerima.text.toString()
            val berat = binding.etBerat.text.toString()
            val jenis = binding.etJenis.text.toString()
            val resi = binding.etNoResi.text.toString()
            val lokasi = binding.etLokasi.text.toString()
            val statusPembayaran = binding.spStatusPembayaran.selectedItem.toString() // ✅ ambil dari spinner

            if (id == null || nama.isEmpty()) {
                Toast.makeText(this, "ID harus angka dan Nama wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("EditListPengiriman", "Mengirim data: id=$id, lokasi=$lokasi, status=$statusPembayaran")

            ApiClient.apiService.editPengiriman(
                id, nama, noHp, alamatPengirim, alamatPenerima, berat, jenis, resi, lokasi, statusPembayaran
            ).enqueue(object : Callback<ResponseAction> {
                override fun onResponse(
                    call: Call<ResponseAction>,
                    response: Response<ResponseAction>
                ) {
                    val res = response.body()
                    Log.d("EditListPengiriman", "Respon API: $res")

                    if (res != null && res.success) {
                        Toast.makeText(this@EditListPengirimanActivity, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@EditListPengirimanActivity, "Gagal update", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseAction>, t: Throwable) {
                    Log.e("EditListPengiriman", "Error koneksi: ${t.message}")
                    Toast.makeText(this@EditListPengirimanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // ✅ Fungsi untuk isi form otomatis
    private fun isiForm(data: PengirimanModel) {
        binding.etId.setText(data.id?.toString() ?: "")
        binding.etNama.setText(data.nama_pengirim ?: "")
        binding.etNoHp.setText(data.no_hp ?: "")
        binding.etAlamatPengirim.setText(data.alamat_pengirim ?: "")
        binding.etAlamatPenerima.setText(data.alamat_penerima ?: "")
        binding.etBerat.setText(data.berat_barang?.toString() ?: "")
        binding.etJenis.setText(data.jenis_barang ?: "")
        binding.etNoResi.setText(data.no_resi ?: "")
        binding.etLokasi.setText(data.lokasi_barang ?: "")

        // ✅ set spinner status
        val status = data.status_pembayaran ?: "Pending"
        val pos = resources.getStringArray(R.array.status_pembayaran_array).indexOf(status)
        if (pos >= 0) {
            binding.spStatusPembayaran.setSelection(pos)
        }
    }
}

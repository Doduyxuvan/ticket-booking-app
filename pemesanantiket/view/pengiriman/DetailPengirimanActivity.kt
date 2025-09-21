package com.donisw.pemesanantiket.view.pengiriman

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.DetailPengirimanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPengirimanActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvNoHp: TextView
    private lateinit var tvAlamatPengirim: TextView
    private lateinit var tvAlamatPenerima: TextView
    private lateinit var tvBerat: TextView
    private lateinit var tvJenis: TextView
    private lateinit var tvBiaya: TextView
    private lateinit var tvResi: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvStatus: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pengiriman)

        // Inisialisasi view
        tvNama = findViewById(R.id.tvNama)
        tvNoHp = findViewById(R.id.tvNoHp)
        tvAlamatPengirim = findViewById(R.id.tvAlamatPengirim)
        tvAlamatPenerima = findViewById(R.id.tvAlamatPenerima)
        tvBerat = findViewById(R.id.tvBerat)
        tvJenis = findViewById(R.id.tvJenis)
        tvBiaya = findViewById(R.id.tvBiaya)
        tvResi = findViewById(R.id.tvResi)
        tvLokasi = findViewById(R.id.tvLokasi)
        tvStatus = findViewById(R.id.tvStatus)
        progressBar = findViewById(R.id.progressBar)

        val idPengiriman = intent.getIntExtra("id_pengiriman", -1)

        if (idPengiriman != -1) {
            loadDetail(idPengiriman)
        } else {
            Toast.makeText(this, "ID pengiriman tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadDetail(id: Int) {
        progressBar.visibility = View.VISIBLE
        Log.d("DETAIL_PENGIRIMAN", "Memanggil API getDetailPengiriman dengan ID: $id")

        ApiClient.apiService.getDetailPengiriman(id)
            .enqueue(object : Callback<DetailPengirimanResponse> {
                override fun onResponse(
                    call: Call<DetailPengirimanResponse>,
                    response: Response<DetailPengirimanResponse>
                ) {
                    progressBar.visibility = View.GONE

                    // Log detail response
                    Log.d("DETAIL_PENGIRIMAN", "HTTP Code: ${response.code()}")
                    Log.d("DETAIL_PENGIRIMAN", "Raw Response: ${response.raw()}")
                    Log.d("DETAIL_PENGIRIMAN", "Error Body: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("DETAIL_PENGIRIMAN", "Parsed Body: $body")

                        if (body != null && body.success && body.data != null) {
                            val data = body.data
                            tvNama.text = data.nama_pengirim ?: "-"
                            tvNoHp.text = data.no_hp ?: "-"
                            tvAlamatPengirim.text = data.alamat_pengirim ?: "-"
                            tvAlamatPenerima.text = data.alamat_penerima ?: "-"
                            tvBerat.text = "${data.berat_barang ?: 0} Kg"
                            tvJenis.text = data.jenis_barang ?: "-"
                            tvBiaya.text = "Rp ${data.total_biaya ?: 0}"
                            tvResi.text = data.no_resi ?: "-"
                            tvLokasi.text = data.lokasi_barang ?: "-"
                            tvStatus.text = data.status_pembayaran ?: "-"
                        } else {
                            Toast.makeText(
                                this@DetailPengirimanActivity,
                                "Data tidak ditemukan di server",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailPengirimanActivity,
                            "Gagal mengambil data detail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DetailPengirimanResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@DetailPengirimanActivity,
                        "Gagal koneksi: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("DETAIL_PENGIRIMAN", "API call failed", t)
                }
            })
    }
}

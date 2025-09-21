package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityDataPelangganBinding
import com.donisw.pemesanantiket.model.PelangganModel
import com.donisw.pemesanantiket.model.ResponsePelanggan
import com.donisw.pemesanantiket.adapter.PelangganAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataPelangganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPelangganBinding
    private lateinit var adapter: PelangganAdapter
    private var pelangganList: List<PelangganModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPelangganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPelanggan.layoutManager = LinearLayoutManager(this)

        // 🔍 Search listener
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) {
                    adapter.filter.filter(newText)
                }
                return true
            }
        })

        loadPelanggan()
    }

    private fun loadPelanggan() {
        ApiClient.apiService.getAllPelanggan().enqueue(object : Callback<ResponsePelanggan> {
            override fun onResponse(call: Call<ResponsePelanggan>, response: Response<ResponsePelanggan>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res.status) {
                        pelangganList = res.data
                        adapter = PelangganAdapter(this@DataPelangganActivity, pelangganList)
                        binding.rvPelanggan.adapter = adapter
                    } else {
                        Toast.makeText(this@DataPelangganActivity, res?.message ?: "Gagal ambil data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DataPelangganActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponsePelanggan>, t: Throwable) {
                Toast.makeText(this@DataPelangganActivity, "Koneksi gagal: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

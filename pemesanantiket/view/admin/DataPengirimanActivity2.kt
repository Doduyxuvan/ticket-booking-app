package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityDataPengiriman2Binding
import com.donisw.pemesanantiket.model.PengirimanModel
import com.donisw.pemesanantiket.model.ResponsePengiriman
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataPengirimanActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityDataPengiriman2Binding
    private lateinit var adapter: PengirimanAdapter2
    private var pengirimanList: List<PengirimanModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPengiriman2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPengiriman.layoutManager = LinearLayoutManager(this)

        // 🔍 Search
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) {
                    adapter.filter.filter(newText)
                }
                return true
            }
        })

        loadPengiriman()
    }

    private fun loadPengiriman() {
        ApiClient.apiService.getPengiriman().enqueue(object : Callback<ResponsePengiriman> {
            override fun onResponse(call: Call<ResponsePengiriman>, response: Response<ResponsePengiriman>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null && res.success) {
                        pengirimanList = res.data ?: emptyList()
                        adapter = PengirimanAdapter2(pengirimanList)
                        binding.rvPengiriman.adapter = adapter
                    } else {
                        Toast.makeText(this@DataPengirimanActivity2, "Gagal ambil data", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DataPengirimanActivity2, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponsePengiriman>, t: Throwable) {
                Toast.makeText(this@DataPengirimanActivity2, "Koneksi gagal: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

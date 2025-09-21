package com.donisw.pemesanantiket.view.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.databinding.ActivityDataPengiriman1Binding
import com.donisw.pemesanantiket.model.PengirimanModel
import com.donisw.pemesanantiket.model.ResponsePengiriman
import com.donisw.pemesanantiket.adapter.PengirimanAdapter1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataPengirimanActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityDataPengiriman1Binding
    private lateinit var adapter: PengirimanAdapter1
    private var pengirimanList: List<PengirimanModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPengiriman1Binding.inflate(layoutInflater)
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

                        // ✅ klik item kirim full object, bukan cuma id
                        adapter = PengirimanAdapter1(pengirimanList) { pengiriman: PengirimanModel ->
                            val intent = Intent(this@DataPengirimanActivity1, EditListPengirimanActivity::class.java)
                            intent.putExtra("pengiriman", pengiriman) // <-- kirim seluruh data
                            startActivity(intent)
                        }
                        binding.rvPengiriman.adapter = adapter
                    } else {
                        Toast.makeText(this@DataPengirimanActivity1, "Gagal ambil data pengiriman", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DataPengirimanActivity1, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponsePengiriman>, t: Throwable) {
                Toast.makeText(this@DataPengirimanActivity1, "Koneksi gagal: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

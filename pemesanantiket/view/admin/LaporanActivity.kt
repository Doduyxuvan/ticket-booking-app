package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.donisw.pemesanantiket.databinding.ActivityLaporanBinding
import org.json.JSONObject
import com.donisw.pemesanantiket.model.LaporanModel
import android.widget.SearchView

class LaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanBinding
    private lateinit var laporanAdapter: LaporanAdapter
    private val laporanList = ArrayList<LaporanModel>()
    private val laporanListFull = ArrayList<LaporanModel>() // backup untuk filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        laporanAdapter = LaporanAdapter(laporanList)
        binding.rvLaporan.layoutManager = LinearLayoutManager(this)
        binding.rvLaporan.adapter = laporanAdapter

        // 🔎 SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterData(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }
        })

        getDataLaporan()
    }

    private fun getDataLaporan() {
        val url = "http://10.0.2.22/php_api_login/api/get_laporan.php"

        val request = StringRequest(url, { response ->
            try {
                val rootObj = JSONObject(response)
                val success = rootObj.getBoolean("success")
                if (success) {
                    val laporanArray = rootObj.getJSONArray("laporan")
                    laporanList.clear()
                    laporanListFull.clear()
                    for (i in 0 until laporanArray.length()) {
                        val obj = laporanArray.getJSONObject(i)
                        val laporan = LaporanModel(
                            id = obj.optString("id", ""),
                            nama = obj.optString("nama", ""),
                            tanggal = obj.optString("tanggal", ""),
                            jenis = obj.optString("jenis", "Tidak Diketahui"),
                            total = obj.optInt("total", 0).toString()
                        )
                        laporanList.add(laporan)
                        laporanListFull.add(laporan) // backup data full
                    }
                    laporanAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Gagal: Respon tidak sukses", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal parsing data: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }, {
            Toast.makeText(this, "Gagal koneksi: ${it.message}", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(this).add(request)
    }

    // 🔎 Filter data sesuai query
    private fun filterData(query: String?) {
        val filtered = if (query.isNullOrEmpty()) {
            laporanListFull
        } else {
            laporanListFull.filter {
                it.nama.contains(query, ignoreCase = true) ||
                        it.jenis.contains(query, ignoreCase = true) ||
                        it.tanggal.contains(query, ignoreCase = true) ||
                        it.total.contains(query, ignoreCase = true)
            }
        }
        laporanList.clear()
        laporanList.addAll(filtered)
        laporanAdapter.notifyDataSetChanged()
    }
}

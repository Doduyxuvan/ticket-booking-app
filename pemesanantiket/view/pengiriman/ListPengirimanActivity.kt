package com.donisw.pemesanantiket.view.pengiriman

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.PengirimanModel
import com.donisw.pemesanantiket.model.ResponsePengiriman
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPengirimanActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PengirimanAdapter
    private lateinit var progressBar: ProgressBar
    private val listData = mutableListOf<PengirimanModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pengiriman)

        recyclerView = findViewById(R.id.recyclerPengiriman)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PengirimanAdapter(listData)
        recyclerView.adapter = adapter

        loadData()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getPengiriman().enqueue(object : Callback<ResponsePengiriman> {
            override fun onResponse(
                call: Call<ResponsePengiriman>,
                response: Response<ResponsePengiriman>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.success == true) {
                    listData.clear()
                    response.body()?.data?.let {
                        listData.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ListPengirimanActivity, "Data kosong atau gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponsePengiriman>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ListPengirimanActivity, "Gagal terhubung: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

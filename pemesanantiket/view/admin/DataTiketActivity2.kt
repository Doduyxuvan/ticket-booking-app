package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.ResponseTiket10
import com.donisw.pemesanantiket.model.Tiket10
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataTiketActivity2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TiketAdapter5
    private lateinit var searchBar: EditText
    private var tiketList: List<Tiket10> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_tiket2)

        recyclerView = findViewById(R.id.recyclerViewTiket)
        searchBar = findViewById(R.id.etSearchTiket)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadData()

        // 🔍 Search realtime
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (::adapter.isInitialized) {
                    adapter.filter.filter(s.toString())
                }
            }
        })
    }

    private fun loadData() {
        // ✅ Pakai API baru getAllTiket10()
        ApiClient.apiService.getAllTiket10().enqueue(object : Callback<ResponseTiket10> {
            override fun onResponse(
                call: Call<ResponseTiket10>,
                response: Response<ResponseTiket10>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.tiket ?: emptyList()

                    // 🔍 Debug response
                    Log.d("API_DEBUG", "Response body: $body")
                    for (tiket in list) {
                        Log.d(
                            "API_DEBUG",
                            "Tiket -> nama=${tiket.nama}, metode=${tiket.metode}, statusPembayaran=${tiket.statusPembayaran}"
                        )
                    }

                    tiketList = list
                    adapter = TiketAdapter5(tiketList, this@DataTiketActivity2)
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(
                        this@DataTiketActivity2,
                        "Gagal memuat data dari server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseTiket10>, t: Throwable) {
                Toast.makeText(
                    this@DataTiketActivity2,
                    "Error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("API_DEBUG", "onFailure: ${t.localizedMessage}", t)
            }
        })
    }
}

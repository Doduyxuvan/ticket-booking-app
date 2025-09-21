package com.donisw.pemesanantiket.view.ticket

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.model.TiketModel

class Ticket1 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tiketAdapter: TiketAdapter99
    private lateinit var progressBar: ProgressBar
    private val tiketList = mutableListOf<TiketModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket1)

        setupToolbar()

        recyclerView = findViewById(R.id.rvTicket)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        tiketAdapter = TiketAdapter99(tiketList)
        recyclerView.adapter = tiketAdapter

        // 🔍 SearchView
        val searchView: SearchView = findViewById(R.id.searchTiket)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                tiketAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tiketAdapter.filter.filter(newText)
                return true
            }
        })

        // 🔄 Load data tiket
        loadTiket()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbarTicket)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Data Tiket"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadTiket() {
        val url = "http://192.168.18.4/php_api_login/get_tiket1.php"
        progressBar.visibility = View.VISIBLE

        val request = JsonArrayRequest(url,
            { response ->
                tiketList.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    tiketList.add(
                        TiketModel(
                            id = obj.getInt("id"),
                            nama = obj.getString("nama"),
                            asal = obj.getString("asal"),
                            tujuan = obj.getString("tujuan"),
                            tanggal = obj.getString("tanggal"),
                            telp = obj.getString("telp"),
                            nomorBangku = obj.getInt("nomor_bangku"),
                            // ✅ pakai getString lalu amanin ke Int
                            hargaTotal = obj.getString("harga_total").toIntOrNull() ?: 0,
                            kelas = obj.getString("kelas"),
                            status = obj.getString("status"),
                            createdAt = obj.optString("created_at", ""),
                            statusPembayaran = obj.optString("status_pembayaran", "pending")
                        )
                    )
                }
                tiketAdapter.notifyDataSetChanged()
                tiketAdapter.tiketListFull = ArrayList(tiketList) // ✅ update backup list
                progressBar.visibility = View.GONE
            },
            { error ->
                Toast.makeText(this, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            })

        Volley.newRequestQueue(this).add(request)
    }
}

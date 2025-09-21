package com.donisw.pemesanantiket.view.admin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.ResponseAction
import com.donisw.pemesanantiket.model.Tiket10
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTicketActivity2 : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etAsal: EditText
    private lateinit var etTujuan: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etTelp: EditText
    private lateinit var spNomorBangku: Spinner
    private lateinit var etHargaTotal: EditText
    private lateinit var etKelas: EditText
    private lateinit var etStatus: EditText
    private lateinit var spStatusPembayaran: Spinner
    private lateinit var btnUpdateTiket: Button

    private var tiketId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ticket2)

        // Inisialisasi view
        etNama = findViewById(R.id.etNama)
        etAsal = findViewById(R.id.etAsal)
        etTujuan = findViewById(R.id.etTujuan)
        etTanggal = findViewById(R.id.etTanggal)
        etTelp = findViewById(R.id.etTelp)
        spNomorBangku = findViewById(R.id.spNomorBangku)
        etHargaTotal = findViewById(R.id.etHargaTotal)
        etKelas = findViewById(R.id.etKelas)
        etStatus = findViewById(R.id.etStatus)
        spStatusPembayaran = findViewById(R.id.spStatusPembayaran)
        btnUpdateTiket = findViewById(R.id.btnUpdateTiket)

        // Isi spinner nomor bangku 1-6
        val bangkuList = listOf("1", "2", "3", "4", "5", "6")
        val adapterBangku = ArrayAdapter(this, android.R.layout.simple_spinner_item, bangkuList)
        adapterBangku.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spNomorBangku.adapter = adapterBangku

        // Isi spinner status pembayaran
        val statusList = listOf("pending", "paid", "expired")
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatusPembayaran.adapter = adapterStatus

        tiketId = intent.getIntExtra("id", -1)

        if (tiketId != -1) {
            loadTiketById(tiketId)
        }

        btnUpdateTiket.setOnClickListener {
            updateTiket()
        }
    }

    private fun loadTiketById(id: Int) {
        ApiClient.apiService.getTiketDetail10(id).enqueue(object : Callback<Tiket10> {
            override fun onResponse(call: Call<Tiket10>, response: Response<Tiket10>) {
                if (response.isSuccessful) {
                    val tiket = response.body()
                    tiket?.let {
                        etNama.setText(it.nama)
                        etAsal.setText(it.asal)
                        etTujuan.setText(it.tujuan)
                        etTanggal.setText(it.tanggal)
                        etTelp.setText(it.telp)

                        // pilih nomor bangku sesuai data
                        val posBangku = (spNomorBangku.adapter as ArrayAdapter<String>).getPosition(it.nomorBangku)
                        if (posBangku >= 0) spNomorBangku.setSelection(posBangku)

                        etHargaTotal.setText(it.hargaTotal.toString())
                        etKelas.setText(it.kelas)
                        etStatus.setText(it.status)

                        // pilih status pembayaran sesuai data
                        val posStatus = (spStatusPembayaran.adapter as ArrayAdapter<String>).getPosition(it.statusPembayaran)
                        if (posStatus >= 0) spStatusPembayaran.setSelection(posStatus)
                    }
                } else {
                    Toast.makeText(this@EditTicketActivity2, "Gagal memuat data tiket", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tiket10>, t: Throwable) {
                Toast.makeText(this@EditTicketActivity2, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTiket() {
        val nama = etNama.text.toString()
        val asal = etAsal.text.toString()
        val tujuan = etTujuan.text.toString()
        val tanggal = etTanggal.text.toString()
        val telp = etTelp.text.toString()
        val nomorBangku = spNomorBangku.selectedItem.toString()
        val hargaTotal = etHargaTotal.text.toString()
        val kelas = etKelas.text.toString()
        val status = etStatus.text.toString()
        val statusPembayaran = spStatusPembayaran.selectedItem.toString()

        ApiClient.apiService.updateTiket10(
            tiketId,
            nama,
            asal,
            tujuan,
            tanggal,
            telp,
            nomorBangku,
            hargaTotal,
            kelas,
            status,
            statusPembayaran // ✅ tambahan
        ).enqueue(object : Callback<ResponseAction> {
            override fun onResponse(call: Call<ResponseAction>, response: Response<ResponseAction>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditTicketActivity2, "Berhasil update tiket", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditTicketActivity2, "Gagal update tiket", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseAction>, t: Throwable) {
                Toast.makeText(this@EditTicketActivity2, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

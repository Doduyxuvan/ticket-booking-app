package com.donisw.pemesanantiket.view.input

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R

class DataPengirimanActivity : AppCompatActivity() {

    private lateinit var etNamaPengirim: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etAlamatPengirim: EditText
    private lateinit var etAlamatPenerima: EditText
    private lateinit var etBeratBarang: EditText
    private lateinit var etJenisBarang: EditText
    private lateinit var tvTotalBiaya: TextView
    private lateinit var btnPesanSekarang: Button
    private lateinit var btnBack: ImageButton

    private var totalBiaya = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pengiriman)

        etNamaPengirim = findViewById(R.id.etNamaPengirim)
        etNoHp = findViewById(R.id.etNoHp)
        etAlamatPengirim = findViewById(R.id.etAlamatPengirim)
        etAlamatPenerima = findViewById(R.id.etAlamatPenerima)
        etBeratBarang = findViewById(R.id.etBeratBarang)
        etJenisBarang = findViewById(R.id.etJenisBarang)
        tvTotalBiaya = findViewById(R.id.tvTotalBiaya)
        btnPesanSekarang = findViewById(R.id.btnPesanSekarang)
        btnBack = findViewById(R.id.btnBack)

        // Tombol Back
        btnBack.setOnClickListener {
            finish() // kembali ke activity sebelumnya
        }

        // Hitung total biaya otomatis berdasarkan berat barang
        etBeratBarang.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val berat = s?.toString()?.toDoubleOrNull() ?: 0.0
                totalBiaya = (berat * 10000).toInt()
                tvTotalBiaya.text = "Total Biaya: Rp$totalBiaya"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Tombol lanjut → halaman Payment
        btnPesanSekarang.setOnClickListener {
            if (isFormValid()) {
                val intent = Intent(this, PaymentPengirimanActivity::class.java).apply {
                    putExtra("NAMA_PENGIRIM", etNamaPengirim.text.toString())
                    putExtra("NO_HP", etNoHp.text.toString())
                    putExtra("ALAMAT_PENGIRIM", etAlamatPengirim.text.toString())
                    putExtra("ALAMAT_PENERIMA", etAlamatPenerima.text.toString())
                    putExtra("BERAT_BARANG", etBeratBarang.text.toString())
                    putExtra("JENIS_BARANG", etJenisBarang.text.toString())
                    putExtra("TOTAL_BIAYA", totalBiaya)
                }
                startActivity(intent)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return if (
            etNamaPengirim.text.isBlank() ||
            etNoHp.text.isBlank() ||
            etAlamatPengirim.text.isBlank() ||
            etAlamatPenerima.text.isBlank() ||
            etBeratBarang.text.isBlank() ||
            etJenisBarang.text.isBlank()
        ) {
            Toast.makeText(this, "Lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}

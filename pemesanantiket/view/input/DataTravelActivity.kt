package com.donisw.pemesanantiket.view.input

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.view.main.MainActivity
import com.donisw.pemesanantiket.view.payment.PaymentMethodActivity
import java.text.SimpleDateFormat
import java.util.*

class DataTravelActivity : AppCompatActivity() {

    lateinit var sAsal: String
    lateinit var sTujuan: String
    lateinit var sTanggal: String
    lateinit var sNama: String
    lateinit var sTelp: String
    lateinit var sKelas: String
    lateinit var sNomorBangku: String

    var hargaTotal = 0

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var inputTanggal: EditText
    lateinit var inputNama: EditText
    lateinit var inputTelp: EditText
    lateinit var spBerangkat: Spinner
    lateinit var spTujuan: Spinner
    lateinit var spKelas: Spinner
    lateinit var spNomorBangku: Spinner
    lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        initViews()
        // setStatusBar()   👉 dihapus biar toolbar turun normal
        setToolbar()
        setInitView()
        setInputData()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        inputTanggal = findViewById(R.id.inputTanggal)
        inputNama = findViewById(R.id.inputNama)
        inputTelp = findViewById(R.id.inputTelepon)
        spBerangkat = findViewById(R.id.spBerangkat)
        spTujuan = findViewById(R.id.spTujuan)
        spKelas = findViewById(R.id.spKelas)
        spNomorBangku = findViewById(R.id.spNomorBangku)
        btnCheckout = findViewById(R.id.btnCheckout)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // tombol back aktif
        supportActionBar?.title = "Input Data Travel"

        // Aksi tombol back di toolbar
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun setInitView() {
        inputTanggal.setOnClickListener {
            val tanggalJemput = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                tanggalJemput.set(year, month, day)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                inputTanggal.setText(format.format(tanggalJemput.time))
            }
            DatePickerDialog(
                this, dateSetListener,
                tanggalJemput.get(Calendar.YEAR),
                tanggalJemput.get(Calendar.MONTH),
                tanggalJemput.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        spBerangkat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sAsal = p.getItemAtPosition(pos).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sTujuan = p.getItemAtPosition(pos).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sKelas = p.getItemAtPosition(pos).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spNomorBangku.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sNomorBangku = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setInputData() {
        btnCheckout.setOnClickListener {
            Log.d("DEBUG_FLOW", "Checkout button clicked")
            setPerhitunganHargaTiket()
            sNama = inputNama.text.toString()
            sTanggal = inputTanggal.text.toString()
            sTelp = inputTelp.text.toString()

            if (sNama.isEmpty() || sTanggal.isEmpty() || sTelp.isEmpty()
                || sAsal.isEmpty() || sTujuan.isEmpty() || sKelas.isEmpty()
            ) {
                Toast.makeText(this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_SHORT).show()
            } else if (sAsal == sTujuan) {
                Toast.makeText(this, "Asal dan Tujuan tidak boleh sama!", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, PaymentMethodActivity::class.java)
                intent.putExtra("nama", sNama)
                intent.putExtra("asal", sAsal)
                intent.putExtra("tujuan", sTujuan)
                intent.putExtra("tanggal", sTanggal)
                intent.putExtra("telp", sTelp)
                intent.putExtra("kelas", sKelas)
                intent.putExtra("bangku", sNomorBangku)
                intent.putExtra("harga", hargaTotal)
                startActivity(intent)
            }
        }
    }

    private fun setPerhitunganHargaTiket() {
        hargaTotal = when {
            sAsal == "Aceh" && sTujuan == "Medan" -> 500000
            sAsal == "Medan" && sTujuan == "Padangsidimpuan" -> 600000
            sAsal == "Padangsidimpuan" && sTujuan == "Lampung" -> 700000
            sAsal == "Aceh" && sTujuan == "Lampung" -> 1000000
            else -> 400000
        }

        hargaTotal += when (sKelas.lowercase()) {
            "eksekutif", "bisnis" -> 500000
            "ekonomi" -> 100000
            else -> 0
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) layoutParams.flags = layoutParams.flags or bits
            else layoutParams.flags = layoutParams.flags and bits.inv()
            window.attributes = layoutParams
        }
    }
}

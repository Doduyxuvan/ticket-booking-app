package com.donisw.pemesanantiket.view.payment

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.api.ApiClient
import com.donisw.pemesanantiket.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnConfirm: Button

    private var nama = ""
    private var asal = ""
    private var tujuan = ""
    private var tanggal = ""
    private var telp = ""
    private var kelas = ""
    private var bangku = ""
    private var harga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        // ✅ Ambil data dari DataTravelActivity via Intent
        nama = intent.getStringExtra("nama") ?: ""
        asal = intent.getStringExtra("asal") ?: ""
        tujuan = intent.getStringExtra("tujuan") ?: ""
        tanggal = intent.getStringExtra("tanggal") ?: ""
        telp = intent.getStringExtra("telp") ?: ""
        kelas = intent.getStringExtra("kelas") ?: ""
        bangku = intent.getStringExtra("bangku") ?: ""
        harga = intent.getIntExtra("harga", 0)

        radioGroup = findViewById(R.id.radioGroup)
        btnConfirm = findViewById(R.id.btnConfirm)

        btnConfirm.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selected = findViewById<RadioButton>(selectedId)
                when (selected.text.toString()) {
                    "QRIS" -> showQrisDialog()
                    "Bank BRI" -> showBankDialog("BANK BRI", "1234567890")
                    "Bank BNI" -> showBankDialog("BANK BNI", "9876543210")
                }
            } else {
                Toast.makeText(this, "Pilih metode pembayaran dulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Dialog QRIS dengan timer
    private fun showQrisDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_qris_payment, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        val tvTotalQris = dialogView.findViewById<TextView>(R.id.tvTotalQris)
        val tvTimer = dialogView.findViewById<TextView>(R.id.tvTimer)
        val btnSudahBayar = dialogView.findViewById<Button>(R.id.btnSudahBayar)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)

        tvTotalQris.text = "Total: Rp $harga"

        // ✅ Timer 5 menit
        object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val menit = millisUntilFinished / 1000 / 60
                val detik = (millisUntilFinished / 1000) % 60
                tvTimer.text = String.format("%02d:%02d", menit, detik)
            }

            override fun onFinish() {
                tvTimer.text = "Waktu habis"
                Toast.makeText(this@PaymentMethodActivity, "Waktu pembayaran habis", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.start()

        btnSudahBayar.setOnClickListener {
            kirimDataPembayaran("QRIS")
            dialog.dismiss()
        }

        btnBatal.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(false)
        dialog.show()
    }

    // ✅ Dialog Bank Transfer (BRI / BNI) dengan timer
    private fun showBankDialog(namaBank: String, noRek: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_bank_transfer, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()

        val tvNamaBank = dialogView.findViewById<TextView>(R.id.tvNamaBank)
        val tvNoRekening = dialogView.findViewById<TextView>(R.id.tvNoRekening)
        val tvTotal = dialogView.findViewById<TextView>(R.id.tvTotalTransfer)
        val tvTimer = dialogView.findViewById<TextView>(R.id.tvTimerBank)
        val btnSudahTransfer = dialogView.findViewById<Button>(R.id.btnSudahTransfer)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)

        tvNamaBank.text = namaBank
        tvNoRekening.text = noRek
        tvTotal.text = "Total: Rp $harga"

        // ✅ Timer 5 menit
        object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val menit = millisUntilFinished / 1000 / 60
                val detik = (millisUntilFinished / 1000) % 60
                tvTimer.text = String.format("%02d:%02d", menit, detik)
            }

            override fun onFinish() {
                tvTimer.text = "Waktu habis"
                Toast.makeText(this@PaymentMethodActivity, "Waktu pembayaran habis", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.start()

        btnSudahTransfer.setOnClickListener {
            kirimDataPembayaran(namaBank)
            dialog.dismiss()
        }

        btnBatal.setOnClickListener { dialog.dismiss() }

        dialog.setCancelable(false)
        dialog.show()
    }

    // ✅ Kirim data ke database
    private fun kirimDataPembayaran(metode: String) {
        ApiClient.apiService.insertTravel(
            nama,
            asal,
            tujuan,
            tanggal,
            telp,
            bangku,
            harga,
            kelas,
            metode  // simpan metode yang dipilih
        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(this@PaymentMethodActivity, "Pemesanan berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@PaymentMethodActivity, "Gagal kirim data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@PaymentMethodActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

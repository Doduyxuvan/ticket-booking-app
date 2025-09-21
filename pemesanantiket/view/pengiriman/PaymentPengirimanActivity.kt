package com.donisw.pemesanantiket.view.input

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.donisw.pemesanantiket.R
import org.json.JSONObject

class PaymentPengirimanActivity : AppCompatActivity() {

    private lateinit var rbQris: RadioButton
    private lateinit var rbBRI: RadioButton
    private lateinit var rbBNI: RadioButton
    private lateinit var btnConfirm: Button

    private var totalBiaya: Int = 0
    private var userId: String = "3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_pengiriman)

        rbQris = findViewById(R.id.rbQris)
        rbBRI = findViewById(R.id.rbBri)
        rbBNI = findViewById(R.id.rbBni)
        btnConfirm = findViewById(R.id.btnConfirm)

        // Ambil biaya dari intent
        totalBiaya = intent.getIntExtra("TOTAL_BIAYA", 0)

        btnConfirm.setOnClickListener {
            when {
                rbQris.isChecked -> showQrisDialog()
                rbBRI.isChecked -> showTransferDialog("BRI", "1234567890 a.n PT. PMTS BRI")
                rbBNI.isChecked -> showTransferDialog("BNI", "0987654321 a.n PT. PMTS BNI")
                else -> Toast.makeText(this, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showQrisDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_qris4, null)
        val qrImageView = dialogView.findViewById<ImageView>(R.id.qrImageView)
        val btnSudahBayar = dialogView.findViewById<Button>(R.id.btnSudahBayar)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)
        val tvInfo = dialogView.findViewById<TextView>(R.id.tvInfo)
        val tvCountdown = dialogView.findViewById<TextView>(R.id.tvCountdown)

        // Gunakan warna biru muda dari colors.xml (primary)
        val primaryColor = ContextCompat.getColor(this, R.color.primary)
        tvInfo.setTextColor(primaryColor)
        tvCountdown.setTextColor(primaryColor)

        tvInfo.text = "Total: Rp $totalBiaya"

        // Generate QR
        try {
            val qrString = "TRX|QRIS|${System.currentTimeMillis()}|$totalBiaya"
            val size = 500
            val bits = com.google.zxing.qrcode.QRCodeWriter().encode(
                qrString,
                com.google.zxing.BarcodeFormat.QR_CODE,
                size,
                size
            )
            val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bmp.setPixel(x, y, if (bits[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            qrImageView.setImageBitmap(bmp)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal generate QR", Toast.LENGTH_SHORT).show()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Timer 5 menit
        val countdownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                tvCountdown.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@PaymentPengirimanActivity, "QR telah expired!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.start()

        btnSudahBayar.setOnClickListener {
            btnSudahBayar.isEnabled = false
            kirimDataPembayaran("QRIS")
            countdownTimer.cancel()
            dialog.dismiss()
        }

        btnBatal.setOnClickListener {
            countdownTimer.cancel()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showTransferDialog(bankName: String, rekening: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_transfer_bank4, null)
        val tvBank = dialogView.findViewById<TextView>(R.id.tvBank)
        val tvRekening = dialogView.findViewById<TextView>(R.id.tvRekening)
        val tvTotal = dialogView.findViewById<TextView>(R.id.tvTotalTransfer)
        val tvCountdown = dialogView.findViewById<TextView>(R.id.tvCountdownBank)
        val btnSelesai = dialogView.findViewById<Button>(R.id.btnTransferSelesai)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)

        tvBank.text = "Metode Pembayaran: $bankName"
        tvRekening.text = "No Rekening: $rekening"
        tvTotal.text = "Jumlah Transfer: Rp$totalBiaya"

        // Samakan warna countdown dengan primary
        val primaryColor = ContextCompat.getColor(this, R.color.primary)
        tvCountdown.setTextColor(primaryColor)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Timer 5 menit
        val countdownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                tvCountdown.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(this@PaymentPengirimanActivity, "Waktu transfer habis!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }.start()

        btnSelesai.setOnClickListener {
            kirimDataPembayaran(bankName)
            countdownTimer.cancel()
            dialog.dismiss()
            Toast.makeText(this, "Silakan transfer dan konfirmasi ke admin.", Toast.LENGTH_SHORT).show()
        }

        btnBatal.setOnClickListener {
            countdownTimer.cancel()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun kirimDataPembayaran(metode: String) {
        val url = "http://192.168.18.4/php_api_login/api/insert_pengiriman.php"
        val request = object : StringRequest(Method.POST, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getBoolean("success")) {
                        Toast.makeText(this, "Pembayaran berhasil via $metode!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal: ${json.getString("message")}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            {
                Toast.makeText(this, "Gagal koneksi: ${it.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "user_id" to userId,
                    "nama_pengirim" to intent.getStringExtra("NAMA_PENGIRIM").orEmpty(),
                    "no_hp" to intent.getStringExtra("NO_HP").orEmpty(),
                    "alamat_pengirim" to intent.getStringExtra("ALAMAT_PENGIRIM").orEmpty(),
                    "alamat_penerima" to intent.getStringExtra("ALAMAT_PENERIMA").orEmpty(),
                    "berat_barang" to intent.getStringExtra("BERAT_BARANG").orEmpty(),
                    "jenis_barang" to intent.getStringExtra("JENIS_BARANG").orEmpty(),
                    "total_biaya" to totalBiaya.toString(),
                    "metode" to metode
                )
            }
        }
        Volley.newRequestQueue(this).add(request)
    }
}

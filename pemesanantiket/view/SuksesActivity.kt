package com.donisw.pemesanantiket.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.databinding.ActivitySuksesBinding
import com.donisw.pemesanantiket.view.main.MainActivity

class SuksesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuksesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuksesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKembali.setOnClickListener {
            // Balik ke dashboard atau halaman utama
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

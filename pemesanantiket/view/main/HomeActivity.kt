package com.donisw.pemesanantiket.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.view.auth.LoginActivity
import com.donisw.pemesanantiket.view.auth.RegisterActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, com.donisw.pemesanantiket.view.main.MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_home)
        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        val btnMasuk = findViewById<Button>(R.id.btnMasuk)

        btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnMasuk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
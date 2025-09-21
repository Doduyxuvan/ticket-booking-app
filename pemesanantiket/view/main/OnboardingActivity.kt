package com.donisw.pemesanantiket.view.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.view.home.HomeActivity
import com.donisw.pemesanantiket.view.main.MainActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("onboarding_pref", MODE_PRIVATE)

        if (sharedPref.getBoolean("isFirstTime", false)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_onboarding)

        val btnContinue = findViewById<Button>(R.id.btnContinue)
        btnContinue.setOnClickListener {
            sharedPref.edit().putBoolean("isFirstTime", true).apply()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
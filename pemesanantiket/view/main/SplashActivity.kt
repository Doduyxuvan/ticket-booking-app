package com.donisw.pemesanantiket.view.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.view.home.HomeActivity
import com.donisw.pemesanantiket.view.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        sharedPreferences = getSharedPreferences("onboarding", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("firstRun", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isFirstRun) {
                startActivity(Intent(this, OnboardingActivity::class.java))

                val editor = sharedPreferences.edit()
                editor.putBoolean("firstRun", false)
                editor.apply()

            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            finish()
        }, 3000)
    }
}
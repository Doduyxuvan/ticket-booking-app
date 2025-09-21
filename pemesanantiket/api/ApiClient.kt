package com.donisw.pemesanantiket.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit

object ApiClient {

    // ✅ Base URL cukup sampai folder API kamu
    // Pastikan IP sesuai dengan IP server / laptop kamu
    private const val BASE_URL = "http://192.168.18.4/php_api_login/"

    // ✅ Logging biar keliatan request & response di Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ✅ Client dengan timeout yang lebih panjang
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // otomatis retry kalau gagal
            .build()
    }

    // ✅ Gson buat parsing JSON
    private val gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    // ✅ Retrofit Instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ✅ Dipanggil dari Activity/Fragment
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

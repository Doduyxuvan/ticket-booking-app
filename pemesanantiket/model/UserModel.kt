package com.donisw.pemesanantiket.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val id: Int,

    val nama: String,

    @SerializedName("no_hp")
    val noHp: String,

    val role: String,
    val username: String,
    val email: String
)

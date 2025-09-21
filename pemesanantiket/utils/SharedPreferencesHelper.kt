package com.donisw.pemesanantiket.utils

import android.content.Context
import android.content.SharedPreferences
import com.donisw.pemesanantiket.model.UserModel

class SharedPreferencesHelper(context: Context) {

    companion object {
        private const val PREF_NAME = "login_prefs"
        private const val KEY_ID = "user_id"
        private const val KEY_NAMA = "user_nama"
        private const val KEY_NO_HP = "user_no_hp"
        private const val KEY_ROLE = "user_role"
        private const val KEY_USERNAME = "user_username"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLogin(user: UserModel) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_ID, user.id ?: -1)
            putString(KEY_NAMA, user.nama)
            putString(KEY_NO_HP, user.noHp)
            putString(KEY_ROLE, user.role)
            putString(KEY_USERNAME, user.username)
            putString(KEY_EMAIL, user.email)
            apply()
        }
    }

    fun getUser(): UserModel {
        val id = prefs.getInt(KEY_ID, -1)
        val nama = prefs.getString(KEY_NAMA, "") ?: ""
        val noHp = prefs.getString(KEY_NO_HP, "") ?: ""
        val role = prefs.getString(KEY_ROLE, "") ?: ""
        val username = prefs.getString(KEY_USERNAME, "") ?: ""
        val email = prefs.getString(KEY_EMAIL, "") ?: ""

        return UserModel(id, nama, noHp, role, username, email)
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_ID, -1)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getUserId() != -1
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun clearUser() {
        logout() // untuk kompatibel dengan kode lama
    }
}

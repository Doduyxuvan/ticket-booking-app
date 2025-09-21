package com.donisw.pemesanantiket.database

import android.content.Context
import androidx.room.Room

class DatabaseClient private constructor(context: Context) {
    val appDatabase: AppDatabase

    companion object {
        @Volatile
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(context.applicationContext)
            }
            return mInstance!!
        }
    }

    init {
        appDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "travel_db"
        ).fallbackToDestructiveMigration().build()
    }
}
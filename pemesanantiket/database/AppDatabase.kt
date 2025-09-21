package com.donisw.pemesanantiket.database

import androidx.room.Database
import com.donisw.pemesanantiket.model.ModelDatabase
import androidx.room.RoomDatabase
import com.donisw.pemesanantiket.database.dao.DatabaseDao
import com.donisw.pemesanantiket.model.UserEntity

@Database(entities = [ModelDatabase::class, UserEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao?
}
package com.donisw.pemesanantiket.database.dao

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Insert
import com.donisw.pemesanantiket.model.ModelDatabase
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.donisw.pemesanantiket.model.UserEntity

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM tbl_travel")
    fun getAllData(): LiveData<List<ModelDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(vararg modelDatabases: ModelDatabase)

    @Query("DELETE FROM tbl_travel WHERE uid= :uid")
    fun deleteDataById(uid: Int)

    @Insert
    fun insertUser(user: UserEntity)

    @Query("SELECT * FROM tbl_user WHERE no_hp = :noHp AND password = :password LIMIT 1")
    fun loginUser(noHp: String, password: String): UserEntity?
}
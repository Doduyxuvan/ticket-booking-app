package com.donisw.pemesanantiket.model

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "tbl_travel")
class ModelDatabase : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid = 0

    @ColumnInfo(name = "nama_penumpang")
    lateinit var namaPenumpang: String

    @ColumnInfo(name = "keberangkatan")
    lateinit var keberangkatan: String

    @ColumnInfo(name = "tujuan")
    lateinit var tujuan: String

    @ColumnInfo(name = "tanggal")
    lateinit var tanggal: String

    @ColumnInfo(name = "nomor_telepon")
    lateinit var nomorTelepon: String

    // 🔥 Ganti anakAnak & dewasa dengan nomorBangku
    @ColumnInfo(name = "nomor_bangku")
    lateinit var nomorBangku: String

    @ColumnInfo(name = "harga_tiket")
    var hargaTiket = 0

    @ColumnInfo(name = "kelas")
    lateinit var kelas: String

    @ColumnInfo(name = "status")
    lateinit var status: String
}

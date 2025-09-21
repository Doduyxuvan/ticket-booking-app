package com.donisw.pemesanantiket.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.donisw.pemesanantiket.database.DatabaseClient.Companion.getInstance
import com.donisw.pemesanantiket.database.dao.DatabaseDao
import com.donisw.pemesanantiket.model.ModelDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    var dataList: LiveData<List<ModelDatabase>> = MutableLiveData(emptyList())
    var databaseDao: DatabaseDao? = null

    fun deleteDataById(uid: Int) {
        Completable.fromAction {
            databaseDao?.deleteDataById(uid)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    init {
        getInstance(application)?.let { dbClient ->
            databaseDao = dbClient.appDatabase?.databaseDao()
            dataList = databaseDao?.getAllData() ?: MutableLiveData(emptyList())
        }
    }
}

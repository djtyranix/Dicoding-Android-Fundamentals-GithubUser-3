package com.nixstudio.githubuser3.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nixstudio.githubuser3.db.AppDatabase
import com.nixstudio.githubuser3.model.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel : ViewModel() {
    val listFav = MutableLiveData<List<Favorite>>()
    var db: AppDatabase? = null

    fun createDatabase(context: Context) {
        if (db == null) {
            db = AppDatabase(context)
        }
    }

    fun setFavList() {
        viewModelScope.launch(Dispatchers.Default) {
            val list = db?.favoriteDao()?.getAll()

            withContext(Dispatchers.Main) {
                listFav.postValue(list!!)
            }
        }
    }

    fun getFavList(): LiveData<List<Favorite>> = listFav
}
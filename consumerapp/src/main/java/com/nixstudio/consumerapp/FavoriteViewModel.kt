package com.nixstudio.consumerapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel : ViewModel() {
    val listFav = MutableLiveData<ArrayList<UsersItem>>()

    fun setFavList(context: Context) {
        val cursor = context.contentResolver.query(
            DatabaseContract.FavoriteUserColumns.CONTENT_URI,
            null,
            null,
            null,
            null,
        )

        val list = MappingHelper.mapCursorToArrayList(cursor)

        listFav.postValue(list)
    }

    fun getFavList(): LiveData<ArrayList<UsersItem>> = listFav
}
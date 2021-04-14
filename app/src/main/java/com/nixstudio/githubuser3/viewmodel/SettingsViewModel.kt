package com.nixstudio.githubuser3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    val timeString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun setTimeString(time: String) {
        timeString.postValue(time)
    }

    fun getTimeString(): LiveData<String> = timeString
}
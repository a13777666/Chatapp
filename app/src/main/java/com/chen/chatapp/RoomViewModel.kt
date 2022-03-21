package com.chen.chatapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class RoomViewModel: ViewModel() {
    val message = MutableLiveData<String>()

    fun getmessage(single: String){
            message.postValue(single)
    }
}
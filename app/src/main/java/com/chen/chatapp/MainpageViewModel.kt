package com.chen.chatapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class MainpageViewModel: ViewModel() {
    var TAG = MainpageViewModel::class.java.simpleName
    val chatRooms = MutableLiveData<List<Lightyear>>()

    fun getAllRooms(){
        viewModelScope.launch(Dispatchers.IO){
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)
            chatRooms.postValue(response.result.lightyear_list)
        }
    }
}
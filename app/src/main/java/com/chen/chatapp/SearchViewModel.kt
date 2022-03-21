package com.chen.chatapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class SearchViewModel:ViewModel() {
    val chatRooms = MutableLiveData<List<Lightyear>>()

    fun getSearchRooms(searchtext: String){
        viewModelScope.launch(Dispatchers.IO){
            val chatrooms = mutableListOf<Lightyear>()
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)

            val searched = Gson().fromJson(json, Lightyear::class.java)
            if(searchtext in searched.nickname)
                chatRooms.postValue(response.result.lightyear_list)
        }
    }




}
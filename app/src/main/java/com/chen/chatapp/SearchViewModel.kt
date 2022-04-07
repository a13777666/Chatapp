package com.chen.chatapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class SearchViewModel:ViewModel() {
    val searchRooms = MutableLiveData<List<Lightyear>>()
    val hitRooms = MutableLiveData<List<Lightyear>>()

    fun getSearchRooms(searchtext: String, response: List<Lightyear>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
//            val response = Gson().fromJson(json, ChatRooms::class.java)
            val resultRooms = mutableListOf<Lightyear>()

            if (searchtext == "") {
                resultRooms.clear()
            }
            else {
                response.forEach {
                    if (searchtext in it.nickname) resultRooms.add(it)
                    else if (searchtext in it.stream_title) resultRooms.add(it)
                    else if (searchtext in it.tags) resultRooms.add(it)
                }
            }
            searchRooms.value = resultRooms
//        }
    }

    fun getHitRooms(){
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRooms::class.java)

            var resultRooms = response.result.lightyear_list.sortedByDescending {
                it.online_num
            }
            hitRooms.postValue(resultRooms)
        }
    }
}
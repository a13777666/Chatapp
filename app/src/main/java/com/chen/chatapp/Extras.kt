package com.chen.chatapp

import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData

class Extras {
    companion object{
        val LOGIN_NICKNAME = "Nickname"
        val LOGIN_PASSWORD = "Password"
        var AllRooms = mutableListOf<Lightyear>()
    }
}
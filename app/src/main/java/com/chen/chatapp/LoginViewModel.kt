package com.chen.chatapp

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel(): ViewModel() {
    enum class Login {
        INIT, LOGIN_SUCESS, LOGIN_FAILED, LOGIN_NOUSER
    }
    val login_state = MutableLiveData<Login>()
    //spdata

    fun checkSP(context: Context, username: String, password: String){
        val pref = context.getSharedPreferences("User", Context.MODE_PRIVATE)
        val db_user = pref.getString(username, "")

        login_state.value = if(db_user == "")  Login.LOGIN_NOUSER
                            else{
                                val db_password = pref.getString(db_user+Extras.LOGIN_PASSWORD, "")
                                if (db_password == password) Login.LOGIN_SUCESS
                                else Login.LOGIN_FAILED
                            }
    }
}
package com.chen.chatapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(): ViewModel() {
    enum class Login {
        INIT, LOGIN_SUCESS, LOGIN_FAILED, LOGIN_NOUSER
    }
    val login_state = MutableLiveData<Login>()

    fun checkSP(context: Context, username: String, password: String){
        val remstate = context.getSharedPreferences("Prestate", Context.MODE_PRIVATE)
        var db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("account", "$username")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    if(password == it["password"].toString()){
                        login_state.value = Login.LOGIN_SUCESS
                        Nowuser.Nickname = it["nickname"].toString()
                        Nowuser.User = it["account"].toString()
                        Nowuser.LOGIN_STATE = true
                        remstate.edit().putBoolean("pre-login_state", true).apply()
                        remstate.edit().putString("account", Nowuser.User).apply()
                        remstate.edit().putString("nickname", Nowuser.Nickname).apply()
                    }
                    else login_state.value = Login.LOGIN_FAILED
                }
            }
            .addOnFailureListener{
                login_state.value = Login.LOGIN_NOUSER
            }
        //sharepreference method
//        val pref = context.getSharedPreferences("User", Context.MODE_PRIVATE)
//        val db_user = pref.getString(username, "")
//        login_state.value = if(db_user == "")  Login.LOGIN_NOUSER
//                            else{
//                                val db_password = pref.getString(db_user+Extras.LOGIN_PASSWORD, "")
//                                if (db_password == password) Login.LOGIN_SUCESS
//                                else Login.LOGIN_FAILED
//                            }
    }
}
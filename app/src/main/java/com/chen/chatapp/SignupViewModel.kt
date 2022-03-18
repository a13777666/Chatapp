package com.chen.chatapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel(): ViewModel() {
    val TAG = SignupViewModel::class.java.simpleName
    enum class Signup {
        PASS, NICK_SAME, USER_SAME, USER_TOOLONG, USER_TOOSHORT, SYMBOL,
        PWD_TOOLONG, PWD_TOOSHORT
    }

    val signup = MutableLiveData<Signup>()

    fun check(nickname: String, user: String, password: String) {
        val check = Regex("^[a-zA-Z0-9]+$")
        signup.value = if (check.matches(user) || check.matches(password)) {
            if (user.length < 4) Signup.USER_TOOSHORT
            if (user.length > 20) Signup.USER_TOOLONG
            if (password.length < 6) Signup.PWD_TOOSHORT
            if (password.length > 12) Signup.PWD_TOOLONG
            else Signup.PASS
        } else Signup.SYMBOL
    }
}
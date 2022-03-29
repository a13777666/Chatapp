package com.chen.chatapp

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickSharedViewModel: ViewModel() {
    val avatar = MutableLiveData<Bitmap>()
}
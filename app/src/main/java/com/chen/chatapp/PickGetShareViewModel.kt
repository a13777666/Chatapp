package com.chen.chatapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class PickGetShareViewModel(): ViewModel(){
    val bitmap = MutableLiveData<Bitmap>()

    fun getavatar(username: String){
        val imageName = username + "avatar"
        val strorageRef = FirebaseStorage.getInstance().reference.child("images/$imageName")
        val localfile = File.createTempFile("tempImage", "jpg")
        strorageRef.getFile(localfile).addOnSuccessListener{
        bitmap.value = BitmapFactory.decodeFile(localfile.absolutePath)
        }
    }
}

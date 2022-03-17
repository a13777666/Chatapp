package com.chen.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chen.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    lateinit var binding: ActivityMainBinding
    val fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun initFragments() {
        //fragments.add(0, GuessFragment())
        //fragments.add(1, BmiFragment())
    }
}
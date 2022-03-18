package com.chen.chatapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val pref = this.getSharedPreferences("User", Context.MODE_PRIVATE)
        pref.edit()
            .putString(Extras.LOGIN_USERNAME, "UNHAPPY")
            .apply()



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_mainpage -> {
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.main_container, fragments[0])
                        commit()
                    }
                    true
                }
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.main_container, fragments[1])
                        commit()
                    }
                    true
                }
                R.id.action_personal -> {
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.main_container, fragments[2])
                        commit()
                    }
                    true
                }
                else -> true
            }
        }
    }


        fun initFragments() {
            fragments.add(0, MainpageFragment())
            fragments.add(1, SignupFragment())
            fragments.add(2, LoginFragment())

            supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, fragments[0])
                commit()
            }

    }
}
package com.chen.chatapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chen.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    val sharedViewModel by viewModels<PickGetShareViewModel>()
    lateinit var binding: ActivityMainBinding
    val fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        val prefrem = this.getSharedPreferences("Prestate", Context.MODE_PRIVATE)
        Nowuser.LOGIN_STATE = prefrem.getBoolean("pre-login_state", false)
        Log.d(TAG, "onCreate: ${prefrem.getBoolean("pre-login_state", false)}")
        if(Nowuser.LOGIN_STATE == true){
            Nowuser.User = prefrem.getString("account", "")!!
            Nowuser.Nickname = prefrem.getString("nickname", "")!!
            sharedViewModel.getavatar(Nowuser.User)
        }



        binding.bottomNavBar.onTabSelected = {  item ->
            when (item.id) {
                R.id.action_mainpage -> {
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.main_container, fragments[0])
                        commit()
                    }
                }
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.main_container, fragments[1])
                        commit()
                    }
                }
                R.id.action_personal -> {
                    if(Nowuser.LOGIN_STATE == true) {
                        supportFragmentManager.beginTransaction().run {
                            replace(R.id.main_container, fragments[5])
                            commit()
                        }
                    }else if(Nowuser.LOGIN_STATE == false){
                        supportFragmentManager.beginTransaction().run {
                            replace(R.id.main_container, fragments[2])
                            commit()
                        }
                    }
                }
            }
        }
    }


        fun initFragments() {
            fragments.add(0, MainpageFragment.instance)
            fragments.add(1, SearchFragment.instance)
            fragments.add(2, LoginFragment.instance)
            fragments.add(3, SignupFragment.instance)
            fragments.add(4, PickFragment.instance)
            fragments.add(5, PersonalFragment.instance)

            supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, fragments[0])
                commit()
                binding.bottomNavBar.selectTab(binding.bottomNavBar.tabs[0])
            }

    }
}
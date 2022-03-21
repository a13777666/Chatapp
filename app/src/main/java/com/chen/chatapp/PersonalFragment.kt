package com.chen.chatapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chen.chatapp.databinding.FragmentPersonalBinding

class PersonalFragment: Fragment() {
    lateinit var  binding: FragmentPersonalBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = requireActivity() as MainActivity

        binding.tvNickname.text = Nowuser.Nickname
        binding.tvUser.text = Nowuser.User

        binding.bLogout.setOnClickListener {
            Nowuser.LOGIN_STATE = 0
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[2])
                commit()
                Toast.makeText(requireContext(), "登出成功", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}
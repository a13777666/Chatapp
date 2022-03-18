package com.chen.chatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.chen.chatapp.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    lateinit var  binding: FragmentLoginBinding
    val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        var remember = false
//        val pref = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE)
//
//        binding.cbRemember.isChecked = pref.getBoolean("rem_username", false)
//
//
//        val user = pref.getString(Extras.LOGIN_USERNAME, "")
//        val password = pref.getString(Extras.LOGIN_PASSWORD,"")
//
//        if(user != "")
//            binding.edUser.setText(user)
//        if(password != "")
//            binding.edPassword.setText(password)
//
//
//        binding.cbRemember.setOnCheckedChangeListener(){compoundButton, checked ->
//            remember = checked
//            if(!checked){
//                pref.edit()
//                    .putString(Extras.LOGIN_USERNAME,"")
//                    .putString(Extras.LOGIN_PASSWORD,"")
//                    .apply()
//            }
//        }
//        binding.bLogin.setOnClickListener {
//            val username = binding.edUser.text.toString()
//            val password = binding.edPassword.text.toString()
//         //   viewModel.check(username, password)
//            }
//        viewModel.initSharePreference(requireContext())
//        viewModel.login_state.observe(viewLifecycleOwner)  { login_state ->
//                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT)
//                    .show()
//        }
//
//
    }

}

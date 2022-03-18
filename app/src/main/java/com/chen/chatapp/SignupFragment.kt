package com.chen.chatapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chen.chatapp.databinding.FragmentSignupBinding

class SignupFragment: Fragment() {
    lateinit var binding: FragmentSignupBinding
    val viewModel by viewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var nickname: String
        lateinit var user: String
        lateinit var password: String

        val pref = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE)
        //viewModel.check(username, password)
        binding.bSend.setOnClickListener {
            nickname = binding.edNickname.text.toString()
            user = binding.edUser.text.toString()
            password = binding.edPassword.text.toString()
            viewModel.check(nickname, user, password)
        }


        lateinit var message: String
        viewModel.signup.observe(viewLifecycleOwner) { state ->
            if(state == SignupViewModel.Signup.PASS) {
                pref.edit()
                    .putString(Extras.LOGIN_NICKNAME, nickname)
                    .putString(Extras.LOGIN_USERNAME, user)
                    .putString(Extras.LOGIN_PASSWORD, password)
                    .apply()
            }else{
                 message = when(state) {
                            SignupViewModel.Signup.USER_TOOLONG -> "帳號太長"
                            SignupViewModel.Signup.USER_TOOSHORT -> "帳號太短"
                            SignupViewModel.Signup.PWD_TOOLONG -> "密碼太長"
                            SignupViewModel.Signup.PWD_TOOSHORT -> "密碼太短"
                            SignupViewModel.Signup.SYMBOL -> "不能有特殊符號"
                            else -> "somthing goes wrong"
                }
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
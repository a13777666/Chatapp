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
    companion object {
        val TAG = LoginFragment::class.java.simpleName
        val instance: LoginFragment by lazy {
            LoginFragment()
        }
    }
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
        val parentActivity = requireActivity() as MainActivity
        var username = ""
        var password = ""
        var remember = false
        val prefrem = requireContext().getSharedPreferences("Rem", Context.MODE_PRIVATE)
        binding.cbRemember.isChecked = prefrem.getBoolean("rem_username", false)
        binding.cbRemember.setOnCheckedChangeListener() { compoundButton, checked ->
            remember = checked
            prefrem.edit().putBoolean("rem_username", remember).apply()
            if (!checked) {
                prefrem.edit().putString(username, "").apply()
            }
        }
        viewModel.login_state.value = LoginViewModel.Login.INIT

        val prefUser = prefrem.getString(username, "")
        if (prefUser != "") {
            binding.edUser.setText(prefUser)
        }
        binding.bLogin.setOnClickListener {
            username = binding.edUser.text.toString()
            password = binding.edPassword.text.toString()
            viewModel.checkSP(requireContext(), username, password)
        }

        binding.bSignin.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[3])
                commit()
            }
        }

        val pref = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE)
        viewModel.login_state.observe(viewLifecycleOwner) { login_state ->
            if (login_state == LoginViewModel.Login.LOGIN_SUCESS) {
                if (remember) {
                    prefrem.edit()
                        .putString(username, username)
                        .apply()
                }
                Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT)
                .show()
                Nowuser.User = pref.getString(username+Extras.LOGIN_PASSWORD, "")!!
                Nowuser.Nickname = pref.getString(username+Extras.LOGIN_NICKNAME, "")!!
                Nowuser.LOGIN_STATE = true


                parentActivity.supportFragmentManager.beginTransaction().run {
                    replace(R.id.main_container, parentActivity.fragments[0])
                    commit()
                }

            } else {
                val message = when (login_state) {
                    LoginViewModel.Login.LOGIN_NOUSER -> "您還未註冊"
                    LoginViewModel.Login.LOGIN_FAILED -> "密碼錯誤"
                    LoginViewModel.Login.INIT -> "您好  請輸入帳密"
                    else -> "somthing goes wrong"
                }

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

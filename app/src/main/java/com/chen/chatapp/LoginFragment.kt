package com.chen.chatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
    val sharedViewModel by activityViewModels<PickGetShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.edUser.text.clear()
        binding.edPassword.text.clear()
        val prefrem = requireContext().getSharedPreferences("Rem", Context.MODE_PRIVATE)
        val prefUser = prefrem.getString("USER", "")
        if (prefUser != "") {
            binding.edUser.setText(prefUser)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = requireActivity() as MainActivity
        var username = ""
        var password = ""
        var remember = false
        val prefrem = requireContext().getSharedPreferences("Rem", Context.MODE_PRIVATE)
        binding.cbRemember.isChecked = prefrem.getBoolean("rem_username", false)
        binding.cbRemember.setOnCheckedChangeListener() { compoundButton, nowcheck ->
            remember = nowcheck
            prefrem.edit().putBoolean("rem_username", remember).apply()
            if (!nowcheck) {
                prefrem.edit().putString("USER", "").apply()
            }
        }
        viewModel.login_state.value = LoginViewModel.Login.INIT

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
        viewModel.login_state.observe(viewLifecycleOwner) { login_state ->
            if (login_state == LoginViewModel.Login.LOGIN_SUCESS) {
                if (remember) {
                    prefrem.edit()
                        .putString("USER", username)
                        .apply()
                }
                sharedViewModel.getavatar(username)
                Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT)
                .show()

                //跳轉至首頁bar
                parentActivity.binding.bottomNavBar.selectTab(parentActivity.binding.bottomNavBar.tabs[0])
                //跳轉至首頁
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

package com.chen.chatapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chen.chatapp.databinding.FragmentSignupBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment: Fragment() {
    companion object {
        val TAG = SignupFragment::class.java.simpleName
        val instance : SignupFragment by lazy {
            SignupFragment()
        }
    }

    lateinit var binding: FragmentSignupBinding

    val shareviewModel by activityViewModels<PickSharedViewModel>()
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
        val parentActivity = requireActivity() as MainActivity
        lateinit var nickname: String
        lateinit var user: String
        lateinit var password: String

        //SharePreference method
//        val pref = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE)
//        val prefrem = requireContext().getSharedPreferences("Rem", Context.MODE_PRIVATE)

        binding.bSend.setOnClickListener {
            nickname = binding.edNickname.text.toString()
            user = binding.edUser.text.toString()
            password = binding.edPassword.text.toString()
            viewModel.check(nickname, user, password)
        }

        binding.bExit.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[2])
                commit()
            }
        }

        binding.bPick.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[4])
                commit()
            }
        }

        binding.ivAvatar.scaleType = ImageView.ScaleType.CENTER_CROP
        shareviewModel.avatar.observe(viewLifecycleOwner){ bitmap ->
            binding.ivAvatar.setImageBitmap(bitmap)
        }


        lateinit var message: String
        viewModel.signup.observe(viewLifecycleOwner) { state ->
            if(state == SignupViewModel.Signup.PASS) {
                //SharePreference method
//                pref.edit()
//                    .putString(user+Extras.LOGIN_NICKNAME, nickname)
//                    .putString(user, user)
//                    .putString(user+Extras.LOGIN_PASSWORD, password)
//                    .apply()
//                prefrem.edit()
//                    .putString(user, user)
//                    .putString(user+Extras.LOGIN_PASSWORD, password)
//                    .apply()
                //Use Firebase database
                val firedb = FirebaseFirestore.getInstance()
                val userdata: MutableMap<String, Any> = HashMap()
                userdata["nickname"] = nickname
                userdata["account"] = user
                userdata["password"] = password

                firedb.collection("users")
                    .add(userdata)
                    .addOnSuccessListener{ documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                    }
                    .addOnFailureListener{ error ->
                        Log.w(TAG, "Error adding document", error)
                    }

                Toast.makeText(requireContext(), "註冊成功", Toast.LENGTH_SHORT)
                    .show()
                parentActivity.supportFragmentManager.beginTransaction().run {
                    replace(R.id.main_container, parentActivity.fragments[2])
                    commit()
                }

            }else{
                 message = when(state) {
                            SignupViewModel.Signup.USER_TOOLONG -> "帳號太長"
                            SignupViewModel.Signup.USER_TOOSHORT -> "帳號太短"
                            SignupViewModel.Signup.PWD_TOOLONG -> "密碼太長"
                            SignupViewModel.Signup.PWD_TOOSHORT -> "密碼太短"
                            SignupViewModel.Signup.SYMBOL -> "不能有特殊符號"
                            else -> "somthing goes wrong"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
            }

        }

        val Uri = Nowuser.headshot.toUri()
        binding.ivAvatar.setImageURI(Uri)
    }
}
package com.chen.chatapp

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.chen.chatapp.databinding.FragmentPersonalBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import kotlin.collections.HashMap

class PersonalFragment: Fragment() {
    companion object {
        val TAG = PersonalFragment::class.java.simpleName
        val instance : PersonalFragment by lazy {
            PersonalFragment()
        }
    }
    val pickgetsharedViewModel by  activityViewModels<PickGetShareViewModel>()
    val pickshareviewModel by activityViewModels<PickSharedViewModel>()

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

        binding.edName.setText(Nowuser.Nickname)
        binding.tvUser.text = Nowuser.User

        binding.bLogout.setOnClickListener {
            val remstate = requireContext().getSharedPreferences("Prestate", Context.MODE_PRIVATE)
            Nowuser.LOGIN_STATE = false
            remstate.edit().putBoolean("pre-login_state", false).apply()
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[2])
                commit()

                Toast.makeText(requireContext(), "登出成功", Toast.LENGTH_SHORT)
                    .show()
            }

        }
            binding.edName.isFocusable = false
            binding.edName.isFocusableInTouchMode = false
            binding.bSendnick.visibility = View.GONE

            binding.edName.setOnClickListener {
                binding.bSendnick.visibility = View.VISIBLE
                binding.edName.isFocusable = true
                binding.edName.isFocusableInTouchMode = true
                binding.bSendnick.setOnClickListener {
                    binding.edName.isFocusable = false
                    binding.edName.isFocusableInTouchMode = false
                    val userdata: MutableMap<String, Any> = HashMap()
                    val newnickname = binding.edName.text.toString()
                    userdata["nickname"] = newnickname
                    val firedb = FirebaseFirestore.getInstance()
                    firedb.collection("users").document("${Nowuser.User}")
                        .update(userdata)
                    Nowuser.Nickname = newnickname
                    binding.bSendnick.visibility = View.GONE

                }

            }


            binding.bPersonpick.setOnClickListener {
//                parentActivity.supportFragmentManager.beginTransaction().run {
//                    replace(R.id.main_container, parentActivity.fragments[4])
//                 commit()
//                }

                val bundle = Bundle()
                bundle.putString("data","fromperson")
                val fragment = PickFragment()
                fragment.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.main_container,fragment)?.commit()

         }
            pickgetsharedViewModel.bitmap.observe(viewLifecycleOwner){ bitmap ->
                binding.ivAvatar.setImageBitmap(bitmap)
            }

            binding.bSendavatar.visibility = View.GONE
            pickshareviewModel.avatar.observe(viewLifecycleOwner) { bitmap ->
                binding.ivAvatar.setImageBitmap(bitmap)
                binding.bSendavatar.visibility = View.VISIBLE
                binding.bSendavatar.setOnClickListener {
                    //上傳頭像
                    val filename = "${Nowuser.User}"+"avatar"
                    val storageRef = FirebaseStorage.getInstance().getReference("images/$filename")
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    storageRef.putBytes(data)
                    Handler().postDelayed({
                        pickgetsharedViewModel.getavatar(Nowuser.User)
                    },1000)
                    binding.bSendavatar.visibility = View.GONE
                }
            }
    }
}
package com.chen.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.chen.chatapp.databinding.FragmentPickBinding

class PickFragment: Fragment() {

    lateinit var binding: FragmentPickBinding
    val selectPictureFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Nowuser.headshot = it.toString()
                //uri.toString()//content://ccc.ddd/sss/aaa
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentActivity = requireActivity() as MainActivity

        binding.bExit.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().run {
                replace(R.id.main_container, parentActivity.fragments[3])
                commit()
            }
        }

        binding.bAlbum.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        selectPictureFromGallery.launch("image/*")
    }
}
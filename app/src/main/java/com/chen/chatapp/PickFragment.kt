package com.chen.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chen.chatapp.databinding.FragmentPickBinding

class PickFragment: Fragment() {
    lateinit var binding: FragmentPickBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickBinding.inflate(layoutInflater)
        return super.onCreateView(inflater, container, savedInstanceState)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
package com.chen.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chen.chatapp.databinding.FragmentMainpageBinding
import com.chen.chatapp.databinding.RowChatroomBinding

class MainpageFragment: Fragment() {
    lateinit var binding: FragmentMainpageBinding
    private  lateinit var adapter: ChatRoomAdapter
    val rooms = mutableListOf<Lightyear>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainpageBinding.inflate(layoutInflater)
        return super.onCreateView(inflater, container, savedInstanceState)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set Recycler
        binding.roomRecycler.setHasFixedSize(true)
        binding.roomRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ChatRoomAdapter()
        binding.roomRecycler.adapter = adapter
    }

    inner class ChatRoomAdapter: RecyclerView.Adapter<ChatRoomViewHolder>(){
        val chatrooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return ChatRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val lightYear = chatrooms[position]
            holder.room_title.setText(lightYear.stream_title)
            holder.host_name.setText(lightYear.nickname)
            holder.online_num.setText(lightYear.online_num).toString()
            holder.tags.setText(lightYear.tags)
            //Glide processes photo
            Glide.with(this@MainpageFragment)
                .load(lightYear.head_photo)
                .into(holder.headshot)
            holder.itemView.setOnClickListener {
            }
        }

        override fun getItemCount(): Int {
            return  chatrooms.size
        }

    }

    inner class ChatRoomViewHolder(val binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root){
        val online_num = binding.tvOnlineNum
        val tags = binding.tvTags
        val host_name = binding.tvHostName
        val room_title = binding.tvRoomTitle
        val headshot = binding.imageView
    }

}
package com.chen.chatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chen.chatapp.databinding.FragmentMainpageBinding
import com.chen.chatapp.databinding.RowChatroomBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore





class MainpageFragment: Fragment() {
    companion object {
        val TAG = MainpageFragment::class.java.simpleName
        val instance: MainpageFragment by lazy {
            MainpageFragment()
        }
    }
    lateinit var binding: FragmentMainpageBinding
    private  lateinit var adapter: ChatRoomAdapter
    val viewModel by viewModels<MainpageViewModel>()
    val pickgetsharedViewModel by  activityViewModels<PickGetShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainpageBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var db = FirebaseFirestore.getInstance()
        val test = db.collection("users")
                .whereEqualTo("account", "123")
                .get()
        Log.d(TAG, "onViewCreated: $test")
        
        //set Recycler
        if(Nowuser.LOGIN_STATE == false){
            binding.ivAvatar.visibility = View.GONE
            binding.tvMainname.visibility = View.GONE
        }else{
            binding.tvMainname.text = Nowuser.Nickname
            binding.ivAvatar.visibility = View.VISIBLE
        }

        binding.roomRecycler.setHasFixedSize(true)
        binding.roomRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ChatRoomAdapter()
        binding.roomRecycler.adapter = adapter

        pickgetsharedViewModel.bitmap.observe(viewLifecycleOwner){ bitmap ->
            binding.ivAvatar.setImageBitmap(bitmap)
        }

        viewModel.chatRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
            Extras.AllRooms.clear()
            Extras.AllRooms.addAll(rooms)
        }
        viewModel.getAllRooms()

        binding.swDaynight.setOnCheckedChangeListener{ compounndButton, checked ->
            if(checked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    inner class ChatRoomAdapter: RecyclerView.Adapter<ChatRoomViewHolder>() {
        val chatrooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return ChatRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val lightYear = chatrooms[position]
            holder.room_title.setText(lightYear.stream_title)
            holder.host_name.setText(lightYear.nickname)
            holder.online_num.setText(lightYear.online_num.toString())
            holder.tags.setText(lightYear.tags)
            //Glide processes photo
            Glide.with(this@MainpageFragment)
                .load(lightYear.head_photo)
                .into(holder.headshot)
            holder.itemView.setOnClickListener {
                chatRoomClicked(lightYear)
            }
        }

        override fun getItemCount(): Int {
            return chatrooms.size
        }

        fun submitRooms(rooms: List<Lightyear>) {
            chatrooms.clear()
            chatrooms.addAll(rooms)
            notifyDataSetChanged()
        }
    }

    inner class ChatRoomViewHolder(val binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root){
        val online_num = binding.tvOnlineNum
        val tags = binding.tvTags
        val host_name = binding.tvHostName
        val room_title = binding.tvRoomTitle
        val headshot = binding.imageView
    }


    fun chatRoomClicked(lightyear : Lightyear) {
        val intent = Intent(requireContext(), RoomActivity::class.java)
        startActivity(intent)
    }
}
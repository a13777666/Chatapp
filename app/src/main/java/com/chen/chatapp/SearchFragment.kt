package com.chen.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chen.chatapp.databinding.FragmentSearchBinding
import com.chen.chatapp.databinding.RowChatroomBinding

class SearchFragment: Fragment() {
    lateinit var  binding: FragmentSearchBinding
    private  lateinit var adapter: SearchRoomAdapter
    val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecycler.setHasFixedSize(true)
        binding.searchRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchRoomAdapter()
        binding.searchRecycler.adapter = adapter

        viewModel.chatRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }
    }

    inner class SearchRoomAdapter: RecyclerView.Adapter<SearchRoomViewHolder>() {
        val chatrooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRoomViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return SearchRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchRoomViewHolder, position: Int) {
            val lightYear = chatrooms[position]

            holder.room_title.setText(lightYear.stream_title)
            holder.host_name.setText(lightYear.nickname)
            holder.online_num.setText(lightYear.online_num.toString())
            holder.tags.setText(lightYear.tags)
            //Glide processes photo
            Glide.with(this@SearchFragment)
                .load(lightYear.head_photo)
                .into(holder.headshot)
            holder.itemView.setOnClickListener {
                SearchRoomClicked(lightYear)
            }
        }

        override fun getItemCount(): Int {
            return chatrooms.size
        }

        fun submitRooms(rooms: List<Lightyear>) {
            chatrooms.addAll(rooms)
            notifyDataSetChanged()
        }
    }

    inner class SearchRoomViewHolder(val binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root){
        val online_num = binding.tvOnlineNum
        val tags = binding.tvTags
        val host_name = binding.tvHostName
        val room_title = binding.tvRoomTitle
        val headshot = binding.imageView
    }


    fun SearchRoomClicked(lightyear : Lightyear) {
        val intent = Intent(requireContext(), RoomActivity::class.java)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_roomlist, menu)
        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchtext = binding.svRoom.query.toString()
                viewModel.getSearchRooms(searchtext)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val searchtext = binding.svRoom.query.toString()
                viewModel.getSearchRooms(searchtext)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}
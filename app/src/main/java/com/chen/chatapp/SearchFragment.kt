package com.chen.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    companion object {
        val TAG = SearchFragment::class.java.simpleName
        val instance : SearchFragment by lazy {
            SearchFragment()
        }
    }

    lateinit var  binding: FragmentSearchBinding
    private  lateinit var searchadapter: SearchRoomAdapter
    private  lateinit var hitadapter: HitRoomAdapter

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
        //一開始搜尋結果為Gone
        //binding.tvSearchResult.visibility = View.GONE


        binding.searchRecycler.setHasFixedSize(true)
        binding.searchRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        searchadapter = SearchRoomAdapter()
        binding.searchRecycler.adapter = searchadapter

        binding.hitRecycler.setHasFixedSize(true)
        binding.hitRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        hitadapter = HitRoomAdapter()
        binding.hitRecycler.adapter = hitadapter




        viewModel.searchRooms.observe(viewLifecycleOwner) { searchrooms ->
            searchadapter.submitRooms(searchrooms)
        }
        binding.svRoom.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchtext = binding.svRoom.query.toString()
                viewModel.getSearchRooms(searchtext, Extras.AllRooms)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val searchtext = binding.svRoom.query.toString()
                viewModel.getSearchRooms(searchtext, Extras.AllRooms)
                return false
            }
        })


        viewModel.hitRooms.observe(viewLifecycleOwner) { hitrooms ->
            hitadapter.submitRooms(hitrooms)
        }
        viewModel.getHitRooms()
    }
    //search room
    inner class SearchRoomAdapter: RecyclerView.Adapter<SearchRoomViewHolder>() {
        val searchchatrooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRoomViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return SearchRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchRoomViewHolder, position: Int) {
            val lightYear = searchchatrooms[position]

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
            return searchchatrooms.size
        }

        fun submitRooms(rooms: List<Lightyear>) {
            searchchatrooms.clear()
            searchchatrooms.addAll(rooms)
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
    //hitrooms recycler
    inner class HitRoomAdapter: RecyclerView.Adapter<HitRoomViewHolder>() {
        val hitchatrooms = mutableListOf<Lightyear>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitRoomViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return HitRoomViewHolder(binding)
        }

        override fun onBindViewHolder(holder: HitRoomViewHolder, position: Int) {
            val lightYear = hitchatrooms[position]

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
            return hitchatrooms.size
        }

        fun submitRooms(rooms: List<Lightyear>) {
            hitchatrooms.clear()
            hitchatrooms.addAll(rooms)
            notifyDataSetChanged()
        }
    }

    inner class HitRoomViewHolder(val binding: RowChatroomBinding): RecyclerView.ViewHolder(binding.root){
        val online_num = binding.tvOnlineNum
        val tags = binding.tvTags
        val host_name = binding.tvHostName
        val room_title = binding.tvRoomTitle
        val headshot = binding.imageView
    }

    //room click
    fun SearchRoomClicked(lightyear : Lightyear) {
        val intent = Intent(requireContext(), RoomActivity::class.java)
        startActivity(intent)
    }
}
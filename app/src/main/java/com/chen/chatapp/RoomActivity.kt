package com.chen.chatapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chen.chatapp.databinding.ActivityRoomBinding
import com.chen.chatapp.databinding.RowChatroomBinding
import com.chen.chatapp.databinding.RowMessageBinding
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class RoomActivity : AppCompatActivity() {
    val TAG = RoomActivity::class.java.simpleName

    lateinit var binding: ActivityRoomBinding
    private  lateinit var adapter: RoomActivity.ChatMessageAdapter
    val viewModel by viewModels<RoomViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lateinit var websocket: WebSocket
        //websocket
        val client = OkHttpClient.Builder()
            .readTimeout(3,TimeUnit.SECONDS)
            .build()

        lateinit var nickname : String
        if(Nowuser.LOGIN_STATE == 1){
            nickname = Nowuser.Nickname
        }else   nickname = "訪客"

        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$nickname")
            .build()
        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                if("default_message" in text) {
                    val response = Gson().fromJson(text, receive::class.java)
                    val singlemessage = "${response.body.nickname} : ${response.body.text}"
                    viewModel.getmessage(singlemessage)
                }
            }
        })


        binding.bSend.setOnClickListener {
            val message = binding.edChat.text.toString()
            websocket.send(Gson().toJson(send("N", message)))
        }

        binding.vvRoom.setVideoPath("android.resource://"+packageName+"/"+R.raw.hime3)
        binding.vvRoom.setOnPreparedListener {
            binding.vvRoom.start()
        }

        binding.bExit.setOnClickListener {
            val inflater = this.layoutInflater
            AlertDialog.Builder(this)
                .setTitle("離開聊天室")
                .setView(inflater.inflate(R.layout.dialog_roomexit, null))
                .setPositiveButton("確定"){ d, w->
                    this.finish()
                }
                .setNegativeButton("取消", null)
                .show()
        }



        binding.chatRecycler.setHasFixedSize(true)
        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lm.stackFromEnd = true
        binding.chatRecycler.layoutManager = lm
        adapter = ChatMessageAdapter()
        binding.chatRecycler.adapter = adapter



        viewModel.message.observe(this) { message ->
            adapter.submitmessage(message)
        }
    }


    inner class ChatMessageAdapter: RecyclerView.Adapter<MessageViewHolder>() {
        val Message = mutableListOf<String>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val binding = RowMessageBinding.inflate(layoutInflater, parent, false)
            return MessageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val sendmessage = Message[position]
            holder.message.setText(sendmessage)
        }

        override fun getItemCount(): Int {
            return Message.size
        }

        fun submitmessage(message: String) {
            Message.add(message)
            notifyDataSetChanged()
        }
    }

    inner class MessageViewHolder(val binding: RowMessageBinding): RecyclerView.ViewHolder(binding.root){
        val message = binding.tvMessage
    }
}
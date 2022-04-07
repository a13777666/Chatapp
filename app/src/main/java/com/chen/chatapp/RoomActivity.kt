package com.chen.chatapp

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.MediaController
import android.widget.Toast
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
import java.util.*
import java.util.concurrent.TimeUnit
import android.app.Activity






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
        if(Nowuser.LOGIN_STATE == true){
            nickname = Nowuser.Nickname
        }else   nickname = "訪客"

        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$nickname")
            .build()
        websocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                lateinit var singlemessage :String

                if("default_message" in text) {
                    val response = Gson().fromJson(text, receive::class.java)
                    singlemessage = "${response.body.nickname} : ${response.body.text}"
                    viewModel.getmessage(singlemessage)
                }
                else if("sys_updateRoomStatus" in text){
                    val response = Gson().fromJson(text, updateRoomStatus::class.java)
                    val action = response.body.entry_notice.action
                    singlemessage =
                        when(action){
                            "enter" -> "歡迎"+ "${response.body.entry_notice.username}" + "進入聊天室"
                            "leave" -> "${response.body.entry_notice.username}"+"離開聊天室"
                            else ->""
                        }
                    viewModel.getmessage(singlemessage)
                }
                else if("admin_all_broadcast" in text){
                    val response = Gson().fromJson(text, admin_all_broadcast::class.java)
                    val country = Locale.getDefault().country
                    singlemessage =
                        when(country){
                            "TW" -> "廣播 : ${response.body.content.tw}"
                            "US" -> "broadcast : ${response.body.content.en}"
                            else -> "广播 : ${response.body.content.cn}"
                        }
                    viewModel.getmessage(singlemessage)
                }
                else if("sys_member_notice" in text){
                    val response = Gson().fromJson(text, memberNotice::class.java)
                    runOnUiThread { Toast.makeText(this@RoomActivity, "請輸入內容" , Toast.LENGTH_SHORT).show() }
                }
            }
        })

        binding.bSend.setOnClickListener {
            val message = binding.edChat.text.toString()
            websocket.send(Gson().toJson(send("N", message)))
            binding.edChat.text.clear()
        }

//        binding.vvRoom.setVideoPath("android.resource://"+packageName+"/"+R.raw.hime3)
//        binding.vvRoom.setOnPreparedListener {
//            binding.vvRoom.start()
//        }

        //媒體控制器
        //val mediaController = MediaController(this)
        //mediaController.setAnchorView(binding.vvRoom)
        //binding.vvRoom.setMediaController(mediaController)

        val uri = "https://firebasestorage.googleapis.com/v0/b/benching-chatapp.appspot.com/o/video%2FAsiaGodTon.mp4?alt=media&token=142004ac-43a0-4d59-8bab-cac119ffa17f"
        binding.vvRoom.setVideoURI(Uri.parse(uri))
        binding.vvRoom.setOnPreparedListener(){
            it.isLooping = true
        }
        binding.vvRoom.start()

        binding.bExit.setOnClickListener {
            val inflater = this.layoutInflater
            AlertDialog.Builder(this)
                .setTitle("離開聊天室")
                .setView(inflater.inflate(R.layout.dialog_roomexit, null))
                .setPositiveButton("確定"){ d, w->
                    websocket.close(1000, "正常關閉")
                    this.finish()
                }
                .setNegativeButton("取消", null)
                .show()
        }


        adapter = ChatMessageAdapter()
        binding.chatRecycler.setHasFixedSize(true)
        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lm.stackFromEnd = true
        binding.chatRecycler.layoutManager = lm
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
            holder.message.animation = AnimationUtils.loadAnimation(holder.message.context, R.anim.scale)
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
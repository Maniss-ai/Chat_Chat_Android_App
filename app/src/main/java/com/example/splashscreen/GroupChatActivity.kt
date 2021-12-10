package com.example.splashscreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import com.example.splashscreen.adapters.ChatAdapter
import com.example.splashscreen.adapters.GroupChatAdapter
import com.example.splashscreen.databinding.ActivityGroupChatBinding
import com.example.splashscreen.models.MessageModels
import com.example.splashscreen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var senderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        hide default app bar ....
        supportActionBar?.hide()

//        when pressing back arrow ....
        binding.arrowImageView.setOnClickListener {
            onBackPressed()
        }

//        GET SENDER NAME FROM DATABASE ....
//        TODO()
//        val senderName = intent.getStringExtra("senderName")

//        get instance of firebase database ....
        database = FirebaseDatabase.getInstance()

//        get sender id from firebase ....
        senderId = FirebaseAuth.getInstance().uid.toString()
        binding.usernameTextView.text = "Friends Group"

//        set this chat adapter to recycler view .....
//        1. create instance of chatAdapter ....
        val messageList: ArrayList<MessageModels> = ArrayList()
        chatAdapter = ChatAdapter(messageList, this)
//        2. set this chatAdapter to recyclerView ....
        binding.chatRecyclerView.adapter = chatAdapter


//        set linear layout manager to recycler view ....
        val linearLayoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

//       **********************
//       GET DATA FROM DATABASE ....
        database.reference
            .child("Group Chat")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    Remove Duplicates Snapshot(Previous Snapshots) .....
//                    clear the previous list before showing it .....
                    messageList.clear()
                    for (snapShot in dataSnapshot.children) {
                        val singleMessageModel = snapShot.getValue(MessageModels::class.java)
                        if (singleMessageModel != null) {
                            messageList.add(singleMessageModel)
                        }
                    }

//                    show message at the same time, send button clicked ....
//                    To update recycler view at runtime ....
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })

//       **********************
//        SET DATA TO DATABASE .....
//        set on click listener to send button ....
        binding.sendImageView.setOnClickListener {
            val message = binding.enterMessageEditText.text.toString()
            val messageModel = MessageModels(senderId, message)
            messageModel.timeStamp = Date().time

//            empty the message box....
            binding.enterMessageEditText.setText("")

//            set this data to firebase database ....
            database.reference
                .child("Group Chat")
                .push()
                .setValue(messageModel)
                .addOnSuccessListener {

                }
        }
    }
}
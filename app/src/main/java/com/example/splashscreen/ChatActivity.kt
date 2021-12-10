package com.example.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splashscreen.adapters.ChatAdapter
import com.example.splashscreen.databinding.ActivityChatBinding
import com.example.splashscreen.models.MessageModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var backArrowImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        create instance of firebase database and auth ....
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

//        Hooks .....
        backArrowImageView = binding.arrowImageView

//        get sender id and receiver id and extra information from userAdapter ....
        val senderId = auth.uid
        val receiverId = intent.getStringExtra("userId")
        val username = intent.getStringExtra("username")
        val profilePicture = intent.getStringExtra("profilePicture")

//        set these extra information to views of this activity .....
        binding.usernameTextView.text = username

//        load profile picture from database using picasso ....
        Picasso.get().load(profilePicture).placeholder(R.drawable.logo).into(binding.profileImageView)
        binding.usernameTextView.text = username

//        remove actions bar ....
        supportActionBar?.hide()

//        Set on Click listener on backArrowImageView ....
        backArrowImageView.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }


//        Connecting chatActivity recyclerView with chatAdapter ....
        val messageList : ArrayList<MessageModels> = ArrayList()

        val chatAdapter: ChatAdapter = ChatAdapter(messageList, this)
        binding.chatRecyclerView.adapter = chatAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId

//        get data from database ....
        database.reference
            .child("Chats")
            .child(senderRoom)
            .addValueEventListener(object: ValueEventListener {

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

//        Set on click listener on SEND button ....
        binding.sendImageView.setOnClickListener {

//            get message from messageBox edit text ....
            val message = binding.enterMessageEditText.text.toString()

//            create object of messageModel with message and timeStamp ....
            val messageModel = MessageModels(senderId.toString(), message)
            messageModel.timeStamp = Date().time

//            empty the messageBox edit text ....
            binding.enterMessageEditText.setText("")

//            create a node of CHATS in database ....
            database.reference
                .child("Chats")
                .child(senderRoom)
                .push()
                .setValue(messageModel)
                .addOnSuccessListener {
                    database.reference
                        .child("Chats")
                        .child(receiverRoom)
                        .push()
                        .setValue(messageModel)
                        .addOnSuccessListener {

                        }
            }

        }



    }
}
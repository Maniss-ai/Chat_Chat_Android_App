package com.example.splashscreen.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.example.splashscreen.ChatActivity
import com.example.splashscreen.R
import com.example.splashscreen.models.MessageModels
import com.example.splashscreen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UsersAdapter(private val mList: List<User>, private val context: Context?): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)
        val username = view.findViewById<TextView>(R.id.usernameTextView)
        val lastMessage = view.findViewById<TextView>(R.id.lastMessageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.user_sample_chat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = mList[position]

//        download profile picture from database and set to profileImageView ....
        Picasso.get().load(user.profile).placeholder(R.drawable.logo).into(holder.profileImageView)
        holder.username.text = user.username

//        GET LAST MESSAGE FROM DATABASE ....
        FirebaseDatabase.getInstance().reference
            .child("Chats")
            .child(FirebaseAuth.getInstance().uid + user.userId)
            .orderByChild("timeStamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.hasChildren()) {
                        for(snapShot in dataSnapshot.children) {
                            holder.lastMessage.text = snapShot.child("message").value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })

//        go to chat activity with extra information ....
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("profilePicture", user.profile)
            intent.putExtra("username", user.username)
            context?.startActivity(intent)
        }

//        last message .....

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
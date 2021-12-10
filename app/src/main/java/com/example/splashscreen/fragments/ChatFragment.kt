package com.example.splashscreen.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splashscreen.adapters.UsersAdapter
import com.example.splashscreen.databinding.FragmentChatBinding
import com.example.splashscreen.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.DividerItemDecoration

import android.R.string.no
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var arrayList: ArrayList<User>
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

//        create an instance of firebaseDatabase ....
        database = FirebaseDatabase.getInstance()

//        create arrayList ....
        arrayList = ArrayList()

//        create an object for UserAdapter ....
        val userAdapter = UsersAdapter(arrayList, context)

        binding.chatRecyclerView.adapter = userAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

//        add a divider line ....
        binding.chatRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.chatRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )


        //        create a listener ....
        val listener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                arrayList.clear()
                for (snapShot in dataSnapshot.children) {
                    val user = snapShot.getValue(User::class.java)
                    user?.userId = snapShot.key.toString()
                    if (user != null && user.userId != FirebaseAuth.getInstance().uid) {
                        arrayList.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

//        get values from database ....
        database.reference.child("User").addValueEventListener(listener)

        return binding.root
    }


}
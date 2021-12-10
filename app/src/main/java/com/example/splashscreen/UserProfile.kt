package com.example.splashscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import com.example.splashscreen.databinding.ActivityUserProfileBinding
import com.example.splashscreen.models.MessageModels
import com.example.splashscreen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class UserProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var profileImageView: ImageView
    private lateinit var updateButton: Button
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        hide action bar ....
        supportActionBar?.hide()

//        back arrow pressed ....
        binding.backArrowImageView.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }


//        create instances of Firebase Auth, Database, Storage ....
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

//        set on click listener to saveButton to save new name and status to firebase database ....
        binding.saveButton.setOnClickListener {
            val status = binding.statusEditText.text.toString()
            val username = binding.usernameEditText.text.toString()

            val hashMap = HashMap<String, Any>()
            hashMap["username"] = username
            hashMap["status"] = status

            database.reference.child("User").child(auth.uid.toString()).updateChildren(hashMap as Map<String, Any>)

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

        }

//        get profile picture from database and set into profileImageView ....
//        get username and about from database and set into profileImageView ....
        database.reference.child("User")
            .child(auth.uid.toString())
            .addValueEventListener(object: ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    Picasso.get().load(user?.profile)
                        .placeholder(R.drawable.user)
                        .into(binding.profileImageView)

                    binding.statusEditText.setText(user?.status)
                    binding.usernameEditText.setText(user?.username)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("TAG", "Failed to read value.", error.toException())
                }

            })

//        add on click listener to plus button ....
        binding.plusButton.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
                .type = "image/*"

            startActivityForResult(intent, 69)
        }
    }

//    Result of PlusButton Click ....
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//    check if user selected some picture or not ....
        if(data?.data != null) {
            val file: Uri = data.data!!
//            set image file to profilePictureImageView ....
            binding.profileImageView.setImageURI(file)

//            create a reference of Firebase Storage ....
            val storageReference = storage.reference.child("profile_pictures")
                .child(auth.uid.toString())

//            upload file image to firebase storage
            storageReference.putFile(file).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {uri->
                    database.reference.child("User")
                        .child(auth.uid.toString())
                        .child("profile")
                        .setValue(uri.toString())

                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
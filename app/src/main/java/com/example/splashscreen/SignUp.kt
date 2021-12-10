package com.example.splashscreen

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splashscreen.databinding.ActivitySignUpBinding
import com.example.splashscreen.models.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fullNameId: TextInputLayout
    private lateinit var usernameId: TextInputLayout
    private lateinit var emailId: TextInputLayout
    private lateinit var phoneNoId: TextInputLayout
    private lateinit var passwordId: TextInputLayout
    private lateinit var goButtonId: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
//    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.haveAccountButton.setOnClickListener {
            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

//        remove actions bar ....
        supportActionBar?.hide()

//        remove status bar ....
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        create objects of firebase auth and firebase database ....
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()


        fullNameId = binding.nameEditTextLayout
        usernameId = binding.usernameEditTextLayout
        emailId = binding.emailEditTextLayout
        phoneNoId = binding.phoneEditTextLayout
        passwordId = binding.passwordEditTextLayout
        goButtonId = binding.goButton

        //        create progressDialog ...
//        progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Creating Account")
//        progressDialog.setMessage("We're creating your account")


        binding.goButton.setOnClickListener {
//            VALIDATE ALL FIELDS ....
            if (!validateName() or
                !validateUsername() or
                !validateEmail() or
                !validatePhoneNo() or
                !validatePassword()
            ) {
                Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show()
            }

            else {
                val fullName = fullNameId.editText?.text.toString()
                val username = usernameId.editText?.text.toString()
                val email = emailId.editText?.text.toString()
                val phoneNo = phoneNoId.editText?.text.toString()
                val password = passwordId.editText?.text.toString()

//                add to firebase
//                add to database
//                progressDialog.show()
                binding.progressBar.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener { task ->

//                  dismiss progressDialog ...
//                    progressDialog.dismiss()
                    binding.progressBar.visibility = View.GONE
//                  if user created successfully ....
                    if (task.isSuccessful) {
//                      go to mainActivity ...
//                            val intent: Intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)

//                    after sign up go to dashboard activity ...
                        val intent: Intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)

//                      create an object of user class ...
                        val user = User(fullName, username, password, email, phoneNo)

//                      getId of user ...
                        val id: String = task.result?.user?.uid.toString()

                        database.reference.child("User").child(id).setValue(user)
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG)
                            .show()

                    }
//                if task failed ...
                    else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    //    VALIDATION FUNCTIONS ....
    private fun validateName(): Boolean {
        val name = fullNameId.editText?.text.toString()

        return if (name.isEmpty()) {
            fullNameId.error = "Field cannot be empty"
            false
        } else {
            fullNameId.error = null
            true
        }
    }

    private fun validateUsername(): Boolean {
        val value = usernameId.editText?.text.toString()
        val whiteSpaces = "(?=\\s+$)"

        return if (value.isEmpty()) {
            usernameId.error = "Field cannot be empty"
            false
        } else if (value.matches(whiteSpaces.toRegex())) {
            usernameId.error = "White spaces are not allowed"
            false
        } else if (value.length <= 3) {
            usernameId.error = "Username too short"
            false
        } else if (value.length >= 15) {
            usernameId.error = "Username too long"
            false
        } else {
            usernameId.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val value = emailId.editText?.text.toString()
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        return if (value.isEmpty()) {
            emailId.error = "Field cannot be empty"
            false
        } else if (!value.matches(pattern.toRegex())) {
            emailId.error = "Invalid email address"
            false
        } else {
            emailId.error = null
            true
        }
    }

    private fun validatePhoneNo(): Boolean {
        val value = phoneNoId.editText?.text.toString()
        val whiteSpaces = "(?=\\s+$)"

        return if (value.isEmpty()) {
            phoneNoId.error = "Field cannot be empty"
            false
        } else {
            phoneNoId.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value = passwordId.editText?.text.toString()
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{4,}\$\n"

//        How It Works? ....
//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
//        (?=\\S+$)          # no whitespace allowed in the entire string
//        .{4,}             # anything, at least six places though
//        $                 # end-of-string

        return if (value.isEmpty()) {
            passwordId.error = "Field cannot be empty"
            false
        } else if (value.matches(passwordPattern.toRegex())) {
            passwordId.error = "Password is too weak"
            false
        } else {
            passwordId.error = null
            true
        }
    }

}


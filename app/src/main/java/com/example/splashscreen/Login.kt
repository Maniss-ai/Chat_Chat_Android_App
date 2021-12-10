package com.example.splashscreen

import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splashscreen.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.github.ybq.android.spinkit.style.DoubleBounce

import com.github.ybq.android.spinkit.sprite.Sprite

import android.R

import android.widget.ProgressBar


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var logoImageView: ImageView
    private lateinit var logoTextView: TextView
    private lateinit var signInTextView: TextView
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var newAccountButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
//    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

//        remove actions bar ....
        supportActionBar?.hide()

//        remove status bar ....
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(binding.root)

//        Hooks ....
        logoImageView = binding.logoImageView
        logoTextView = binding.logoTextView
        signInTextView = binding.signInTextView
        username = binding.usernameEditTextLayout
        password = binding.passwordEditTextLayout
        loginButton = binding.goButton
        newAccountButton = binding.newAccountButton


//        Create new account ....
        newAccountButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)

//            create pairs ....
            val pair1 = Pair<View, String>(logoImageView, "logo_image")
            val pair2 = Pair<View, String>(logoTextView, "logo_text")
            val pair3 = Pair<View, String>(signInTextView, "sign_up_in")
            val pair4 = Pair<View, String>(username, "username")
            val pair5 = Pair<View, String>(password, "password")
            val pair6 = Pair<View, String>(loginButton, "go_button")
            val pair7 = Pair<View, String>(newAccountButton, "account")

            val pairs = ArrayList<Pair<View, String>>()
            pairs.add(pair1)
            pairs.add(pair2)
            pairs.add(pair3)
            pairs.add(pair4)
            pairs.add(pair5)
            pairs.add(pair6)
            pairs.add(pair7)

            val options: ActivityOptions =
                ActivityOptions.makeSceneTransitionAnimation(this, *pairs.toTypedArray())

            startActivity(intent, options.toBundle())
        }

        //        create progressDialog ...
//        progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Login")
//        progressDialog.setMessage("Login to your account")


//        create firebaseAuth object ...
        auth = FirebaseAuth.getInstance()
//        create database object ...
        database = FirebaseDatabase.getInstance()

//        set click listener on Sign In button ...
        binding.goButton.setOnClickListener {

            if (binding.usernameEditTextLayout.editText?.text.toString() == "" || binding.passwordEditTextLayout.editText?.text.toString() == "") {
                Toast.makeText(this, "Please enter Username or Password", Toast.LENGTH_SHORT)
                    .show()
            } else {
//            show progressDialog
//                progressDialog.show()
                binding.progressBar.visibility = View.VISIBLE

//            login using email and password
                auth.signInWithEmailAndPassword(binding.usernameEditTextLayout.editText?.text.toString(), binding.passwordEditTextLayout.editText?.text.toString()).addOnCompleteListener { task ->
//                    progressDialog.dismiss()
                    binding.progressBar.visibility = View.GONE

                    if (task.isSuccessful) {
//                    after sign in go to main activity ...
                        val intent: Intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    //    back when two times back pressed continuously ....
    var backPressedTime: Long = 0
    lateinit var backToast: Toast
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
//          go to home ...
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)

            super.onBackPressed()
            return
        } else {
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

//        create transition when go back to login activity ....

}
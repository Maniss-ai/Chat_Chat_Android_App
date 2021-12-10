package com.example.splashscreen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.splashscreen.databinding.ActivityMainBinding
import android.util.Pair
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

//    Variables ....
    private var SPLACH_SCREEN: Long = 3000

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var logoTextView: TextView
    private lateinit var sloganTextView: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

//        remove actions bar ....
        supportActionBar?.hide()

//        remove status bar ....
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

//        Add animations ....
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

//        create auth object ....
        auth = FirebaseAuth.getInstance()


//        Hooks ....
        imageView = binding.imageView
        logoTextView = binding.logoTextView
        sloganTextView = binding.sloganTextView


//        Set Animations on images and text ....
        imageView.animation = topAnim
        logoTextView.animation = bottomAnim
        sloganTextView.animation = bottomAnim


//        if any user is already login then show mainActivity ...
        if(auth.currentUser != null) {
            //        Delay ....
            Handler().postDelayed(Runnable {
                val intent: Intent = Intent(this, Dashboard::class.java)

//                val pair1 = Pair<View, String>(imageView, "logo_image")
//                val pair2 = Pair<View, String>(logoTextView, "logo_text")
//
//                val pairs = ArrayList<Pair<View, String>>()
//
//                pairs.add(pair1)
//                pairs.add(pair2)
//
//                val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, *pairs.toTypedArray())
//
//                startActivity(intent, options.toBundle())
                startActivity(intent)
                finish()
            }, SPLACH_SCREEN)
        } else {
            //        Delay ....
            Handler().postDelayed(Runnable {
                val intent: Intent = Intent(this, Login::class.java)

                val pair1 = Pair<View, String>(imageView, "logo_image")
                val pair2 = Pair<View, String>(logoTextView, "logo_text")

                val pairs = ArrayList<Pair<View, String>>()

                pairs.add(pair1)
                pairs.add(pair2)

                val options: ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, *pairs.toTypedArray())

                startActivity(intent, options.toBundle())
                finish()
            }, SPLACH_SCREEN)
        }

    }
}

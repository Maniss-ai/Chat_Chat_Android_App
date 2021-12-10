package com.example.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.splashscreen.adapters.FragmentsAdapter
import com.example.splashscreen.databinding.ActivityDashboardBinding
import com.example.splashscreen.fragments.CallsFragment
import com.example.splashscreen.fragments.ChatFragment
import com.example.splashscreen.fragments.StatusFragment
import com.example.splashscreen.transformations.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth


class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        create an object for auth firebase ....
        auth = FirebaseAuth.getInstance()

//        Hooks ....
        tabLayout = binding.tabLayout
        pager = binding.viewPager

//        set zoomOutPageTransition to fragments
        pager.setPageTransformer(true, ZoomOutPageTransformer())

//        add viewPager with tabLayout ....
        tabLayout.setupWithViewPager(pager)

//        create an object of fragmentAdapter class ....
        val fragmentsAdapter = FragmentsAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

//        add fragment using this object ....
        fragmentsAdapter.addFragment(ChatFragment(), "Chats")
        fragmentsAdapter.addFragment(StatusFragment(), "Status")
        fragmentsAdapter.addFragment(CallsFragment(), "Calls")

//        add this fragment adapter to view pager ....
        pager.adapter = fragmentsAdapter

    }

    //    inflate Menu ...
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

//    When Menu Item is Clicked ....
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
//            logout item ....
            R.id.logout -> {
                auth.signOut()
                val intent: Intent = Intent(this, Login::class.java)
                startActivity(intent)
            }

//            settings item ....
            R.id.settings -> Toast.makeText(this, "We're working on it.", Toast.LENGTH_SHORT).show()

//            profile item ....
            R.id.profile -> {
                val intent = Intent(this, UserProfile::class.java)
                startActivity(intent)
            }

//            group chat item ....
            R.id.groupChat -> {
                val intent = Intent(this, GroupChatActivity::class.java)
                startActivity(intent)
            }
        }
        return true
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
}
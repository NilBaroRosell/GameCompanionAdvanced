package com.example.companionapp.activities

import HomeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.companionapp.fragments.ChatsFragment
import com.example.companionapp.fragments.NewsFragment
import com.example.companionapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.selectedItemId = R.id.news

        //Crear fragment news
        val newsFragment = NewsFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(fragmentContainer.id, newsFragment)
        fragmentTransaction.commit()

        //Firebase authentification
        FirebaseAuth.getInstance()

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.news -> {
                    //Crear fragment news
                    val newsFragment = NewsFragment()
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, newsFragment)
                    fragmentTransaction.commit()
                }
                R.id.chats -> {
                    //Crear fragment chats
                    val chatsFragment = ChatsFragment()
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, chatsFragment)
                    fragmentTransaction.commit()
                }
                R.id.profile ->{
                    //Crear fragment profile
                    val homeFragment = HomeFragment()
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(fragmentContainer.id, homeFragment)
                    fragmentTransaction.commit()
                }
            }

            true
        }
    }
}

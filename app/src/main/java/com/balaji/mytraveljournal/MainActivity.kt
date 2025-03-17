package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn_signup=findViewById<Button>(R.id.signup_btn)
        btn_signup.setOnClickListener {
            val intent=Intent(this,Registration::class.java)
            startActivity(intent)
        }

        val btn_login=findViewById<Button>(R.id.login_btn)
        btn_login.setOnClickListener {
            val intent=Intent(this,Dashboard::class.java)
            startActivity(intent)
        }
    }
}
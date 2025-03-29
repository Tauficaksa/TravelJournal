package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val etemail=findViewById<EditText>(R.id.loginemail)
        val etpwd=findViewById<EditText>(R.id.loginpwd)
        val btn_signup=findViewById<Button>(R.id.signup_btn)
        btn_signup.setOnClickListener {
            val intent=Intent(this,Registration::class.java)
            startActivity(intent)
        }

        val btn_login=findViewById<Button>(R.id.login_btn)
        btn_login.setOnClickListener {
            val email=etemail.text.toString()
            val password=etpwd.text.toString()
            login(email,password)
        }
    }

    private fun login(email:String,password:String){
        val details= mapOf(
            "email" to email,
            "pwd" to password
        )
        Retrofit_Client.instance.loginuser(details).enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if(response.isSuccessful){
                    val user=response.body()
                    if (user != null) {
                        createSession(user.id,user.name,user.profile_image)
                    }
                    Toast.makeText(this@MainActivity,"Login Successful",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this@MainActivity,Dashboard::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this@MainActivity,"Enter valid credentials",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("maina ctivity","failure in "+t.message)
            }
        })
    }
    private fun createSession(userid:String?,name:String?,profile_image:String?){
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("user_id", userid)
        editor.putString("user_name", name)
        editor.putString("profile_image", profile_image)
        editor.apply()
    }
}
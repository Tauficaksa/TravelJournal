package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
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

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        val etname=findViewById<EditText>(R.id.inputname)
        val etemail=findViewById<EditText>(R.id.inputemail)
        val etpassword=findViewById<EditText>(R.id.inputpwd)
        val etconfirmpwd=findViewById<EditText>(R.id.inputrepwd)
        val cbischecked=findViewById<CheckBox>(R.id.checkbox)
        val signupbtn=findViewById<Button>(R.id.registerbtn)
        val backsignin=findViewById<TextView>(R.id.backsignin)
        backsignin.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        signupbtn.setOnClickListener {
            val name=etname.text.toString()
            val email=etemail.text.toString()
            val password=etpassword.text.toString()
            val confirmpwd=etconfirmpwd.text.toString()
            val ischecked=cbischecked.isChecked

            register(name,email,password,confirmpwd,ischecked)
        }
    }

    private fun register(name:String,email:String,password:String,confirmpwd:String,ischecked:Boolean){
        if(!password.equals(confirmpwd)){
            Toast.makeText(this,"retype the correct password",Toast.LENGTH_LONG).show()
        }
        else if(!ischecked){
            Toast.makeText(this,"you have to accept the terms and conditions",Toast.LENGTH_LONG).show()
        }
        else{
            val user=User(email=email,name=name,pwd=password)
            Retrofit_Client.instance.registerUser(user).enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    Toast.makeText(this@Registration, "Registered Successfully",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Toast.makeText(this@Registration,"Registration unsuccessful",Toast.LENGTH_LONG).show()
                    Log.d("main activity","onfailure "+t.message)
                }
            })
        }
    }
}
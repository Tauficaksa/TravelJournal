package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class CompleteJournalView : AppCompatActivity() {
    var imageview:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_complete_journal_view)
        val title=intent.getStringExtra("title")
        val name=intent.getStringExtra("name")
        val location=intent.getStringExtra("location")
        val image=intent.getStringExtra("journalimage")
        val desc=intent.getStringExtra("desc")
        val userid=intent.getStringExtra("user_id")
        val loggesinuserid=getUserId()
        val tvtitle=findViewById<TextView>(R.id.ctitle)
        val tvauthor=findViewById<TextView>(R.id.cauthor)
        val tvlocation=findViewById<TextView>(R.id.clocation)
        imageview=findViewById(R.id.cimage)
        val tvdesc=findViewById<TextView>(R.id.cdesc)
        tvtitle.text=title
        tvauthor.text=name
        tvlocation.text=location
        tvdesc.text=desc
        loadimage(image)
        tvauthor.setOnClickListener {
            if(loggesinuserid!=userid){
                val intent=Intent(this,OthersProfile::class.java)
                intent.putExtra("user_id",userid)
                startActivity(intent)
            }
        }
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun loadimage(imageurl:String?){
        if(imageurl==null){
            imageview?.setImageResource(R.drawable.icon_profile)
        }
        else{
            val finalurl:String= "http://10.0.2.2:5000$imageurl"
            Picasso.get().load(finalurl).into(imageview)
        }
    }
}
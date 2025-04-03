package com.balaji.mytraveljournal

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournals
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.balaji.mytraveljournal.models.User
import com.balaji.mytraveljournal.models.Users
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OthersProfile : AppCompatActivity() {

    private lateinit var journals:ArrayList<TravelJournalsItem>
    private lateinit var ivprofileimage:ImageView
    private lateinit var tvname:TextView
    private lateinit var tvfollowercount:TextView
    private lateinit var btnfollow:Button
    private lateinit var tvfollowingcount:TextView
    private var isfollowed:Boolean=false
    private lateinit var tvjournalcount:TextView
    private lateinit var othersPjAdapter: OthersPjAdapter
    private var loggedinUserId:String?=null
    private lateinit var recyclerview:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_others_profile)
        val userid=intent.getStringExtra("user_id")
        loggedinUserId=getUserId()
        journals=ArrayList()
        ivprofileimage=findViewById(R.id.othersprofileimage)
        tvname=findViewById(R.id.othersusername)
        btnfollow=findViewById(R.id.othersfollowbtn)
        tvfollowercount=findViewById(R.id.othersfollowercount)
        tvfollowingcount=findViewById(R.id.othersfollowingcount)
        tvjournalcount=findViewById(R.id.othersjournalscount)
        recyclerview=findViewById(R.id.others_profile_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        othersPjAdapter = OthersPjAdapter(journals)
        recyclerview.adapter = othersPjAdapter
        btnfollow.setOnClickListener {
            if(isfollowed){
                btnfollow.text="FOLLOW"
                unfollowUser(loggedinUserId,userid)
                isfollowed=false
            }
            else{
                btnfollow.text="UNFOLLOW"
                followUser(loggedinUserId,userid)
                isfollowed=true
            }
        }
        getUser(userid)
        getFollowingList(userid)
        getFollowerList(userid)
        getJournals(userid)
    }

    private fun followUser(userid:String?,followingid:String?){
        if(userid==null||followingid==null) return
        val details= mapOf(
            "follower_id" to userid,
            "following_id" to followingid
        )
        val apiService=Retrofit_Client.instance
        val call=apiService.followUser(details)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {

            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun unfollowUser(userid:String?,followingid:String?){
        if(userid==null||followingid==null) return
        val details= mapOf(
            "follower_id" to userid,
            "following_id" to followingid
        )
        val apiService=Retrofit_Client.instance
        val call=apiService.unfollowUser(details)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {

            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }

    private fun getUser(userid: String?) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    tvname.text=response.body()?.name
                    loadimage(response.body()?.profile_image)
                }
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
    }

    private fun getFollowingList(userid:String?){
        if(userid==null) return
        val apiService=Retrofit_Client.instance
        val call=apiService.getFollowingUsers(userid)
        call.enqueue(object : Callback<Users?> {
            override fun onResponse(call: Call<Users?>, response: Response<Users?>) {
                if(response.isSuccessful){
                    tvfollowingcount.text= (response.body()?.size).toString()
                }
            }

            override fun onFailure(call: Call<Users?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }

    private fun getFollowerList(userid: String?){
        if(userid==null) return
        val apiService=Retrofit_Client.instance
        val call=apiService.getFollowerUsers(userid)
        call.enqueue(object : Callback<Users?> {
            override fun onResponse(call: Call<Users?>, response: Response<Users?>) {
                if(response.isSuccessful){
                    tvfollowercount.text=(response.body()?.size).toString()
                    response.body()?.let {
                        for(user in it){
                            if(user.id==loggedinUserId){
                                isfollowed=true
                            }
                        }
                    }
                    if(isfollowed){
                        btnfollow.text="UNFOLLOW"
                    }
                    else{
                        btnfollow.text="FOLLOW"
                    }
                }
            }

            override fun onFailure(call: Call<Users?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }

    private fun loadimage(imageurl:String?){
        if(imageurl==null){
            ivprofileimage.setImageResource(R.drawable.icon_profile)
        }
        else{
            val finalurl:String= "http://10.0.2.2:5000$imageurl"
            Picasso.get().load(finalurl).into(ivprofileimage)
        }
    }

    private fun getJournals(userid: String?){
        if(userid==null) return
        val apiService=Retrofit_Client.instance
        val call=apiService.getJournalsOfUser(userid)
        call.enqueue(object : Callback<TravelJournals?> {
            override fun onResponse(
                call: Call<TravelJournals?>,
                response: Response<TravelJournals?>
            ) {
                if(response.isSuccessful){
                    journals.clear()
                    response.body()?.let { journals.addAll(it) }
                    tvjournalcount.text=journals.size.toString()
                    recyclerview.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<TravelJournals?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }
}
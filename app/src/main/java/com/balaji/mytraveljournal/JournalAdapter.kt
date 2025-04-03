package com.balaji.mytraveljournal

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalAdapter(private val journals:MutableList<Journal>):RecyclerView.Adapter<JournalAdapter.ViewHolder>() {
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivprofileImage:ImageView=view.findViewById(R.id.profileimage)
        val tvname:TextView=view.findViewById(R.id.name)
        val tvtitle:TextView=view.findViewById(R.id.title)
        val tvdescription:TextView=view.findViewById(R.id.description)
        val ivjournalImage:ImageView=view.findViewById(R.id.journal_image)
        val btnLike:ImageView=view.findViewById(R.id.like_button)
        val btnmoreInfo:ImageView=view.findViewById(R.id.more_info_view)
        val tvlikecount:TextView=view.findViewById(R.id.likecount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.journal_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =journals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal=journals[position]
        val userid=getUserId(holder.itemView.context)
        loadimage(journal.profileImage,holder.ivprofileImage)
        loadimage(journal.journalImage,holder.ivjournalImage)
        holder.tvname.text=journal.name
        holder.tvtitle.text=journal.title
        holder.tvdescription.text=journal.description.substring(0,100)+"..."
        holder.tvlikecount.text=(journal.likecount).toString()
        if(journal.isLiked){
            holder.btnLike.setImageResource(R.drawable.like_button)
        }
        else{
            holder.btnLike.setImageResource(R.drawable.like_before_click)
        }


        holder.tvname.setOnClickListener {
            val intent=Intent(holder.itemView.context,OthersProfile::class.java)
            intent.putExtra("user_id",journal.userid)
            holder.itemView.context.startActivity(intent)
        }

        holder.btnLike.setOnClickListener {
            if(!journal.isLiked){
                holder.btnLike.setImageResource(R.drawable.like_button)
                likeJournal(userid,journal.id)
                journal.likecount+=1
                journal.isLiked=true
                notifyDataSetChanged()
            }
            else{
                holder.btnLike.setImageResource(R.drawable.like_before_click)
                unlikeJournal(userid,journal.id)
                journal.likecount-=1
                journal.isLiked=false
                notifyDataSetChanged()
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CompleteJournalView::class.java)
            intent.putExtra("title",journal.title)
            intent.putExtra("name",journal.name)
            intent.putExtra("location",journal.location)
            intent.putExtra("journalimage",journal.journalImage)
            intent.putExtra("desc",journal.description)
            holder.itemView.context.startActivity(intent)
        }
        holder.btnmoreInfo.setOnClickListener {
            val intent = Intent(holder.itemView.context, CompleteJournalView::class.java)
            intent.putExtra("title",journal.title)
            intent.putExtra("name",journal.name)
            intent.putExtra("location",journal.location)
            intent.putExtra("journalimage",journal.journalImage)
            intent.putExtra("desc",journal.description)
            holder.itemView.context.startActivity(intent)
        }
    }
    private fun loadimage(imageurl:String?,imageview:ImageView){
        if(imageurl==null){
            imageview.setImageResource(R.drawable.icon_profile)
        }
        else{
            val fullurl:String= "http://10.0.2.2:5000$imageurl"
            Picasso.get().load(fullurl).into(imageview)
        }
    }
    private fun likeJournal(userid:String?,journalid:String){
        if(userid==null) return
        val details= mapOf(
            "user_id" to userid,
            "journal_id" to journalid
        )
        val apiService=Retrofit_Client.instance
        val call=apiService.likeJournal(details)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {

            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }
    private fun unlikeJournal(userid:String?,journalid:String){
        if(userid==null) return
        val details= mapOf(
            "user_id" to userid,
            "journal_id" to journalid
        )
        val apiService=Retrofit_Client.instance
        val call=apiService.unlikeJournal(details)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {

            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Log.d("main activity","failure in "+t.message)
            }
        })
    }

    private fun getUserId(context: android.content.Context): String? {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
}
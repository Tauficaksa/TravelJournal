package com.balaji.mytraveljournal

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.balaji.mytraveljournal.models.User
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchJournalAdapter(private val journals:MutableList<SearchJournal>):RecyclerView.Adapter<SearchJournalAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivprofileimage=view.findViewById<ImageView>(R.id.searchprofileimage)
        val tvname=view.findViewById<TextView>(R.id.searchname)
        val tvlocation=view.findViewById<TextView>(R.id.searchlocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_view_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int=journals.size

    fun updateList(newList: List<SearchJournal>) {
        journals.clear()
        journals.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal=journals[position]
        holder.tvlocation.text=journal.location
        getUser(journal.user_id,holder.ivprofileimage,holder.tvname)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CompleteJournalView::class.java)
            intent.putExtra("title",journal.name)
            intent.putExtra("name",journal.username)
            intent.putExtra("location",journal.location)
            intent.putExtra("journalimage",journal.image)
            intent.putExtra("desc",journal.description)
            intent.putExtra("user_id",journal.user_id)
            holder.itemView.context.startActivity(intent)
        }
        holder.ivprofileimage.setOnClickListener {
            val intent=Intent(holder.itemView.context,OthersProfile::class.java)
            intent.putExtra("user_id",journal.user_id)
            holder.itemView.context.startActivity(intent)
        }
    }

    private fun getUser(userid: String?,imageview:ImageView,tvusername:TextView) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    loadimage(response.body()?.profile_image,imageview)
                    tvusername.text=response.body()?.name
                }
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
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
}
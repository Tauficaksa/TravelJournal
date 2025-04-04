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

class OthersPjAdapter(private val journals:MutableList<TravelJournalsItem>):RecyclerView.Adapter<OthersPjAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvtitle=view.findViewById<TextView>(R.id.ojtitle)
        val ivimage=view.findViewById<ImageView>(R.id.ojimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.others_profile_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int=journals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal=journals[position]
        holder.tvtitle.text=journal.name
        loadimage(journal.image,holder.ivimage)
        holder.itemView.setOnClickListener {
            getUser(journal.user_id,journal,holder.itemView.context)
        }
    }

    private fun getUser(userid: String?,journal:TravelJournalsItem,context:Context) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    val intent = Intent(context, CompleteJournalView::class.java)
                    intent.putExtra("title",journal.name)
                    intent.putExtra("name",response.body()?.name)
                    intent.putExtra("location",journal.location)
                    intent.putExtra("journalimage",journal.image)
                    intent.putExtra("desc",journal.description)
                    intent.putExtra("user_id",journal.user_id)
                    context.startActivity(intent)
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
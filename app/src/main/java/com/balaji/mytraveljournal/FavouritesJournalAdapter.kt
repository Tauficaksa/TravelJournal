package com.balaji.mytraveljournal

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.balaji.mytraveljournal.models.User
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouritesJournalAdapter(private val favouritejournals:List<TravelJournalsItem>):RecyclerView.Adapter<FavouritesJournalAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
       val tvtitle=view.findViewById<TextView>(R.id.ftitle)
        val ivimage=view.findViewById<ImageView>(R.id.fimage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.favourites_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int=favouritejournals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favouritejournal=favouritejournals[position]
        holder.tvtitle.text=favouritejournal.name
        loadimage(favouritejournal.image,holder.ivimage)
        holder.itemView.setOnClickListener {
            getUser(favouritejournal.user_id,holder.itemView.context,favouritejournal)
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

    private fun getUser(userid: String,context:Context,journal:TravelJournalsItem) {
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    val intent=Intent(context,CompleteJournalView::class.java)
                    intent.putExtra("title",journal.name)
                    intent.putExtra("name",response.body()?.name)
                    intent.putExtra("location",journal.location)
                    intent.putExtra("journalimage",journal.image)
                    intent.putExtra("desc",journal.description)
                    intent.putExtra("user_id",journal.user_id)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context,"Could not fetch journal",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
                Toast.makeText(context,"Could not fetch journal",Toast.LENGTH_SHORT).show()
            }
        })
    }
}
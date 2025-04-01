package com.balaji.mytraveljournal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProfileJournalAdapter(private val profilejournals:List<ProfileJournal>):RecyclerView.Adapter<ProfileJournalAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvtitle:TextView=view.findViewById(R.id.pjtitle)
        val ivjimage:ImageView=view.findViewById(R.id.pjimage)
        val ivedit:ImageView=view.findViewById(R.id.pjedit)
        val tvlikes:TextView=view.findViewById(R.id.pjlikes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.profile_journal_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =profilejournals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profilejournal=profilejournals[position]
        holder.tvtitle.text=profilejournal.title
        loadimage(profilejournal.journal_image,holder.ivjimage)
        holder.tvlikes.text=(profilejournal.likes).toString()
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
package com.balaji.mytraveljournal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.squareup.picasso.Picasso

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
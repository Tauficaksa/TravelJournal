package com.balaji.mytraveljournal

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(private val journals:List<Journal>):RecyclerView.Adapter<JournalAdapter.ViewHolder>() {
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivprofileImage:ImageView=view.findViewById(R.id.profileimage)
        val tvname:TextView=view.findViewById(R.id.name)
        val tvtitle:TextView=view.findViewById(R.id.title)
        val tvdescription:TextView=view.findViewById(R.id.description)
        val ivjournalImage:ImageView=view.findViewById(R.id.journal_image)
        val btnfollow:TextView=view.findViewById(R.id.followbtn)
        val btnLike:ImageView=view.findViewById(R.id.like_button)
        val btnmoreInfo:ImageView=view.findViewById(R.id.more_info_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.journal_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =journals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal=journals[position]
        holder.ivprofileImage.setImageResource(journal.profileImage)
        holder.tvname.text=journal.name
        holder.tvtitle.text=journal.title
        holder.tvdescription.text=journal.description
        holder.ivjournalImage.setImageResource(journal.journalImage)
        if(journal.isLiked){
            holder.btnLike.setImageResource(R.drawable.like_button)
        }
        else{
            holder.btnLike.setImageResource(R.drawable.like_before_click)
        }

        if(journal.isFollowed){
            holder.btnfollow.text="FOLLOWING"
        }
        else{
            holder.btnfollow.text="FOLLOW"
        }

        holder.btnLike.setOnClickListener {
            if(!journal.isLiked){
                holder.btnLike.setImageResource(R.drawable.like_button)
                journal.isLiked=true
            }
            else{
                holder.btnLike.setImageResource(R.drawable.like_before_click)
                journal.isLiked=false
            }
        }
        holder.btnfollow.setOnClickListener {
            if(!journal.isFollowed){
                journal.isFollowed=true
                holder.btnfollow.text="FOLLOWING"
            }
            else{
                journal.isFollowed=false
                holder.btnfollow.text="FOLLOW"
            }
        }
        holder.btnmoreInfo.setOnClickListener {
            val intent = Intent(holder.itemView.context, CompleteJournalView::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
}
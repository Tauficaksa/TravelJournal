package com.balaji.mytraveljournal

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileJournalAdapter(private val profilejournals:MutableList<ProfileJournal>):RecyclerView.Adapter<ProfileJournalAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvtitle:TextView=view.findViewById(R.id.pjtitle)
        val ivjimage:ImageView=view.findViewById(R.id.pjimage)
        val ivedit:ImageView=view.findViewById(R.id.pjedit)
        val tvlikes:TextView=view.findViewById(R.id.pjlikes)
        val ivdelete:ImageView=view.findViewById(R.id.pjdelete)
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
        holder.ivdelete.setOnClickListener {
            showalert(profilejournal.id,holder.itemView.context,profilejournal)
        }
        holder.ivedit.setOnClickListener {
            editalertdialog(profilejournal.id,holder.itemView.context,profilejournal)
        }
    }

    private fun editalertdialog(journalid:String,context: Context,profilejournal: ProfileJournal){
        val dialogview=LayoutInflater.from(context).inflate(R.layout.edit_journal_view,null)
        val ettitle=dialogview.findViewById<EditText>(R.id.editjtitle)
        val etlocation=dialogview.findViewById<EditText>(R.id.editjlocation)
        val etdesc=dialogview.findViewById<EditText>(R.id.editjdesc)
        ettitle.setText(profilejournal.title)
        etlocation.setText(profilejournal.location)
        etdesc.setText(profilejournal.description)
        val dialog=AlertDialog.Builder(context)
            .setView(dialogview)
            .setPositiveButton("Update"){_,_->
                val title=ettitle.text.toString()
                val location=etlocation.text.toString()
                val desc=etdesc.text.toString()
                if(desc!=""&&desc.length<200){
                    Toast.makeText(context,"description should be atleast 200 words",Toast.LENGTH_SHORT).show()
                }
                else{
                    updateJournal(journalid,title,location,desc,context)
                    if(title!="") profilejournal.title=title
                    if(desc!="") profilejournal.description=desc
                    if(location!="") profilejournal.location=location
                    notifyDataSetChanged()
                }
            }
            .setNegativeButton("cancel"){_,_->}
            .create()
        dialog.show()
    }

    private fun updateJournal(id:String,title:String,location:String,desc:String,context: Context){
        val details= mapOf(
            "name" to title,
            "location" to location,
            "description" to desc
        )
        val apiservice=Retrofit_Client.instance
        val call=apiservice.updateJournal(id,details)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"Journal updated successfully",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"Journal  not updated successfully",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                Toast.makeText(context,"Journal updated request failed successfully",Toast.LENGTH_SHORT).show()
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

    private fun showalert(id:String,context: Context,profilejournal:ProfileJournal){
        val dialog=AlertDialog.Builder(context)
            .setTitle("DELETE")
            .setMessage("Are you sure you want to delete the journal")
            .setPositiveButton("Yes"){_,_->
                deleteJournal(id,context)
                profilejournals.remove(profilejournal)
                notifyDataSetChanged()
            }
            .setNegativeButton("No"){_,_->}
            .create()
        dialog.show()
    }

    private fun deleteJournal(id:String,context:Context){
        val apiservice=Retrofit_Client.instance
        val call=apiservice.deleteJournal(id)
        call.enqueue(object : Callback<Unit?> {
            override fun onResponse(call: Call<Unit?>, response: Response<Unit?>) {
                if(response.isSuccessful){
                    Toast.makeText(context,"journal deleted successfully",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {

            }
        })
    }
}
package com.balaji.mytraveljournal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournals
import com.balaji.mytraveljournal.models.TravelJournalsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavouritesFragment : Fragment() {

    private lateinit var recyclerview:RecyclerView
    private lateinit var likedjournals:ArrayList<TravelJournalsItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerview=view.findViewById(R.id.favourites_recyclerview)
        recyclerview.layoutManager=GridLayoutManager(requireContext(),2)
        recyclerview.adapter=FavouritesJournalAdapter(emptyList())
        likedjournals=ArrayList()
        val userid=getUserId()
        getAllLikedJournals(userid)
        return view
    }

    private fun getAllLikedJournals(userid: String?) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getLikedJournals(userid)

        call.enqueue(object : Callback<TravelJournals?> {
            override fun onResponse(call: Call<TravelJournals?>, response: Response<TravelJournals?>) {
                if(response.isSuccessful){
                    likedjournals.clear()
                    response.body()?.let { likedjournals.addAll(it) }
                    recyclerview.adapter=FavouritesJournalAdapter(likedjournals)
                    recyclerview.adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<TravelJournals?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
    }

    private fun getUserId(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

}
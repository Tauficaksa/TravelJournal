package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournals
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.balaji.mytraveljournal.models.User
import com.balaji.mytraveljournal.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var alljournals: ArrayList<TravelJournalsItem>
    private lateinit var allfollowing: ArrayList<User>
    private lateinit var likedjournals: ArrayList<TravelJournalsItem>
    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerview = view.findViewById(R.id.home_recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter=JournalAdapter(mutableListOf())
        alljournals = ArrayList()
        allfollowing = ArrayList()
        likedjournals = ArrayList()

        val userid = getUserId()
        if (userid != null) {
            getAllFollowing(userid)
            getAllLikedJournals(userid)
            getAllJournals(userid) { journals ->
                recyclerview.adapter = JournalAdapter(journals.toMutableList())
            }
        }
        return view
    }

    private fun getUserId(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

    private fun getAllJournals(userid: String, callback: (List<Journal>) -> Unit) {
        val apiService = Retrofit_Client.instance
        val call = apiService.getAllJournals(userid)

        call.enqueue(object : Callback<TravelJournals?> {
            override fun onResponse(call: Call<TravelJournals?>, response: Response<TravelJournals?>) {
                if (response.isSuccessful) {
                    alljournals.clear()
                    response.body()?.let { journalList ->
                        alljournals.addAll(journalList)
                        val journals = mutableListOf<Journal>()
                        var processedCount = 0

                        for (journal in alljournals) {
                            getUser(journal.user_id) { journaluser ->
                                val name = journaluser?.name
                                val profileimage = journaluser?.profile_image
                                val isfollowed = allfollowing.any { it.id == journal.user_id }
                                val isliked = likedjournals.any { it.id == journal.id }

                                journals.add(
                                    Journal(
                                        journal.id, journal.user_id, profileimage, name,
                                        journal.name, journal.description, journal.image, isfollowed, isliked
                                    )
                                )

                                processedCount++
                                if (processedCount == alljournals.size) {
                                    callback(journals)
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Could not load journals", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TravelJournals?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
    }

    private fun getUser(userid: String, callback: (User?) -> Unit) {
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
                callback(null)
            }
        })
    }

    private fun getAllFollowing(userid: String) {
        val apiService = Retrofit_Client.instance
        val call = apiService.getFollowingUsers(userid)

        call.enqueue(object : Callback<Users?> {
            override fun onResponse(call: Call<Users?>, response: Response<Users?>) {
                if (response.isSuccessful) {
                    allfollowing.clear()
                    response.body()?.let { allfollowing.addAll(it) }
                } else {
                    Toast.makeText(requireContext(), "Could not load following list", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Users?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
    }

    private fun getAllLikedJournals(userid: String) {
        val apiService = Retrofit_Client.instance
        val call = apiService.getLikedJournals(userid)

        call.enqueue(object : Callback<TravelJournals?> {
            override fun onResponse(call: Call<TravelJournals?>, response: Response<TravelJournals?>) {
                if(response.isSuccessful){
                    likedjournals.clear()
                    response.body()?.let { likedjournals.addAll(it) }
                }
            }

            override fun onFailure(call: Call<TravelJournals?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
            }
        })
    }
}

package com.balaji.mytraveljournal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balaji.mytraveljournal.api.Retrofit_Client
import com.balaji.mytraveljournal.models.TravelJournals
import com.balaji.mytraveljournal.models.TravelJournalsItem
import com.balaji.mytraveljournal.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private  lateinit var recyclerview:RecyclerView
    private lateinit var searchjournals:ArrayList<SearchJournal>
    private lateinit var originaljournals:ArrayList<SearchJournal>
    private lateinit var searchbarview:SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search, container, false)
        searchjournals= ArrayList()
        originaljournals= ArrayList()
        recyclerview=view.findViewById(R.id.search_recyclerview)
        searchbarview=view.findViewById(R.id.searchbar)
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        recyclerview.adapter=SearchJournalAdapter(searchjournals)
        val userid=getUserId()
        getAllJournals(userid)
        searchbarview.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterJournals(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterJournals(it) }
                return true
            }

        })
        return view
    }

    private fun filterJournals(query:String){
        if (query.isBlank()) {
            (recyclerview.adapter as? SearchJournalAdapter)?.updateList(originaljournals)
        }
        else{
            val filteredlist=searchjournals.filter {
                it.username!!.contains(query, ignoreCase = true)||it.location.contains(query, ignoreCase = true)
            }
            (recyclerview.adapter as? SearchJournalAdapter)?.updateList(filteredlist)
        }
    }

    private fun getAllJournals(userid: String?) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getAllJournals(userid)

        call.enqueue(object : Callback<TravelJournals?> {
            override fun onResponse(call: Call<TravelJournals?>, response: Response<TravelJournals?>) {
                if (response.isSuccessful) {
                    searchjournals.clear()
                    originaljournals.clear()
                    response.body()?.let {
                        var count=0
                        for(journal in it){
                            getUser(journal.user_id){username->
                                searchjournals.add(SearchJournal(journal.description,journal.id,journal.image,journal.location,journal.name,journal.user_id,username))
                                count++
                                if(count==it.size){
                                    recyclerview.adapter?.notifyDataSetChanged()
                                    originaljournals.addAll(searchjournals)
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

    private fun getUser(userid: String?,callback: (String?)->Unit) {
        if(userid==null) return
        val apiService = Retrofit_Client.instance
        val call = apiService.getUser(userid)

        call.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    callback(response.body()?.name)
                }
                else{
                    callback("")
                }
            }
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Log.d("HomeFragment", "Failure: ${t.message}")
                callback("")
            }
        })
    }

    private fun getUserId(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }

}
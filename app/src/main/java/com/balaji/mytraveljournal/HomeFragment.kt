package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerview=view.findViewById<RecyclerView>(R.id.home_recyclerview)
        val journals= listOf(
            Journal(R.drawable.icon_profile,"Balaji","sankranti trip","Hyderabad, the capital of Telangana, is a major city in southern India known for its rich history, vibrant culture, and booming tech industry. Founded in 1591 by Muhammad Quli Qutb Shah, the city is famous for its iconic landmarks like Charminar, Golconda Fort, and Hussain Sagar Lake. It is also a hub for IT companies, earning it the nickname Cyberabad.",
                R.drawable.hyd_img,false,false),
            Journal(R.drawable.icon_profile,"taufic","tirupati","Hyderabad, the capital of Telangana, is a major city in southern India known for its rich history, vibrant culture, and booming tech industry. Founded in 1591 by Muhammad Quli Qutb Shah, the city is famous for its iconic landmarks like Charminar, Golconda Fort, and Hussain Sagar Lake. It is also a hub for IT companies, earning it the nickname Cyberabad.",
                R.drawable.tirupati,false,false),
            Journal(R.drawable.icon_profile,"manith","Hyderabad","Hyderabad, the capital of Telangana, is a major city in southern India known for its rich history, vibrant culture, and booming tech industry. Founded in 1591 by Muhammad Quli Qutb Shah, the city is famous for its iconic landmarks like Charminar, Golconda Fort, and Hussain Sagar Lake. It is also a hub for IT companies, earning it the nickname Cyberabad.",
                R.drawable.hyd_img,false,false)
        )
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        recyclerview.adapter=JournalAdapter(journals)
        return view
    }
}
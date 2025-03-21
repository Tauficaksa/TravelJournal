package com.balaji.mytraveljournal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        val moreinfobutton=view.findViewById<ImageView>(R.id.more_info_view)
        moreinfobutton.setOnClickListener {
            val intent=Intent(requireContext(),CompleteJournalView::class.java)
            startActivity(intent)
        }
        return view
    }
}
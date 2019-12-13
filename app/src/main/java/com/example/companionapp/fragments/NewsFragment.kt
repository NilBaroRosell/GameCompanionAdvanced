package com.example.companionapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.companionapp.models.NewsData
import com.example.companionapp.adapters.NewsListAdapter
import com.example.companionapp.R
import com.example.companionapp.utils.COLLECTION_NEWS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        FirebaseFirestore.getInstance().collection(COLLECTION_NEWS).get().addOnSuccessListener { querySnapshot ->
            // TODO: borrar los elementos del cardview
                val myNews = querySnapshot.toObjects(NewsData::class.java)

                recyclerNews.layoutManager = LinearLayoutManager(context)

                val newsAdapter = NewsListAdapter()

                newsAdapter.elements = ArrayList(myNews.toList())

                recyclerNews.adapter = newsAdapter
        }.addOnFailureListener {

        }
    }

}
package com.example.companionapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.companionapp.R
import com.example.companionapp.models.NewsData
import kotlinx.android.synthetic.main.news_elements.view.*

class NewsListAdapter: RecyclerView.Adapter<NewsListAdapter.ViewHolder>(){

    var elements = ArrayList<NewsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_elements, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return elements.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  element = elements[position]

        //Update Views
        holder.text.text = element.text
        Glide
            .with(holder.image.context)
            .load(element.url)
            .into(holder.image)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text = itemView.newText
        val image = itemView.newImage
    }
}
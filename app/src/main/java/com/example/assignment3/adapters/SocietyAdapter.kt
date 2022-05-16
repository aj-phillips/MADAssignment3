package com.example.assignment3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.R
import com.example.assignment3.holders.SocietyHolder
import com.example.assignment3.model.society.Data
import com.squareup.picasso.Picasso

class SocietyAdapter(private val dataList: MutableList<Data>) : RecyclerView.Adapter<SocietyHolder>() {
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocietyHolder {
        context = parent.context
        return SocietyHolder(LayoutInflater.from(context).inflate(R.layout.society_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: SocietyHolder, position: Int) {
        // Get data
        val data = dataList[position]

        // Get component references
        val societyImageView = holder.itemView.findViewById<ImageView>(R.id.societyImage)
        val societyNameTextView = holder.itemView.findViewById<TextView>(R.id.societyName)
        val societySummaryTextView = holder.itemView.findViewById<TextView>(R.id.societySummary)

        // Set variables to data values
        val societyName = "${data.name}"
        val societySummary = "${data.summary}"

        // Set the values to the views
        societyNameTextView.text = societyName
        societySummaryTextView.text = societySummary

        Picasso.get().load(data.image).into(societyImageView)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, societyName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
package com.example.assignment3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3.holders.CourseHolder
import com.example.assignment3.R
import com.example.assignment3.model.course.Data
import com.squareup.picasso.Picasso

class CourseAdapter(private val dataList: MutableList<Data>) : RecyclerView.Adapter<CourseHolder>() {
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        context = parent.context
        return CourseHolder(LayoutInflater.from(context).inflate(R.layout.course_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {
        // Get data
        val data = dataList[position]

        // Get component references
        val courseImageView = holder.itemView.findViewById<ImageView>(R.id.courseImage)
        val courseNameTextView = holder.itemView.findViewById<TextView>(R.id.courseName)
        val courseSummaryTextView = holder.itemView.findViewById<TextView>(R.id.courseSummary)

        // Set variables to data values
        val courseName = "${data.name}"
        val courseSummary = "${data.summary}"

        // Set the values to the views
        courseNameTextView.text = courseName
        courseSummaryTextView.text = courseSummary

        Picasso.get().load(data.image).into(courseImageView)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, courseName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
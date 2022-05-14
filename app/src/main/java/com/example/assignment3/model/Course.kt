package com.example.assignment3.model


import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("data")
    val `data`: List<Data>
)
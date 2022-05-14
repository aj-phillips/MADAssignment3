package com.example.assignment3.model.society


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("summary")
    val summary: String
)
package com.example.assignment3.model.society


import com.google.gson.annotations.SerializedName

data class Society(
    @SerializedName("data")
    val `data`: List<Data>
)
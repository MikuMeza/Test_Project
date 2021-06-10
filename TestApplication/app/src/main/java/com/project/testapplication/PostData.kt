package com.project.testapplication

import com.google.gson.annotations.SerializedName

data class PostData(
    @SerializedName("title")
    val title: String,
    @SerializedName("desc")
    val desc: String
)

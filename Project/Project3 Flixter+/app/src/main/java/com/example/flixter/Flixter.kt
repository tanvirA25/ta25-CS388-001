package com.example.flixter

import com.google.gson.annotations.SerializedName

class Flixter {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("backdrop_path")
    var movieImageUrl: String? = null

    @SerializedName("overview")
    var description: String? = null

}
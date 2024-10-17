package com.example.flixter

import com.google.gson.annotations.SerializedName

class Flixter {

    @SerializedName("id")
    var id = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("original_name")
    var name: String? = null

    @SerializedName("poster_path")
    var movieImageUrl: String? = null

    @SerializedName("overview")
    var description: String? = null

    @SerializedName("release_date")
    var release: String? = null

    @SerializedName("vote_average")
    var vote: String? = null

}
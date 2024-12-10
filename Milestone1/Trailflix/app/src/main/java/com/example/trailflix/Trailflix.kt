package com.example.trailflix

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Trailflix")
data class Trailflix(
        @PrimaryKey(autoGenerate = true) val localId: Int = 0,
        var firestoreId: String? = null,
        val content: String,
        val isDone: Boolean = false,
        val completedDate: Long? = null ,
        val userId: String //author
         )

class TrailflixItem {

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
        var vote: Double? = null

        @SerializedName("key")
        var trailer_key: String? = null

        @SerializedName("site")
        var website: String? = null

        @SerializedName("media_type")
        var media_type: String? = null

        @SerializedName("known_for")
        var knownFor: List<TrailflixItem>? = null

}

class Actor(
        @SerializedName("name")
        val actorName: String? = null, // Matches "name" in JSON

        @SerializedName("profile_path")
        val actorImageUrl: String? = null // Matches "profile_path" in JSON
)


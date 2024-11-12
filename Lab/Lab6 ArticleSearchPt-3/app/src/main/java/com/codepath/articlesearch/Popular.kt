// PopularArticlesResponse.kt
package com.codepath.articlesearch

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class PopularNewsResponse(
    @SerialName("status")
    val status: String?,
    @SerialName("num_results")
    val numResults: Int?,
    @SerialName("results")
    val results: List<PopularArticle>?
)

@Keep
@Serializable
data class PopularArticle(
    @SerialName("url")
    val url: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("byline")
    val byline: String?,
    @SerialName("abstract")
    val abstract: String?,
    @SerialName("media")
    val media: List<Media>?
) : java.io.Serializable {
    val mediaImageUrl: String?
        get() = media?.firstOrNull()?.mediaMetadata?.lastOrNull()?.url ?: "default_image_url"
}

@Keep
@Serializable
data class Media(
    @SerialName("media-metadata")
    val mediaMetadata: List<MediaMetadata>?
) : java.io.Serializable

@Keep
@Serializable
data class Title(
    @SerialName("main")
    val main: String
) : java.io.Serializable


@Keep
@Serializable
data class MediaMetadata(
    @SerialName("url")
    val url: String?,
) : java.io.Serializable
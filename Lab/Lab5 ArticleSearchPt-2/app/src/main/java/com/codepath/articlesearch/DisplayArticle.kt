package com.codepath.articlesearch

import kotlinx.serialization.Serializable
data class DisplayArticle(
    val headline: String?,
    val abstract: String?,
    val byline: String?,
    val mediaImageUrl: String?
) : java.io.Serializable
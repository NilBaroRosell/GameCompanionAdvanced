package com.example.companionapp.models

import com.google.gson.annotations.SerializedName

data class StreamModel(
    val id: String? = null,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("game_id") val gameId: String? = null,
    @SerializedName("user_name") val username: String? = null,
    val title: String? = null,
    @SerializedName("viewer_count") val viewerCount: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null)
    {
        var game: GameModel? = null
        fun getTumbnailImageUrl(width: Int = 300, height: Int = 300):String?
        {
            return thumbnailUrl?.replace("{width}", width.toString())?.replace("{height}", height.toString())
        }
    }

data class StreamsResponse(
    val data: List <StreamModel>? = null,
    val pagination: TwitchPagination? = null
)

data class TwitchPagination(
        val cursor: String? = null)
package com.example.companionapp.models

import com.google.gson.annotations.SerializedName

data class GameModel(
        val id: String? = null,
        val name: String? = null,
        @SerializedName("box_art_url") val imageUrl: String? = null
)

data class GamesResponse(
        val data: List <GameModel>? = null
)
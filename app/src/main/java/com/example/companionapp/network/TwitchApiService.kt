package com.example.companionapp.network

import com.example.companionapp.models.GameModel
import com.example.companionapp.models.GamesResponse
import com.example.companionapp.models.StreamsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchApiService {
    @Headers("Client-ID: $clientId")
    @GET("streams")
    fun getCharacters(): Call<StreamsResponse>

    @Headers("Client-ID: $clientId")
    @GET("games")
    fun getGames(@Query("id") gameIds: List<String>): retrofit2.Call<GamesResponse>

    companion object
    {
        private const val clientId = "ywvglt0gib8rqdly0ejobehqfi071m"
        //http client
        private var retrofit = Retrofit.Builder()
                .baseUrl("https://api.twitch.tv/helix/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val endpoints = retrofit.create<TwitchApiService>(TwitchApiService::class.java)
    }
}
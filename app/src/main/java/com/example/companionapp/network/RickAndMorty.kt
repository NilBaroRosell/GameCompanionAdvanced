package com.example.companionapp.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface RickAndMorty {
    @GET("character")
    fun getCharacters(): Call<ResponseBody>

}
package com.example.companionapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyHttpClient
{
    companion object
    {
        //http client
        private var retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val endpoints = retrofit.create<RickAndMorty>(RickAndMorty::class.java)
    }
}
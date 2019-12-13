package com.example.companionapp.fragments

import android.net.DnsResolver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.companionapp.R
import com.example.companionapp.models.GamesResponse
import com.example.companionapp.models.StreamsResponse
import com.example.companionapp.network.RickAndMortyHttpClient
import com.example.companionapp.network.TwitchApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class StreamsFragment: Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        TwitchApiService.endpoints.getCharacters().enqueue(object : retrofit2.Callback<StreamsResponse>
        {
            override fun onResponse(call: retrofit2.Call<StreamsResponse>, response: retrofit2.Response<StreamsResponse>) {
                Log.i("StreamsFragment", "")
                if(response.isSuccessful)
                {
                    Log.i("StreamsFragment", response.body()?.toString() ?: "Null bpdy")
                    val streams = response.body()?.data

                    //Get list of all gameId
                    val ids = streams?.map { it.gameId ?: ""}

                    ids?.let {ids ->
                        TwitchApiService.endpoints.getGames(ids).enqueue(object: retrofit2.Callback<GamesResponse>
                        {
                            override fun onFailure(call: retrofit2.Call<GamesResponse>, t: Throwable) {
                                Log.w("StreamsFragment", t)
                            }

                            override fun onResponse(call: retrofit2.Call<GamesResponse>, response: retrofit2.Response<GamesResponse>) {
                                if(response.isSuccessful)
                                {
                                    val games = response.body()?.data
                                    streams?.forEach {stream ->
                                        games?.forEach {game ->
                                            if(stream.gameId == game.id)
                                            {
                                                stream.game = game
                                            }
                                        }
                                    }
                                    adapter.list = ArrayList(streams.map { it.game?.name ?: "" })
                                    adapter.notifyDataSetChanged()

                                    Log.i("StreamsFragment", "Got games $games")
                                    Log.i("StreamsFragment", "Got games $streams")

                                }
                                else{
                                    Log.w("StreamsFragment", "Error getting games")
                                }
                            }
                        })
                    }
                }
                else
                {
                    Log.i("StreamsFragment", response.message())
                }
            }
            override fun onFailure(call: Call<StreamsResponse>, t: Throwable) {
                Log.w("StreamsFragment", t)
            }

        })
    }

    private fun getRickAndMorty()
    {
        RickAndMortyHttpClient.endpoints.getCharacters().enqueue(object : retrofit2.Callback<ResponseBody>
        {
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.w("StreamsFragment", t)
            }
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                Log.i("StreamsFragment", "")
                if(response.isSuccessful)
                {
                    Log.i("StreamsFragment", "ALL OK")
                    Log.i("StreamsFragment", response.body()?.string() ?: "Empty bpdy")
                }
            }
        })
    }
}
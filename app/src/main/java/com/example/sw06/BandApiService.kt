package com.example.sw06


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BandApiService {

    @GET("all.json")
    fun getBands(): Call<List<BandCode>>

    @GET("info/{Code}.json")
    fun getCurrentBand(@Path("Code") code: String): Call<BandInfo>?


}
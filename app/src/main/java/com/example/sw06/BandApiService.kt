package com.example.sw06

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.http.GET

interface BandApiService {

    @GET("all.json")
    fun getBandNames(): Call<List<BandCode>>

    @GET("all.json")
    fun getBandInfo(): Call<List<BandInfo>>

    data class BandCode(
        val name: String,
        val code: String
    )

    @JsonClass(generateAdapter = true)
    data class BandInfo(
        val name: String,
        val foundingYear: Int,
        val homeCountry: String,
        val bestOfCdCoverImageUrl: String?
    )
    /*
    @GET("all.json")
    fun getBandNames(): Call<List<BandCode>>

    val moshi: Moshi = Moshi.Builder().add(BandApiService()).build()
    val adapter: JsonAdapter<BandCode> = moshi.adapter(BandCode::class.java)
    val BandCode = adapter.fromJson(BandApiService.BandCode)
*/
}
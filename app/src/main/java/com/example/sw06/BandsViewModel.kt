package com.example.sw06

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sw06.BandApiService.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {
    val bands: MutableLiveData<BandCode> = MutableLiveData()
    val currentBand: MutableLiveData<BandInfo?> = MutableLiveData()
    var testString: String = ""

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()
    private val bandsService = retrofit.create(BandApiService::class.java)
    private val call = bandsService.getBandNames()

    fun getBandinfo() {
        call.enqueue(object : Callback<List<BandCode>> {
            override fun onFailure(call: Call<List<BandCode>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<List<BandCode>>,
                response: Response<List<BandCode>>
            ) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    testString = response.body().orEmpty().toString()
                    //bands.value = response.body().orEmpty()
                }
            }


        }
        )
    }


}
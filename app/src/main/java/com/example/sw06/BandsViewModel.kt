package com.example.sw06

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {

    var bands: MutableLiveData<List<BandCode>>? = null
    var currentBand: MutableLiveData<BandInfo?>? = null
    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()
    private val bandsService = retrofit.create(BandApiService::class.java)


    init {
        bands = MutableLiveData()
        currentBand = MutableLiveData()
        bands?.value = emptyList()

    }

    fun getBands() = bandsService.getBands().enqueue(object : Callback<List<BandCode>> {
        override fun onResponse(
            call: Call<List<BandCode>>,
            response: Response<List<BandCode>>
        ) {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                bands?.value = response.body().orEmpty()
            }
        }

        override fun onFailure(call: Call<List<BandCode>>, t: Throwable) {
            throw Exception("Failed to load JSON")
        }
    })

    fun getCurrentBand(code: String?) {
        if (code != null) {
            bandsService.getCurrentBand(code)?.enqueue(object : Callback<BandInfo> {
                override fun onResponse(call: Call<BandInfo>, response: Response<BandInfo>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        currentBand?.value = response.body()
                    }
                }

                override fun onFailure(call: Call<BandInfo>, t: Throwable) {
                    throw Exception("Failed to load JSON")
                }
            })
        }
    }


    fun resetViewModel() {
        bands?.value = emptyList()
        currentBand?.value = BandInfo("", emptyList(), 0, "", null)
    }

    fun getArrayListBands(): Array<String>? {
        return bands?.value?.map { bandCode -> bandCode.name }?.toTypedArray()
    }


}
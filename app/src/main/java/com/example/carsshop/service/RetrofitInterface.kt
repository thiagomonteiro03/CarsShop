package com.example.carsshop.service

import com.example.carsshop.model.CarModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    @GET(RetrofitConstants.URL)
    suspend fun getCars(@Query("Page") page:Int) : Response<List<CarModel?>>

}
package com.example.carsshop.service

import com.example.carsshop.model.CarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class CarRepository {

    suspend fun getApiData(): Response<List<CarModel?>> {
         return withContext(Dispatchers.Default) {
            val retrofitClient = RetrofitUtils
                .getRetrofitInstance(RetrofitConstants.URL)
            val endpoint = retrofitClient.create(RetrofitInterface::class.java)
            endpoint.getCars(3)
        }
    }
}
package com.example.carsshop.service

import com.example.carsshop.model.CarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface CarRepository {

    suspend fun getApiData(page: Int): Response<List<CarModel?>> {
         return Response.success(listOf())
    }
}
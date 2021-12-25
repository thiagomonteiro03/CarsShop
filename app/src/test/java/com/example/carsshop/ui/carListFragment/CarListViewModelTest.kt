package com.example.carsshop.ui.carListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import com.example.carsshop.service.RetrofitConstants
import com.example.carsshop.service.RetrofitInterface
import com.example.carsshop.service.RetrofitUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

class CarListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CarListViewModel

    @Test
    fun `when view model getCars get success then sets cars`(){
        // Arrange
        val cars = listOf(
            CarModel(1, "Honda", "City", "3.0", "link", 1 ,"30000,00" ,1 ,1 ,"white")
        )
        val repository = MockRepository(Response.success(cars))
        viewModel = CarListViewModel(repository)

        // Act

        // Assert
    }
}

class MockRepository(private val response: Response<List<CarModel?>>) : CarRepository {

    override suspend fun getApiData(page: Int): Response<List<CarModel?>> {
        return response
    }

}
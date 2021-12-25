package com.example.carsshop.ui.carListFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CarListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var carsObserver: Observer<ArrayList<CarModel>>

    private lateinit var viewModel: CarListViewModel

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun `when view model getCars get success then sets cars`(){
        // Arrange
        val actualCars = arrayListOf(
            CarModel(1, "Honda", "Civic", "3.0", "link", 1 ,"60000,00" ,1 ,1 ,"black")
        )
        val cars = listOf(
            CarModel(2, "Honda", "City", "3.0", "link", 1 ,"30000,00" ,1 ,1 ,"white")
        )
        actualCars.add(cars[0])

        val repository = MockRepository(Response.success(cars))
        viewModel = CarListViewModel(repository)
        viewModel.cars.observeForever(carsObserver)

        // Act
        runBlocking {
            viewModel.getCars(actualCars, 2)
        }

        // Assert
        verify(carsObserver).onChanged(actualCars)
    }
}

class MockRepository(private val response: Response<List<CarModel?>>) : CarRepository {

    override suspend fun getApiData(page: Int): Response<List<CarModel?>> {
        return response
    }

}
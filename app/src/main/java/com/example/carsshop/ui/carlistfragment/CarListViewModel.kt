package com.example.carsshop.ui.carlistfragment

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import kotlinx.coroutines.launch

class CarListViewModel(private val repository: CarRepository) : ViewModel() {

    private val _cars = MutableLiveData<ArrayList<CarModel>>()
    val cars : LiveData<ArrayList<CarModel>>
        get() = _cars

    val loading = MutableLiveData<Boolean>()

    val error : MutableLiveData<Boolean> = MutableLiveData()

    fun loadCars(carList : ArrayList<CarModel>, page: Int) {
        loading.value = true

        viewModelScope.launch {
            getCars(carList, page)
        }
    }

    suspend fun getCars(carList: ArrayList<CarModel>, page: Int){
        repository.getApiData(page).let {
            var carListApi: List<CarModel>? = null
            if (it.isSuccessful){
                carListApi = it.body() as List<CarModel>?
            } else if (carListApi == null)error.value = true

            if(!carListApi.isNullOrEmpty()) {
                carList.addAll(carListApi)
                _cars.value = carList
            }

            loading.value = false
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("detailImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().fitCenter())
                .into(view)
        }
    }

    class CarListViewModelFactory(
        private val repository: CarRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CarListViewModel(repository) as T
        }
    }

}
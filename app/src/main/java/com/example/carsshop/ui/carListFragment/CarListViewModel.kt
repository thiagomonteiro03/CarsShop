package com.example.carsshop.ui.carListFragment

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarListViewModel(private val repository: CarRepository) : ViewModel() {

    private val _cars = MutableLiveData<List<CarModel>>()
    val cars : LiveData<List<CarModel>>
        get() = _cars

    val loading = MutableLiveData<Boolean>()

    fun loadCars() {
        loading.value = true
        var carList: List<CarModel>? = null
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.getApiData()
            withContext(Dispatchers.Default) {
                if (response.isSuccessful){
                    carList = response.body() as List<CarModel>?
                }
            }
            loading.value = false
            _cars.value = carList?: listOf()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().circleCrop())
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
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

    private val _cars = MutableLiveData<ArrayList<CarModel>>()
    val cars : LiveData<ArrayList<CarModel>>
        get() = _cars

    val loading = MutableLiveData<Boolean>()

    fun loadCars(carList : ArrayList<CarModel>, page: Int) {
        loading.value = true
        var carListApi: List<CarModel>? = null
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.getApiData(page)
            withContext(Dispatchers.Default) {
                if (response.isSuccessful){
                    carListApi = response.body() as List<CarModel>?
                }
            }
            if(carListApi != null) {
                carList.addAll(carListApi!!)
                _cars.value = carList
            }
            loading.value = false
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().centerCrop())
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
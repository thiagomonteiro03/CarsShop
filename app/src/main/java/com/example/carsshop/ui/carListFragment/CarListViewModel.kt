package com.example.carsshop.ui.carListFragment

import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.carsshop.R
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

    val error = MutableLiveData<Boolean>()

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
            if(carListApi != null && carListApi!!.size == 10) {
                carList.addAll(carListApi!!)
                _cars.value = carList
            } else if (carListApi == null)error.value = true
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
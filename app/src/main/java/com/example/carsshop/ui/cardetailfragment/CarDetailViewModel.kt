package com.example.carsshop.ui.cardetailfragment

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CarDetailViewModel : ViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().centerCrop())
                .into(view)
        }
    }

}
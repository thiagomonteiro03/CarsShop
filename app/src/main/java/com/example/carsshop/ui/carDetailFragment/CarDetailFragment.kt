package com.example.carsshop.ui.carDetailFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carsshop.R

class CarDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CarDetailFragment()
    }

    private lateinit var viewModel: CarDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
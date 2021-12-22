package com.example.carsshop.ui.CarListFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carsshop.databinding.CarsListFragmentBinding
import com.example.carsshop.model.CarModel

class CarListFragment : Fragment() {

    private var adapter: CarListAdapter? = null
    private var binding: CarsListFragmentBinding? = null
    private var carList: ArrayList<CarModel> = arrayListOf(
        CarModel(
        1,
        "Corsa",
        "Corsa Hetch",
        "1.0",
        "http://desafioonline.webmotors.com.br/content/img/02.jpg",
        47500.0,
        49000.00,
        1,
        1,
        "Azul"),
        CarModel(
            2,
            "Corsao",
            "Corsa Hetchzao",
            "1.0",
            "http://desafioonline.webmotors.com.br/content/img/01.jpg",
            47500.0,
            49000.00,
            1,
            1,
            "Preto")
    )

    private lateinit var viewModel: CarListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CarsListFragmentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding?.recyclerBooks?.let {
            this.adapter = CarListAdapter(carList, viewModel)
            it.adapter = this.adapter
        } ?: throw AssertionError()
    }

}
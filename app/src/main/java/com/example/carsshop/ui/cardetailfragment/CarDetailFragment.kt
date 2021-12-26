package com.example.carsshop.ui.cardetailfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.carsshop.databinding.CarDetailFragmentBinding

class CarDetailFragment : Fragment() {

    private lateinit var viewModel: CarDetailViewModel
    private var binding: CarDetailFragmentBinding? = null

    private val args: CarDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CarDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CarDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.carModel.let {
            binding?.carModel = it
        }
    }

}
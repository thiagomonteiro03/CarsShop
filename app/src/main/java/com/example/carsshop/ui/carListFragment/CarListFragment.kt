package com.example.carsshop.ui.carListFragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carsshop.R
import com.example.carsshop.databinding.CarsListFragmentBinding
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import kotlinx.android.synthetic.main.cars_list_fragment.*


class CarListFragment : Fragment() {

    private var adapter: CarListAdapter? = null
    private var binding: CarsListFragmentBinding? = null
    private var carList: ArrayList<CarModel> = arrayListOf()

    private lateinit var viewModel: CarListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.list_car_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CarsListFragmentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            CarListViewModel.CarListViewModelFactory(CarRepository()))[CarListViewModel::class.java]

        viewModel.loadCars()

        viewModel.cars.observe(viewLifecycleOwner, {
//            adapter?.setEvents(it)
            val carListAdapter = CarListAdapter(it, viewModel).apply {
//                onItemClick = { event ->
//                    val directions = EventListFragmentDirections.actionEventListFragmentToEventFragment()
//                    findNavController().navigateWithAnimations(directions)
//                }
            }

            with(recyclerBooks) {
                setHasFixedSize(true)
                adapter = carListAdapter
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding!!.progressBarHome.visibility = View.VISIBLE
            } else {
                binding!!.progressBarHome.visibility = View.GONE
            }
        })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding?.recyclerBooks?.let {
            this.adapter = CarListAdapter(carList, viewModel)
            it.adapter = this.adapter
        } ?: throw AssertionError()
    }

}
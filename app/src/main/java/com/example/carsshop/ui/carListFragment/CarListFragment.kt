package com.example.carsshop.ui.carListFragment

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carsshop.R
import com.example.carsshop.databinding.CarsListFragmentBinding
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import kotlinx.android.synthetic.main.cars_list_fragment.*

import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsshop.utils.navigateWithAnimations
import java.util.*
import kotlin.collections.ArrayList


class CarListFragment : Fragment() {

    private var adapter: CarListAdapter? = null
    private var binding: CarsListFragmentBinding? = null
    private var carList: ArrayList<CarModel> = arrayListOf()
    private var tempCarList: ArrayList<CarModel> = arrayListOf()
    private var position: Int = 1

    private lateinit var layoutManager: LinearLayoutManager
    var initialSkip = 0
    var initialTop = 10

    private lateinit var viewModel: CarListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutManager = LinearLayoutManager(context)
        binding = CarsListFragmentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            CarListViewModel.CarListViewModelFactory(CarRepository()))[CarListViewModel::class.java]

        viewModel.loadCars(tempCarList, position)

        viewModel.cars.observe(viewLifecycleOwner, {
            carList.addAll(it)
            val carListAdapter = CarListAdapter(it, viewModel).apply {
                onItemClick = { carModel ->
                    val directions = CarListFragmentDirections.actionCarsListFragmentToCarDetailFragment(carModel)
                    findNavController().navigateWithAnimations(directions)
                }
            }

            if (position == 1) {
                with(recyclerBooks) {
                    setHasFixedSize(false)
                    adapter = carListAdapter
                }
            } else {
                refreshAdapter()
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

//    fun filter(text: String, cars: ArrayList<CarModel>): ArrayList<CarModel> {
//        val temp = ArrayList<CarModel>()
//        for (d in cars) {
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if (d.toString().contains(text.to)) {
//                temp.add(d)
//            }
//        }
//        return temp
//    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding?.recyclerBooks?.let {
            this.adapter = CarListAdapter(tempCarList, viewModel)
            it.adapter = this.adapter
            it.addOnScrollListener(recyclerViewOnScrollListener)
            it.layoutManager = layoutManager
        } ?: throw AssertionError()
    }

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= initialTop) {
                        position++
                        viewModel.loadCars(tempCarList, position)
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.list_car_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Pesquise algum modelo..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                tempCarList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    carList.forEach {
                        if (it.toString().lowercase(Locale.getDefault()).contains(searchText)){
                            tempCarList.add(it)
                        }
                    }
                    refreshAdapter()
                } else {
                    tempCarList.addAll(carList)
                    refreshAdapter()
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    fun refreshAdapter(){
        with(recyclerBooks) {
            adapter?.notifyDataSetChanged()
        }
    }

}
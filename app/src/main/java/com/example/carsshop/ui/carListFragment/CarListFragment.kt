package com.example.carsshop.ui.carListFragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carsshop.R
import com.example.carsshop.databinding.CarsListFragmentBinding
import com.example.carsshop.model.CarModel
import com.example.carsshop.service.CarRepository
import com.example.carsshop.service.CarRepositoryClass
import com.example.carsshop.utils.navigateWithAnimations
import kotlinx.android.synthetic.main.cars_list_fragment.*
import java.util.*
import android.net.ConnectivityManager

import android.net.NetworkInfo

import android.net.NetworkCapabilities

import android.os.Build
import android.os.Handler
import okio.Timeout
import kotlin.concurrent.schedule


class CarListFragment : Fragment() {

    private var adapter: CarListAdapter? = null
    private var binding: CarsListFragmentBinding? = null
    private var carList: ArrayList<CarModel> = arrayListOf()
    private var tempCarList: ArrayList<CarModel> = arrayListOf()
    private var position: Int = 1

    private lateinit var layoutManager: LinearLayoutManager
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
        val repositoryClass = CarRepositoryClass()
        viewModel = ViewModelProvider(
            this,
            CarListViewModel.CarListViewModelFactory(repositoryClass))[CarListViewModel::class.java]

        loadItems()
        setSwipeRefresh()
        setObserversItems()
    }

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
        searchView.queryHint = R.string.search_hint.toString()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                //Local search logic created just for example because API don`t have a support to search from get
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
                    tempCarList.clear()
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

    fun isInternetConnected(getApplicationContext: Context): Boolean {
        var status = false
        val cm =
            getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                // connected to the internet
                status = true
            }
        }
        return status
    }

    fun setSwipeRefresh(){
        binding?.swiperefresh.let {
            it!!.setOnRefreshListener {
                loadItems()
            }
        }
    }

    fun loadItems(){
        if(isInternetConnected(requireContext())){
            viewModel.loadCars(tempCarList, position)
            binding?.clWifiOff?.visibility = View.GONE
            Timer().schedule(2000) {
                binding?.swiperefresh?.isRefreshing = false
            }

        }
        else {
            if(carList.isNullOrEmpty()){
                binding?.clWifiOff?.visibility = View.VISIBLE
            }
            binding?.swiperefresh?.isRefreshing = false
        }
    }

    fun setObserversItems(){
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
            binding?.swiperefresh?.isRefreshing = false
        })

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding!!.progressBarHome.visibility = View.VISIBLE
            } else {
                binding!!.progressBarHome.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            if (it) Toast.makeText(requireContext(), R.string.any_error_occurrent, Toast.LENGTH_LONG).show()
        })
    }

}
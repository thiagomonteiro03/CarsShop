package com.example.carsshop.ui.carListFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carsshop.databinding.CarItemBinding
import com.example.carsshop.model.CarModel

class CarListAdapter(
    private var cars: List<CarModel>,
    private val viewModel: CarListViewModel,
) : RecyclerView.Adapter<ViewHolder>() {

    var onItemClick: ((entity: CarModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCars(cars: List<CarModel>) {
        this.cars = cars
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carModel = cars[holder.adapterPosition]
        holder.bind(carModel,viewModel)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(carModel)
        }
    }
}

class ViewHolder constructor(val binding: CarItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CarModel, viewModel: CarListViewModel) {
        binding.carModel = item
        binding.viewModel = viewModel
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CarItemBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }
}
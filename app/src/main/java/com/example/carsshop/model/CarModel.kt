package com.example.carsshop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarModel(
    val ID : Long,
    val Make: String,
    val Model: String,
    val Version : String,
    val Image : String,
    val KM : Double,
    val Price : String,
    val YearModel : Int,
    val YearFab : Int,
    val Color : String) : Parcelable

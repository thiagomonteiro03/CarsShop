package com.example.carsshop.utils

open class Util {

    companion object {

        @JvmStatic
        fun getRealCurrency(valor: String?): String {
            return "R\$ $valor"
        }

        @JvmStatic
        fun changeLinkToHttps(valor: String?): String {
            return valor?.replace("http", "https") ?: ""
        }
    }
}
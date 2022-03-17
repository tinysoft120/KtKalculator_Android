package com.midstatesrecycling.ktkalculator.network

import java.io.IOException

class NoInternetException: IOException() {

    override val message: String
        get() = "No internet exception"
}
package com.midstatesrecycling.ktkalculator.network

/**
 * Generic class that holds the network state
 */
sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    object Loading : ApiResult<Nothing>()
    data class Error(val error: Exception) : ApiResult<Nothing>()
}
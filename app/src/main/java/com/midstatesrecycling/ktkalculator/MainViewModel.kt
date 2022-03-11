package com.midstatesrecycling.ktkalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midstatesrecycling.ktkalculator.network.ApiResult
import com.midstatesrecycling.ktkalculator.network.GoogleServer
import com.midstatesrecycling.ktkalculator.repository.RealRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val repository: RealRepository,
    private val googleServer: GoogleServer
) : ViewModel() {
    private val _karatValue = MutableLiveData<Float>()
    private val _karatApiValue = MutableLiveData<ApiResult<Float>>()
    private val _netConnection = MutableLiveData<Boolean>()

    val karatValue: LiveData<Float> = _karatValue
    val karatApiValue: LiveData<ApiResult<Float>> = _karatApiValue
    val netConnection: LiveData<Boolean> = _netConnection

    init {
        loadNetworkContent()
    }

    private fun loadNetworkContent() = viewModelScope.launch(IO) {
        fetchNetworkState()
        fetchKaratApiValue()
    }

    fun fetchNetworkState() {
        viewModelScope.launch(IO) {
            googleServer.connectToGoogle().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val value = response.body()
                    _netConnection.postValue(value != null)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _netConnection.postValue(false)
                }
            })
        }
    }

    fun fetchKaratApiValue() {
        viewModelScope.launch(IO) {
            _karatApiValue.postValue(ApiResult.Loading)
            val apiValue = repository.karatValue()
            _karatApiValue.postValue(apiValue)
            withContext(Main) {
                when (apiValue) {
                    is ApiResult.Success -> {
                        // TODO: check now Manual Mode
                        _karatValue.postValue(apiValue.data!!)
                    }
                    else -> {}
                }
            }
        }
    }
}
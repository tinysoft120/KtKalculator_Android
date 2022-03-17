package com.midstatesrecycling.ktkalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midstatesrecycling.ktkalculator.model.CalcResult
import com.midstatesrecycling.ktkalculator.network.ApiResult
import com.midstatesrecycling.ktkalculator.network.NoConnectivityException
import com.midstatesrecycling.ktkalculator.network.NoInternetException
import com.midstatesrecycling.ktkalculator.repository.RealRepository
import com.midstatesrecycling.ktkalculator.util.LogU
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: RealRepository
) : ViewModel() {
    private val _karatValue = MutableLiveData<Double>()
    private val _karatApiValue = MutableLiveData<ApiResult<Double>>()
    private val _netConnection = MutableLiveData<Boolean>()
    private val _calcResult = MutableLiveData<CalcResult>()

    val karatValue: LiveData<Double> = _karatValue
    val karatApiValue: LiveData<ApiResult<Double>> = _karatApiValue
    val netConnection: LiveData<Boolean> = _netConnection
    val calcResult: LiveData<CalcResult> = _calcResult

    init {
        loadNetworkContent()
    }

    private fun loadNetworkContent() = viewModelScope.launch(IO) {
        fetchKaratApiValue()
    }

    fun fetchKaratApiValue() {
        viewModelScope.launch(IO) {
            _karatApiValue.postValue(ApiResult.Loading)
            val apiValue = repository.karatValue()
            _karatApiValue.postValue(apiValue)
            //withContext(Main) {
            when (apiValue) {
                is ApiResult.Success -> {
                    // TODO: check now Manual Mode
                    _netConnection.postValue(true)
                    _karatValue.postValue(apiValue.data!!)
                }
                is ApiResult.Error -> {
                    LogU.e("net", apiValue.error.message ?: "")
                    when (apiValue.error) {
                        is NoInternetException -> {
                            _netConnection.postValue(false)
                        }
                        is NoConnectivityException -> {
                            _netConnection.postValue(false)
                        }
                        else -> { }
                    }
                }
                ApiResult.Loading -> { }
            }
            //}
        }
    }

    fun updateKaratValue(goldRate: Double) {
        viewModelScope.launch(IO) {
            _karatValue.postValue(goldRate)
        }
    }

    fun updateCalcResult(result: CalcResult) {
        viewModelScope.launch(IO) {
            _calcResult.postValue(result)
        }
    }
}
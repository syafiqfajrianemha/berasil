package com.berasil.ui.price

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.berasil.data.remote.response.CheckPriceResponse
import com.berasil.data.remote.response.DataItem
import com.berasil.data.repository.BiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCheckPriceViewModel(private val biRepository: BiRepository) : ViewModel() {

    private val _ricePrice = MutableLiveData<List<DataItem>>()
    val ricePrice: LiveData<List<DataItem>> = _ricePrice

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun checkRicePrice(tanggal: String, commodity: String, priceType: Int, provId: Int) {
        _isLoading.value = true

        val client = biRepository.getRicePrice(tanggal, commodity, priceType, provId)

        client.enqueue(object : Callback<CheckPriceResponse> {
            override fun onResponse(
                call: Call<CheckPriceResponse>,
                response: Response<CheckPriceResponse>
            ) {
                if (response.isSuccessful) {
                    _ricePrice.postValue(response.body()?.data)
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CheckPriceResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = DialogCheckPriceViewModel::class.java.simpleName
    }
}
package com.project.testapplication

import android.app.Application
import android.content.Context
import android.icu.text.CaseMap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {


    suspend fun getListData(): MutableLiveData<NetworkResult<ItemRow>> {
        var listData = MutableLiveData<NetworkResult<ItemRow>>()
        viewModelScope.launch {
            listData.value = NetworkResult.Loading()
            if (hasInternetConnection()) {
                try {
                    val response = ListRepository().getListData()
                    listData.value =
                        NetworkResponse<ItemRow>().handleNetworkResponse(response)


                } catch (e: Exception) {
                    listData.value = NetworkResult.Error("Not found.")
                }
            } else {
                listData.value = NetworkResult.Error("No Internet Connection.")
            }
        }
        return listData
    }

    suspend fun postListData(title: String,desc:String): MutableLiveData<NetworkResult<String>> {
        var listData = MutableLiveData<NetworkResult<String>>()

        viewModelScope.launch {
            listData.value = NetworkResult.Loading()
            if (hasInternetConnection()) {
                try {
                    val response = ListRepository().postListData(title, desc)
                    listData.value =
                        NetworkResponse<String>().handleNetworkResponse(response)


                } catch (e: Exception) {
                    listData.value = NetworkResult.Error("Not found.")
                }
            } else {
                listData.value = NetworkResult.Error("No Internet Connection.")
            }
        }
        return listData
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
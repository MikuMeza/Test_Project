package com.project.testapplication

import retrofit2.Response

class NetworkResponse<T> {
    fun handleNetworkResponse(response: Response<T>): NetworkResult<T>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code()==500->{
                return NetworkResult.Error("Not Successfull")
            }
            response.body()==null -> {
                return NetworkResult.Error("Result not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}
package com.project.testapplication

import retrofit2.Response
import retrofit2.http.*

interface ListService {
    @GET
    suspend fun getListData(
        @Url url: String = ListLink
    ): Response<ItemRow>

    @POST
    @Headers("Content-Type: application/json; charset=UTF-8")
    suspend fun postListData(
        @Url url: String = ListLink,@Query("title") title:String,@Query("desc") desc:String
    ): Response<Boolean>

    companion object {
        operator fun invoke(): ListService {
            return RetrofitObject.getRetrofitInstance().create(ListService::class.java)
        }
    }
}
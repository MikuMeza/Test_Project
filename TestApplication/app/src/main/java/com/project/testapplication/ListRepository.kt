package com.project.testapplication

import retrofit2.Response

class ListRepository {
    suspend fun getListData(): Response<ItemRow> {
        return ListService().getListData()
    }

    suspend fun postListData(title:String,desc:String): Response<Boolean> {
        return ListService().postListData(title = title,desc = desc)
    }
}
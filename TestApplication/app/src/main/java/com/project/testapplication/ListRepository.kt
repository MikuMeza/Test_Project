package com.project.testapplication

import com.google.gson.Gson
import retrofit2.Response

class ListRepository {
    suspend fun getListData(): Response<ItemRow> {
        return ListService().getListData()
    }

    suspend fun postListData(title:String,desc:String): Response<String> {
        var postData=PostData(title, desc)
        return ListService().postListData(param = Gson().toJson(postData))
    }
}
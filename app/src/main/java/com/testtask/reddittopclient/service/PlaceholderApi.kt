package com.testtask.reddittopclient.service

import com.testtask.reddittopclient.PlaceholderEntries
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceholderApi{

    @GET("top.json")
    fun getEntriesAsync(@Query("limit") limit: Int) : Deferred<Response<PlaceholderEntries>>

    @GET("top.json")
    fun getNextEntriesAsync(@Query("limit") limit: Int, @Query("after") after: String) : Deferred<Response<PlaceholderEntries>>
}
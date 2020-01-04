package com.testtask.reddittopclient.service

import com.testtask.reddittopclient.AppConstants.JSON_PLACEHOLDER_BASE_URL

object ApiFactory{

    val placeholderApi : PlaceholderApi = RetrofitFactory.retrofit(JSON_PLACEHOLDER_BASE_URL)
        .create(PlaceholderApi::class.java)
}
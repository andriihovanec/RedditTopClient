package com.testtask.reddittopclient.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.testtask.reddittopclient.models.Children

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface EntriesView : MvpView {
    fun startLoading()
    fun endLoading()
    fun showError(textResource: Int)
    fun setUpEmptyList()
    fun setUpEntriesList(entries: List<Children>)
}
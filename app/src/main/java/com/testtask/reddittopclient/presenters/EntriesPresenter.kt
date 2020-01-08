package com.testtask.reddittopclient.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.testtask.reddittopclient.AppConstants.LIMIT
import com.testtask.reddittopclient.R
import com.testtask.reddittopclient.models.PlaceholderEntries
import com.testtask.reddittopclient.providers.EntriesProvider
import com.testtask.reddittopclient.views.EntriesView

@InjectViewState
class EntriesPresenter : MvpPresenter<EntriesView>() {

    private lateinit var mAfter: String
    private var mEntriesCount: Int = 0

    fun loadEntries() {
        viewState.startLoading()
        EntriesProvider(presenter = this).loadEntries(LIMIT)
    }

    fun loadNextEntries() {
        if (mEntriesCount > 50) viewState.showSnack(R.string.limit_reached)
        else EntriesProvider(presenter = this).loadNextEntries(LIMIT, mAfter)
    }

    fun entriesLoaded(entries: PlaceholderEntries) {
        mAfter = entries.data.after
        mEntriesCount = entries.data.children.size
        viewState.endLoading()
        viewState.setUpEntriesList(entries = entries.data.children)
    }

    fun nextEntriesLoaded(entries: PlaceholderEntries) {
        mAfter = entries.data.after
        mEntriesCount += entries.data.children.size
        viewState.addNextEntriesList(entries = entries.data.children)
    }

    fun errorLoadedEntries() {
        viewState.endLoading()
        viewState.setUpEmptyList()
        viewState.showError(textResource = R.string.error_message)
    }
}
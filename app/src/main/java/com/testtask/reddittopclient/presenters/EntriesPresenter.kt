package com.testtask.reddittopclient.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.testtask.reddittopclient.AppConstants.LIMIT
import com.testtask.reddittopclient.models.PlaceholderEntries
import com.testtask.reddittopclient.providers.EntriesProvider
import com.testtask.reddittopclient.views.EntriesView

@InjectViewState
class EntriesPresenter : MvpPresenter<EntriesView>() {

    private var mAfter: String = ""

    fun loadEntries() {
        viewState.startLoading()
        EntriesProvider(presenter = this).loadEntries(LIMIT)
    }

    fun entriesLoaded(entries: PlaceholderEntries) {
        viewState.endLoading()
        viewState.setUpEntriesList(entries = entries.data.children)
        mAfter = entries.data.after
    }
}
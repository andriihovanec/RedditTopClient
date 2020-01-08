package com.testtask.reddittopclient.providers

import android.util.Log
import com.testtask.reddittopclient.AppConstants.TAG_ENTRIES_RESPONSE
import com.testtask.reddittopclient.presenters.EntriesPresenter
import com.testtask.reddittopclient.service.ApiFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EntriesProvider(var presenter: EntriesPresenter) {

    private val service = ApiFactory.placeholderApi
    private var parentJob: Job = Job()
    private var backgroundContext: CoroutineContext = Dispatchers.IO
    private var foregroundContext: CoroutineContext = Dispatchers.Main

    private fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }

    fun loadEntries(limit: Int) {
        unsubscribe()
        parentJob = Job()
        CoroutineScope(backgroundContext + parentJob).launch {
            val postRequest = service.getEntriesAsync(limit)
            try {
                val response = postRequest.await()
                withContext(foregroundContext) {
                    if (response.isSuccessful) {
                        val entries = response.body()
                        entries?.let {
                            presenter.entriesLoaded(it)
                            Log.d(TAG_ENTRIES_RESPONSE, "success" + entries.data.children.size)
                        }
                    } else {
                        presenter.errorLoadedEntries()
                        Log.d(TAG_ENTRIES_RESPONSE, response.errorBody().toString())
                    }
                }
            } catch (e: Exception){
                presenter.errorLoadedEntries()
                Log.d(TAG_ENTRIES_RESPONSE, e.toString())
            }
        }
    }

    fun loadNextEntries(limit: Int, after: String) {
        unsubscribe()
        parentJob = Job()
        CoroutineScope(backgroundContext + parentJob).launch {
            val postRequest = service.getNextEntriesAsync(limit, after)
            try {
                val response = postRequest.await()
                withContext(foregroundContext) {
                    if (response.isSuccessful) {
                        val entries = response.body()
                        entries?.let {
                            presenter.nextEntriesLoaded(entries)
                        }
                        Log.d(TAG_ENTRIES_RESPONSE, "success" + entries!!.data.children.size)
                    } else {
                        presenter.errorLoadedEntries()
                        Log.d(TAG_ENTRIES_RESPONSE, response.errorBody().toString())
                    }
                }
            } catch (e: Exception){
                presenter.errorLoadedEntries()
                Log.d(TAG_ENTRIES_RESPONSE, e.toString())
            }
        }
    }
}
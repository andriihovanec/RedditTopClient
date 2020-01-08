package com.testtask.reddittopclient.listeners

import com.testtask.reddittopclient.models.Children

interface OnItemClickListener {
    fun onItemClicked(entry: Children)
}
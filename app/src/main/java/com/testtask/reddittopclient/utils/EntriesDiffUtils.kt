package com.testtask.reddittopclient.utils

import androidx.recyclerview.widget.DiffUtil
import com.testtask.reddittopclient.models.Children

class EntriesDiffUtils(private val oldList: ArrayList<Children?>, private val newList: List<Children>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]!!.data.id == newList[newItemPosition].data.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
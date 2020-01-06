package com.testtask.reddittopclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testtask.reddittopclient.R
import com.testtask.reddittopclient.models.Children
import kotlinx.android.synthetic.main.cell_entries.view.*

class EntriesAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSourceList: ArrayList<Children> = ArrayList()

    fun setupList(entriesList: List<Children>) {
        mSourceList.clear()
        mSourceList.addAll(entriesList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_entries, parent, false)
        return EntriesViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mSourceList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val children = mSourceList[position]
        if (holder is EntriesViewHolder) {
            holder.bind(entry = children, clickListener = itemClickListener)
        }
    }

    class EntriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //private var rating: TextView = itemView.findViewById(R.id.tv_rating)
        private var subreddit = itemView.tv_subreddit
        private var author: TextView = itemView.findViewById(R.id.tv_author)
        private var title: TextView = itemView.findViewById(R.id.tv_title)
        private var postTime: TextView = itemView.findViewById(R.id.tv_post_time)
        private var preview: ImageView = itemView.findViewById(R.id.iv_preview)

        fun bind(entry: Children, clickListener: OnItemClickListener) {
            //rating.text = entry.data.score.toString()
            subreddit.text = entry.data.subreddit
            author.text = entry.data.author
            title.text = entry.data.title
            if (entry.data.thumbnail == "default") {
                preview.visibility = View.GONE
            } else {
                Glide.with(itemView.context)
                    .load(entry.data.thumbnail)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(preview)
            }

            itemView.setOnClickListener {
                clickListener.onItemClicked(entry)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(entry: Children)
    }
}
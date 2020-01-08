package com.testtask.reddittopclient.adapters

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testtask.reddittopclient.AppConstants.VIEW_TYPE_ITEM
import com.testtask.reddittopclient.AppConstants.VIEW_TYPE_LOADING
import com.testtask.reddittopclient.R
import com.testtask.reddittopclient.listeners.OnItemClickListener
import com.testtask.reddittopclient.models.Children
import kotlinx.android.synthetic.main.cell_entries.view.*
import kotlinx.android.synthetic.main.cell_progress_loading.view.*

class EntriesAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mSourceList: ArrayList<Children?> = ArrayList()
    private lateinit var mContext: Context

    fun setupList(entriesList: List<Children>) {
        mSourceList.addAll(entriesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.cell_entries, parent, false)
            EntriesViewHolder(itemView = itemView)
        } else {
            val view = LayoutInflater.from(mContext).inflate(R.layout.cell_progress_loading, parent, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                view.progressbar.indeterminateDrawable.colorFilter = BlendModeColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorAccent), BlendMode.SRC_ATOP)
            } else {
                view.progressbar.indeterminateDrawable.setColorFilter(
                    ContextCompat.getColor(mContext, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
            }
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mSourceList.count()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mSourceList[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun addLoadingView() {
        //add loading item
        mSourceList.add(null)
        notifyItemInserted(mSourceList.size - 1)
    }

    fun removeLoadingView() {
        //Remove loading item
        if (mSourceList.size != 0) {
            mSourceList.removeAt(mSourceList.size - 1)
            notifyItemRemoved(mSourceList.size)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            val children = mSourceList[position]
            if (holder is EntriesViewHolder) {
                holder.bind(mContext, entry = children!!, clickListener = itemClickListener)
            }
        }
    }

    class EntriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var subreddit = itemView.tv_subreddit
        private var author = itemView.tv_author
        private var title = itemView.tv_title
        private var commentsCount = itemView.tv_comments_count
        private var score = itemView.tv_score
        private var preview = itemView.iv_preview

        fun bind(context: Context, entry: Children, clickListener: OnItemClickListener) {
            subreddit.text = context.resources.getString(R.string.subreddit, entry.data.subreddit)
            author.text = context.resources.getString(R.string.author, entry.data.author)
            title.text = entry.data.title
            commentsCount.text = context.resources.getString(R.string.comments, entry.data.num_comments.toString())
            score.text = context.resources.getString(R.string.score, entry.data.score.toString())

            Glide.with(itemView.context)
                .load(entry.data.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(preview)

            itemView.setOnClickListener {
                clickListener.onItemClicked(entry)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
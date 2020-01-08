package com.testtask.reddittopclient.fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.snackbar.Snackbar
import com.testtask.reddittopclient.AppConstants.KEY_URL
import com.testtask.reddittopclient.MainActivity
import com.testtask.reddittopclient.R
import com.testtask.reddittopclient.adapters.EntriesAdapter
import com.testtask.reddittopclient.helpers.ConnectionHelper
import com.testtask.reddittopclient.helpers.TabHelper
import com.testtask.reddittopclient.listeners.OnItemClickListener
import com.testtask.reddittopclient.listeners.OnLoadMoreListener
import com.testtask.reddittopclient.listeners.ScrollListener
import com.testtask.reddittopclient.models.Children
import com.testtask.reddittopclient.presenters.EntriesPresenter
import com.testtask.reddittopclient.utils.EntriesDiffUtils
import com.testtask.reddittopclient.views.EntriesView
import kotlinx.android.synthetic.main.fragment_entries.*

class EntriesFragment : MvpAppCompatFragment(), EntriesView, OnItemClickListener {

    @InjectPresenter
    lateinit var mEntriesPresenter: EntriesPresenter

    private lateinit var mAdapter: EntriesAdapter
    private lateinit var mRvEntries: RecyclerView
    private var mCustomTabHelper: TabHelper = TabHelper()
    private lateinit var mScrollListener: ScrollListener
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)
        mRvEntries = view.findViewById(R.id.rv_entries_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ConnectionHelper().isNetworkAvailable(requireContext()))
            mEntriesPresenter.loadEntries()
        else showSnack(R.string.no_internet_connection)
    }

    private fun setupRecyclerView() {
        mAdapter = EntriesAdapter(this)
        mRvEntries.adapter = mAdapter
        mLinearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRvEntries.layoutManager = mLinearLayoutManager
        mRvEntries.setHasFixedSize(true)
        mScrollListener = ScrollListener(
            mLinearLayoutManager
        )
        mScrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                if (ConnectionHelper().isNetworkAvailable(requireContext()))
                    loadMoreData()
                else showSnack(R.string.no_internet_connection)
            }
        })
        mRvEntries.addOnScrollListener(mScrollListener)
    }

    private fun loadMoreData() {
        mAdapter.addLoadingView()
        mEntriesPresenter.loadNextEntries()
    }

    private fun openChromeTab(entryUrl: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        builder.setStartAnimations(requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        val customTabsIntent = builder.build()

        // check is Сhrom available
        val packageName = mCustomTabHelper.getPackageNameToUse(requireContext(), entryUrl)
        if (packageName == null) {
            // if chrome not available open in web view
            val bundle = Bundle()
            bundle.putString(KEY_URL, entryUrl)
            findNavController().navigate(
                R.id.webViewFragment,
                bundle,
                (activity as MainActivity).getNavOptions()
            )
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(requireContext(), Uri.parse(entryUrl))
        }
    }

    override fun startLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun endLoading() {
        progress.visibility = View.GONE
    }

    override fun showError(textResource: Int) {
        showSnack(textResource)
    }

    override fun setUpEmptyList() {
        tv_empty_list.visibility = View.VISIBLE
    }

    override fun setUpEntriesList(entries: List<Children>) {
        setupRecyclerView()
        mAdapter.setupList(entriesList = entries)
    }

    override fun addNextEntriesList(entries: List<Children>) {
        Handler().postDelayed({
            mAdapter.removeLoadingView()
            val utils = EntriesDiffUtils(mAdapter.mSourceList, entries)
            val result = DiffUtil.calculateDiff(utils)
            mAdapter.setupList(entriesList = entries)
            mScrollListener.setLoaded()
            mRvEntries.post {
                result.dispatchUpdatesTo(mAdapter)
                mAdapter.notifyDataSetChanged()
            }
        }, 2000)
    }

    override fun showSnack(textResource: Int) {
        val snack = Snackbar.make(requireView(), resources.getString(textResource),
            Snackbar.LENGTH_LONG)
        snack.setActionTextColor(Color.WHITE)
        val snackView = snack.view
        snackView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        val textView =
            snackView.findViewById(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snack.show()
    }

    override fun onItemClicked(entry: Children) {
        openChromeTab(entry.data.url)
    }
}
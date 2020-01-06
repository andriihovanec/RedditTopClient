package com.testtask.reddittopclient.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.testtask.reddittopclient.AppConstants.KEY_URL
import com.testtask.reddittopclient.MainActivity
import com.testtask.reddittopclient.R
import com.testtask.reddittopclient.adapters.EntriesAdapter
import com.testtask.reddittopclient.helpers.TabHelper
import com.testtask.reddittopclient.models.Children
import com.testtask.reddittopclient.presenters.EntriesPresenter
import com.testtask.reddittopclient.views.EntriesView
import kotlinx.android.synthetic.main.fragment_entries.*

class EntriesFragment : MvpAppCompatFragment(), EntriesView, EntriesAdapter.OnItemClickListener {

    @InjectPresenter
    lateinit var entriesPresenter: EntriesPresenter

    private lateinit var mAdapter: EntriesAdapter
    private lateinit var mRvFriendsMessages: RecyclerView
    private var customTabHelper: TabHelper = TabHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)
        mRvFriendsMessages = view.findViewById(R.id.rv_entries_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entriesPresenter.loadEntries()

    }

    private fun setupRecyclerView() {
        mAdapter = EntriesAdapter(this)
        mRvFriendsMessages.adapter = mAdapter
        mRvFriendsMessages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mRvFriendsMessages.setHasFixedSize(true)
    }

    private fun openChromeTab(entryUrl: String) {
        val builder = CustomTabsIntent.Builder()

        // modify toolbar color
        builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        // add share button to overflow menu
        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        builder.setStartAnimations(requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(requireContext(), android.R.anim.fade_in, android.R.anim.fade_out)
        val customTabsIntent = builder.build()

        // check is Сhrom available
        val packageName = customTabHelper.getPackageNameToUse(requireContext(), entryUrl)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUpEmptyList() {
        tv_empty_list.visibility = View.VISIBLE
    }

    override fun setUpEntriesList(entries: List<Children>) {
        setupRecyclerView()
        mAdapter.setupList(entries)
    }

    override fun onItemClicked(entry: Children) {
        openChromeTab(entry.data.url)
    }
}
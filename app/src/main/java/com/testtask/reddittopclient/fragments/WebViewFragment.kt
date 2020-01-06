package com.testtask.reddittopclient.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.testtask.reddittopclient.AppConstants.KEY_URL
import com.testtask.reddittopclient.R
import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : MvpAppCompatFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(KEY_URL).let { wv_view.loadUrl(it) }
    }
}
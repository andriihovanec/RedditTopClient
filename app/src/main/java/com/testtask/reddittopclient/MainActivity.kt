package com.testtask.reddittopclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.testtask.reddittopclient.service.ApiFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(
            R.id.entriesFragment,
            null,
            getNavOptions()
        )
    }

    // animation options
    fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.fragment_fade_enter)
            .setExitAnim(R.anim.fragment_fade_exit)
            .setPopEnterAnim(R.anim.fragment_fade_enter)
            .setPopExitAnim(R.anim.fragment_fade_exit)
            .build()
    }
}

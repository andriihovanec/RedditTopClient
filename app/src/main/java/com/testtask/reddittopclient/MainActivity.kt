package com.testtask.reddittopclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment


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

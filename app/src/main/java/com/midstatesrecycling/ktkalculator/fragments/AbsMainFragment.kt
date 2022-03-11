package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.midstatesrecycling.ktkalculator.MainActivity
import com.midstatesrecycling.ktkalculator.MainViewModel
import com.midstatesrecycling.ktkalculator.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class AbsMainFragment(@LayoutRes layout: Int) : Fragment(layout) {
    val mainViewModel: MainViewModel by sharedViewModel()  // activityViewModels()

    val navOptions by lazy {
        navOptions {
            launchSingleTop = false
            anim {
                enter = R.anim.anim_fragment_open_enter
                exit = R.anim.anim_fragment_open_exit
                popEnter = R.anim.anim_fragment_close_enter
                popExit = R.anim.anim_fragment_close_exit
            }
        }
    }

    val mainActivity: MainActivity
        get() = activity as MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        Log.d(javaClass.simpleName, "onActivityCreated:")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(javaClass.simpleName, "onViewCreated:")
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.simpleName, "onResume:")
    }

    override fun onStop() {
        super.onStop()
        Log.d(javaClass.simpleName, "onStop:")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(javaClass.simpleName, "onDestroyView:")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "onDestroy:")
    }
}
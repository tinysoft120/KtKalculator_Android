package com.midstatesrecycling.ktkalculator.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.midstatesrecycling.ktkalculator.MainActivity
import com.midstatesrecycling.ktkalculator.MainViewModel
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.extensions.colorButtons
import com.midstatesrecycling.ktkalculator.util.LogU
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

    protected open fun hideKeyboard() {
        try {
            val parent = activity ?: return
            val v = parent.currentFocus ?: return
            val imm = parent.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                v.clearFocus()
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun showAlert(title: String, message: String, callback: (()->Unit)? = null ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> callback?.let { it() } }
            .create()
            .colorButtons()
            .show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        LogU.d(javaClass.simpleName, "onActivityCreated:")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LogU.d(javaClass.simpleName, "onViewCreated:")
    }

    override fun onResume() {
        super.onResume()
        LogU.d(javaClass.simpleName, "onResume:")
    }

    override fun onStop() {
        super.onStop()
        LogU.d(javaClass.simpleName, "onStop:")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogU.d(javaClass.simpleName, "onDestroyView:")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogU.d(javaClass.simpleName, "onDestroy:")
    }
}
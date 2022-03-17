package com.midstatesrecycling.ktkalculator.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.midstatesrecycling.ktkalculator.BuildConfig
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.databinding.FragmentAboutBinding

class AboutFragment : AbsMainFragment(R.layout.fragment_about) {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val tabKaratCalculator get() = binding.tabKaratCalculator
    private val tabMidStates get() = binding.tabMidStates
    private val webView get() = binding.webView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)

        tabKaratCalculator.setOnClickListener {
            tabKaratCalculator.isChecked = true
            tabMidStates.isChecked = false

            webView.loadUrl("file:///android_asset/page_01.html")
        }
        tabMidStates.setOnClickListener {
            tabMidStates.isChecked = true
            tabKaratCalculator.isChecked = false

            webView.loadUrl("file:///android_asset/page_02.html")
        }

        tabKaratCalculator.isChecked = true
        tabMidStates.isChecked = false
        webView.loadUrl("file:///android_asset/page_01.html")
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
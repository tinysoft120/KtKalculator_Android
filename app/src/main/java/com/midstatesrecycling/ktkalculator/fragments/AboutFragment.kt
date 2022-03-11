package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.View
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.databinding.FragmentAboutBinding

class AboutFragment : AbsMainFragment(R.layout.fragment_about) {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val tabKaratCalculator get() = binding.tabKaratCalculator
    private val tabMidStates get() = binding.tabMidStates
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)

        tabKaratCalculator.setOnClickListener {
            tabKaratCalculator.isChecked = true
            tabMidStates.isChecked = false
        }
        tabMidStates.setOnClickListener {
            tabMidStates.isChecked = true
            tabKaratCalculator.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.extensions.hidden
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.extensions.show
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.adapter.EstimateItem
import com.midstatesrecycling.ktkalculator.adapter.EstimateTableAdapter
import com.midstatesrecycling.ktkalculator.databinding.FragmentKalculatorBinding
import com.midstatesrecycling.ktkalculator.databinding.ItemDropCalcBinding
import com.midstatesrecycling.ktkalculator.logic.Logic
import com.midstatesrecycling.ktkalculator.logic.MeasureUnit


class CalculatorFragment : AbsMainFragment(R.layout.fragment_kalculator) {
    private var _binding: FragmentKalculatorBinding? = null
    private val binding get() = _binding!!

    private val tvGoldPrice: TextView           get() = binding.tvGoldPrice
    private val tvMeasurementValue: TextView    get() = binding.tvMeasurementValue
    private val overlay: View                   get() = binding.vOverlay
    private val dropDownContainer: LinearLayout get() = binding.llDropdown

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKalculatorBinding.bind(view)

        overlay.setOnClickListener { showDropDown(false) }
        binding.btnMeasure.setOnClickListener { showDropDown(!overlay.isVisible) }
        binding.btnCalculate.setOnClickListener {  }
        binding.btnClear.setOnClickListener {  }
        tvGoldPrice.text = String.format("%.2f", 0f)
        tvMeasurementValue.text = Logic.currentMeasureUnit.unitName

        val adapter = EstimateTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)

        val estimatesItems = listOf(
            EstimateItem.DataItem(0, 0.44123f, "10 Karat", 0f),
            EstimateItem.DataItem(1, 0.12f, "12 Karat", 0f),
            EstimateItem.DataItem(2, 10.1f, "14 Karat", 0f),
            EstimateItem.DataItem(3, 4.132312f, "16 Karat", 0f),
            EstimateItem.DataItem(4, 2.31f, "18 Karat", 0f),
            EstimateItem.DataItem(5, 4.1f, "22 Karat", 0f),
            EstimateItem.DataItem(6, 6.234f, "24 Karat", 0f),
            EstimateItem.DataItem(7, 6.234f, "24 Karat", 0f),
            EstimateItem.DataItem(8, 6.234f, "24 Karat", 0f),
        )
        adapter.addHeaderAndSubmitList(estimatesItems)

        initDropdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDropdown() {
        for (unit in MeasureUnit.values()) {
            val itemBinding = ItemDropCalcBinding.inflate(layoutInflater, dropDownContainer, true)
            with (itemBinding) {
                root.tag = unit
                title.text = unit.unitName
                if (unit == Logic.currentMeasureUnit) {
                    icon.show()
                } else {
                    icon.hidden()
                }
                root.setOnClickListener {
                    MeasureUnit.values().map {
                        dropDownContainer.findViewWithTag<View>(it)?.findViewById<View>(R.id.icon)?.hidden()
                    }
                    icon.show()
                    Logic.currentMeasureUnit = unit
                    tvMeasurementValue.text = unit.unitName

                    showDropDown(false)
                }
            }
        }
    }

    private fun showDropDown(open: Boolean) {
        if (open) {
            overlay.show()
            dropDownContainer.show()
        } else {
            overlay.hide()
            dropDownContainer.hide()
        }
    }
}
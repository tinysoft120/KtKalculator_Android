package com.midstatesrecycling.ktkalculator.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
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
import com.midstatesrecycling.ktkalculator.model.CalcResult
import com.midstatesrecycling.ktkalculator.util.LogU
import com.midstatesrecycling.ktkalculator.util.PreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CalculatorFragment : AbsMainFragment(R.layout.fragment_kalculator) {
    private var _binding: FragmentKalculatorBinding? = null
    private val binding get() = _binding!!

    private val tvGoldPrice: TextView           get() = binding.tvGoldPrice
    private val tvMeasurementValue: TextView    get() = binding.tvMeasurementValue
    private val overlay: View                   get() = binding.vOverlay
    private val dropDownContainer: LinearLayout get() = binding.llDropdown

    private val calcScope = CoroutineScope(Dispatchers.Default)
    private lateinit var adapter: EstimateTableAdapter

    private var goldRate: Double = PreferenceUtil.futureGoldPrice.toDouble()
    private var curMeasureUnit = MeasureUnit.DWT

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKalculatorBinding.bind(view)

        overlay.setOnClickListener { showDropDown(false) }
        binding.btnMeasure.setOnClickListener { showDropDown(!overlay.isVisible) }
        binding.btnCalculate.setOnClickListener { actionCalculate() }
        binding.btnClear.setOnClickListener { actionClear() }
        binding.btnGoldPrice.setOnClickListener { mainActivity.activePage(2) }
        tvGoldPrice.text = Logic.displayDecimalText(goldRate)
        tvMeasurementValue.text = curMeasureUnit.unitName

        adapter = EstimateTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)
        binding.tableview.setHasFixedSize(true)
        (binding.tableview.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        val estimatesItems = listOf(
            EstimateItem.DataItem(0, 0.0, "10 Karat", 0.0),
            EstimateItem.DataItem(1, 0.0, "12 Karat", 0.0),
            EstimateItem.DataItem(2, 0.0, "14 Karat", 0.0),
            EstimateItem.DataItem(3, 0.0, "16 Karat", 0.0),
            EstimateItem.DataItem(4, 0.0, "18 Karat", 0.0),
            EstimateItem.DataItem(5, 0.0, "22 Karat", 0.0),
            EstimateItem.DataItem(6, 0.0, "24 Karat", 0.0),
        )//.map { CalculateDataFromformula(it, curMeasureUnit) }

        adapter.addHeaderAndSubmitList(curMeasureUnit, estimatesItems)
        adapter.setOnItemChangeListener(object : EstimateTableAdapter.OnItemChangeListener{
            override fun onChangedQuantity(item: EstimateItem.DataItem, position: Int) {
                calcScope.launch {
                    val newItem = Logic.calculateEstimateValue(item, curMeasureUnit, goldRate)

                    LogU.e(TAG, "updated - %s", item.toString())
                    withContext(Dispatchers.Main) {
                        adapter.updateListItem(position, newItem)
                    }

                    val goldItems = adapter.currentList.mapNotNull { it as? EstimateItem.DataItem }
                    val result = Logic.calculateResult(goldItems, curMeasureUnit, goldRate)
                    mainViewModel.updateCalcResult(result)
                }
            }
        })

        mainViewModel.karatValue.observe(viewLifecycleOwner) { goldPrice ->
            if (goldRate != goldPrice) {
                goldRate = goldPrice
                reloadPage()
            }
        }

        initDropdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        reloadPage()
    }

    private fun initDropdown() {
        for (unit in MeasureUnit.values()) {
            val itemBinding = ItemDropCalcBinding.inflate(layoutInflater, dropDownContainer, true)
            with (itemBinding) {
                root.tag = unit
                title.text = unit.unitName
                if (unit == curMeasureUnit) {
                    icon.show()
                } else {
                    icon.hidden()
                }
                root.setOnClickListener {
                    showDropDown(false)
                    MeasureUnit.values().map {
                        dropDownContainer.findViewWithTag<View>(it)?.findViewById<View>(R.id.icon)?.hidden()
                    }
                    icon.show()
                    curMeasureUnit = unit

                    reloadPage()
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

    private fun actionCalculate() {
        var quantity = 0.0
        adapter.currentList.map {
            val item = it as? EstimateItem.DataItem
            if (item != null) {
                quantity += item.quantity
            }
        }
        if (quantity == 0.0) {
            showAlert("Karat Calculator", "There must be at least one quantity to calculate.")
            return
        }

        calcScope.launch {
            val goldItems = adapter.currentList.mapNotNull { it as? EstimateItem.DataItem }
            val result = Logic.calculateResult(goldItems, curMeasureUnit, goldRate)
            mainViewModel.updateCalcResult(result)

            withContext(Dispatchers.Main) {
                mainActivity.activePage(1)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun actionClear() {
        calcScope.launch {
            for (i in 0 until adapter.itemCount) {
                val item = adapter.currentList[i]
                if (item is EstimateItem.DataItem) {
                    item.quantity = 0.0
                    item.value = 0.0
                }
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }

            mainViewModel.updateCalcResult(CalcResult.EMPTY)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun reloadPage() {
        tvMeasurementValue.text = curMeasureUnit.unitName
        tvGoldPrice.text = Logic.displayDecimalText(goldRate) //Logic.RoundecValue(goldRate, 2)

        calcScope.launch {
            for (i in 0 until adapter.itemCount) {
                when (val item = adapter.currentList[i]) {
                    is EstimateItem.Header -> item.unit = curMeasureUnit
                    is EstimateItem.DataItem -> {
                        val newItem = Logic.calculateEstimateValue(item, curMeasureUnit, goldRate)
                        item.value = newItem
                        //adapter.updateListItem(i, newItem)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }

            val goldItems = adapter.currentList.mapNotNull { it as? EstimateItem.DataItem }
            val result = Logic.calculateResult(goldItems, curMeasureUnit, goldRate)
            mainViewModel.updateCalcResult(result)
        }
    }

    companion object {
        val TAG: String = CalculatorFragment::class.java.simpleName
    }
}
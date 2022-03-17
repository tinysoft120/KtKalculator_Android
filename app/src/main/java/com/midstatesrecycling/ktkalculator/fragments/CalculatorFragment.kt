package com.midstatesrecycling.ktkalculator.fragments

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
import com.midstatesrecycling.ktkalculator.extensions.toDoubleValue
import com.midstatesrecycling.ktkalculator.logic.Logic
import com.midstatesrecycling.ktkalculator.logic.MeasureUnit
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

    private val aryDWT: List<String> = listOf(".0200", ".0234", ".0277", ".0295", ".0359", ".0436", ".0481")
    private val arygrams: List<String> = listOf(".012863",".015038",".017829",".018958",".023137",".028020",".030912")
    private val aryTroy: List<String> = listOf(".400",".468",".554",".59",".718",".872",".962") // ".394",@".468",@".544",@".59",@".704",@".872",@".962"

    private var goldRate: Double = PreferenceUtil.futureGoldPrice.toDouble()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentKalculatorBinding.bind(view)

        overlay.setOnClickListener { showDropDown(false) }
        binding.btnMeasure.setOnClickListener { showDropDown(!overlay.isVisible) }
        binding.btnCalculate.setOnClickListener { actionCalculate() }
        binding.btnClear.setOnClickListener { actionClear() }
        binding.btnGoldPrice.setOnClickListener { mainActivity.activePage(2) }
        tvGoldPrice.text = Logic.displayDecimalText(goldRate)
        tvMeasurementValue.text = Logic.currentMeasureUnit.unitName

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
        )//.map { CalculateDataFromformula(it, Logic.currentMeasureUnit) }

        adapter.addHeaderAndSubmitList(estimatesItems)
        adapter.setOnItemChangeListener(object : EstimateTableAdapter.OnItemChangeListener{
            override fun onChangedQuantity(item: EstimateItem.DataItem, position: Int) {
                val newItem = calculateDataFromFormula(item, Logic.currentMeasureUnit)
                LogU.e(TAG, "updated - %s", item.toString())
                adapter.updateListItem(position, newItem)

                Logic.aryTextFieldData[item.index] = item.quantity.toString()
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

    private fun actionCalculate() {

        mainActivity.activePage(1)
    }

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
        }
    }

    private fun reloadPage() {
        tvGoldPrice.text = Logic.displayDecimalText(goldRate) //Logic.RoundecValue(goldRate, 2)

        calcScope.launch {
            Logic.MethodUpdateData()

            for (i in 0 until adapter.itemCount) {
                val item = adapter.currentList[i]
                if (item is EstimateItem.DataItem) {
                    val newItem = calculateDataFromFormula(item, Logic.currentMeasureUnit)
                    item.value = newItem
                    //adapter.updateListItem(i, newItem)
                }
            }
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun calculateDataFromFormula(item: EstimateItem.DataItem, unit: MeasureUnit): Double {
        val unitValue = when (unit) {
            MeasureUnit.DWT -> aryDWT[item.index].toDoubleValue()
            MeasureUnit.GRAM -> arygrams[item.index].toDoubleValue()
            MeasureUnit.TROY -> aryTroy[item.index].toDoubleValue()
        }

        val floatTemp = item.quantity * unitValue * goldRate
        val tempStr = String.format("%.2f", floatTemp)
        return tempStr.toDoubleValue()  // floatTemp
    }

    companion object {
        val TAG: String = CalculatorFragment::class.java.simpleName
    }
}
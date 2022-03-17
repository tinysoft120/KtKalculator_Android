package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.extensions.focusAndShowKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.adapter.PriceItem
import com.midstatesrecycling.ktkalculator.adapter.PriceTableAdapter
import com.midstatesrecycling.ktkalculator.databinding.FragmentGoldPriceBinding
import com.midstatesrecycling.ktkalculator.extensions.colorButtons
import com.midstatesrecycling.ktkalculator.extensions.toDoubleValue
import com.midstatesrecycling.ktkalculator.logic.Logic
import com.midstatesrecycling.ktkalculator.util.LogU
import com.midstatesrecycling.ktkalculator.util.PreferenceUtil
import java.text.DecimalFormat

class GoldPriceFragment : AbsMainFragment(R.layout.fragment_gold_price) {
    private var _binding: FragmentGoldPriceBinding? = null
    private val binding get() = _binding!!

    private val tvGoldPriceToday: TextView get() = binding.tvGoldPriceToday
    private val tvTodayPrice: TextView     get() = binding.tvTodayPrice
    private val etGoldPriceToday: EditText get() = binding.etGoldPriceToday

    private lateinit var adapter: PriceTableAdapter

    private var futureMode: Boolean = false
    private var goldRate: Double = PreferenceUtil.futureGoldPrice.toDouble()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGoldPriceBinding.bind(view)

        binding.btnEdit.setOnClickListener { actionEdit() }
        binding.btnRefresh.setOnClickListener { actionRefresh() }

        etGoldPriceToday.setText(Logic.displayDecimalText(goldRate))
        etGoldPriceToday.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etGoldPriceToday.isEnabled = false
                etGoldPriceToday.clearFocus()
                //etGoldPriceToday.isCursorVisible = false
                hideKeyboard()

                val price = etGoldPriceToday.text.toString()
                LogU.e(TAG, "lose focus: price=$price")
                makeFutureMode(price)
            }
        }
        etGoldPriceToday.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event.action == KeyEvent.ACTION_DOWN
                        && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                v.clearFocus()
            }
            return@setOnEditorActionListener true
        }

        adapter = PriceTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)

        val priceItems = listOf(
            PriceItem.DataItem(0, "10K", "$0.0", "10K", "$0.0"),
            PriceItem.DataItem(1, "14K", "$0.0", "14K", "$0.0"),
            PriceItem.DataItem(2, "18K", "$0.0", "18K", "$0.0"),
        )
        adapter.addHeaderAndSubmitList(priceItems)

        mainViewModel.netConnection.observe(viewLifecycleOwner) { connected ->
            if (!connected) {
                showAlert("Karat Calculator",
                    "No network found. Check your internet connection") {
                    val storedPrice = PreferenceUtil.futureGoldPrice
                    makeFutureMode(storedPrice)
                }
            }
        }

        mainViewModel.karatValue.observe(viewLifecycleOwner) { goldPrice ->
            if (goldRate != goldPrice) {
                LogU.d(TAG, "updated gold price:$goldPrice, prev value: $goldRate")
                goldRate = goldPrice
                reloadPage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        reloadPage()
    }

    private fun actionEdit() {
        etGoldPriceToday.isEnabled = true
        etGoldPriceToday.focusAndShowKeyboard()
    }

    private fun actionRefresh() {
        futureMode = false
        mainViewModel.fetchKaratApiValue()
    }

    private fun makeFutureMode(price: String) {
        val strPrice = Logic.displayDecimalText(price.toDoubleValue())
        val dblPrice = strPrice.toDoubleValue()
        if (strPrice.length > 1 && dblPrice > 0.0) {
            futureMode = true

            PreferenceUtil.futureGoldPrice = strPrice
            etGoldPriceToday.setText(strPrice)
            mainViewModel.updateKaratValue(dblPrice)

            showAlert("Karat Kalculator", "Gold price updated successfully")
        } else {
            showAlert("Karat Kalculator", "Enter valid gold price.")
        }
    }

    private fun reloadPage() {
        if (futureMode) {
            tvGoldPriceToday.text = "FUTURE GOLD PRICE :"
            tvTodayPrice.text = "FUTURE GOLD PRICES"
        } else {
            tvGoldPriceToday.text = "GOLD PRICE TODAY :"
            tvTodayPrice.text = "TODAY'S GOLD PRICE"
        }
        etGoldPriceToday.setText(Logic.displayDecimalText(goldRate))

        val dwtRates = listOf(0.0, 0.0200, 0.0277, 0.0359)
        val gramRates = listOf(0.0, 0.012863, 0.017829, 0.023137)
        for (i in 0 until adapter.itemCount) {
            val item = adapter.currentList[i]
            if (item is PriceItem.DataItem) {
                item.dwtValue = String.format("$%.2f", goldRate * dwtRates[i])
                item.gramValue = String.format("$%.2f", goldRate * gramRates[i])
            }
        }

        adapter.notifyDataSetChanged()
    }

    companion object {
        val TAG: String = GoldPriceFragment::class.java.simpleName
    }
}
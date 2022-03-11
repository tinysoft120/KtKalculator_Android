package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.adapter.PriceItem
import com.midstatesrecycling.ktkalculator.adapter.PriceTableAdapter
import com.midstatesrecycling.ktkalculator.databinding.FragmentGoldPriceBinding

class GoldPriceFragment : AbsMainFragment(R.layout.fragment_gold_price) {
    private var _binding: FragmentGoldPriceBinding? = null
    private val binding get() = _binding!!

    private val etGoldPriceToday: EditText get() = binding.etGoldPriceToday

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGoldPriceBinding.bind(view)

        binding.btnEdit.setOnClickListener { actionEdit() }
        binding.btnRefresh.setOnClickListener { actionRefresh() }

        etGoldPriceToday.doAfterTextChanged {
        }

        val adapter = PriceTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)

        val priceItems = listOf(
            PriceItem.DataItem(0, "10K", "$39.97", "10K", "$25.39"),
            PriceItem.DataItem(0, "12K", "$32.97", "14K", "$225.319"),
            PriceItem.DataItem(0, "13K", "$339.97", "15K", "$225.39"),
        )
        adapter.addHeaderAndSubmitList(priceItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun actionEdit() {

    }

    private fun actionRefresh() {

    }
}
package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.adapter.ResultItem
import com.midstatesrecycling.ktkalculator.adapter.ResultTableAdapter
import com.midstatesrecycling.ktkalculator.databinding.FragmentResultBinding
import com.midstatesrecycling.ktkalculator.logic.Logic

class ResultFragment : AbsMainFragment(R.layout.fragment_result) {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ResultTableAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultBinding.bind(view)

        adapter = ResultTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)

        val resultItems = listOf(
            ResultItem.Header(App.getContext().getString(R.string.strResult)),
            ResultItem.DataItem(0, App.getContext().getString(R.string.strResultTitle1), "$0"),
            ResultItem.DataItem(1, App.getContext().getString(R.string.strResultTitle2), "0 t/ozs"),
            ResultItem.DataItem(2, App.getContext().getString(R.string.strResultTitle3), "0 t/ozs"),
            ResultItem.DataItem(3, App.getContext().getString(R.string.strResultTitle4), "0 t/ozs"),
            ResultItem.DataItem(4, App.getContext().getString(R.string.strResultTitle5), "0.00 %\n0.00 K"),
            ResultItem.Separator,
            ResultItem.Header(App.getContext().getString(R.string.strEstimatedRefiningTers)),
            ResultItem.DataItem(5, App.getContext().getString(R.string.strResultTitle6), "0 %"),
            ResultItem.DataItem(6, App.getContext().getString(R.string.strResultTitle7), "$0.00"),
        )
        adapter.addHeaderAndSubmitList(resultItems)

        mainViewModel.calcResult.observe(viewLifecycleOwner) { result ->
            adapter.currentList.map {
                val item = it as? ResultItem.DataItem ?: return@map
                item.result = when (item.index) {
                    0 -> "$${Logic.roundedShort(result.finalEstimatedValue, 2)}"
                    1 -> "${Logic.roundedShort(result.totalWeight, 3)} t/ozs"
                    2 -> "${Logic.roundedShort(result.estimatedPureGold, 3)} t/ozs"
                    3 -> "${Logic.roundedShort(result.estimatedPayablePureGold, 2)} t/ozs"
                    4 -> "${Logic.displayDecimalText(result.estimatedPurityPercentage)} %" +
                            "\n${Logic.displayDecimalText(result.estimatedPurityKarat)} K"
                    5 -> "${Logic.roundedShort(result.accountability, 2)}%"
                    6 -> {
                        if (result.treatmentCharge != -1.0)
                            "$${Logic.roundedValue(result.treatmentCharge, 2)}"
                        else "Waived"
                    }
                    else -> ""
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}
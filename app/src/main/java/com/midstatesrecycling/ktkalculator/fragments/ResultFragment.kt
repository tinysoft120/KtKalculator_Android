package com.midstatesrecycling.ktkalculator.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.adapter.ResultItem
import com.midstatesrecycling.ktkalculator.adapter.ResultTableAdapter
import com.midstatesrecycling.ktkalculator.databinding.FragmentResultBinding

class ResultFragment : AbsMainFragment(R.layout.fragment_result) {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResultBinding.bind(view)

        val adapter = ResultTableAdapter()
        binding.tableview.adapter = adapter
        binding.tableview.layoutManager = LinearLayoutManager(activity)

        val resultItems = listOf(
            ResultItem.Header(App.getContext().getString(R.string.strResult)),
            ResultItem.DataItem(1, App.getContext().getString(R.string.strResultTitle1), "$0"),
            ResultItem.DataItem(2, App.getContext().getString(R.string.strResultTitle2), "0 t/ozs"),
            ResultItem.DataItem(3, App.getContext().getString(R.string.strResultTitle3), "0 t/ozs"),
            ResultItem.DataItem(4, App.getContext().getString(R.string.strResultTitle4), "0 t/ozs"),
            ResultItem.DataItem(5, App.getContext().getString(R.string.strResultTitle5), "0.00 %\n0.00 K"),
            ResultItem.Separator,
            ResultItem.Header(App.getContext().getString(R.string.strEstimatedRefiningTers)),
            ResultItem.DataItem(6, App.getContext().getString(R.string.strResultTitle6), "0 %"),
            ResultItem.DataItem(7, App.getContext().getString(R.string.strResultTitle7), "$0.00"),
        )
        adapter.addHeaderAndSubmitList(resultItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}
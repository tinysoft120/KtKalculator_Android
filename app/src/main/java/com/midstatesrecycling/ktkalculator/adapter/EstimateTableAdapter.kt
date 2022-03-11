package com.midstatesrecycling.ktkalculator.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.databinding.ItemEstimateTableDataBinding
import com.midstatesrecycling.ktkalculator.databinding.ItemEstimateTableHeaderBinding
import com.midstatesrecycling.ktkalculator.logic.Logic
import com.midstatesrecycling.ktkalculator.logic.MeasureUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class EstimateTableAdapter:
    ListAdapter<EstimateItem, RecyclerView.ViewHolder>(EstimateTableDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<EstimateItem.DataItem>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(EstimateItem.Header)
                else -> listOf(EstimateItem.Header) + list
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val estimateItem = getItem(position) as EstimateItem.DataItem
                holder.bind(estimateItem)
            }
            is HeaderViewHolder -> {
                holder.bind(Logic.currentMeasureUnit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EstimateItem.Header -> ITEM_VIEW_TYPE_HEADER
            is EstimateItem.DataItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder(private val binding: ItemEstimateTableHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(unit: MeasureUnit) {
            binding.tvTitle1.text = App.getContext().getString(R.string.strFormatQuantityIn_, unit.unitName)
            binding.tvTitle2.text = App.getContext().getString(R.string.strDescription)
            binding.tvTitle3.text = App.getContext().getString(R.string.strEstimatedValue)
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEstimateTableHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }


    class ItemViewHolder private constructor(private val binding: ItemEstimateTableDataBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: EstimateItem.DataItem) {
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val estimated = numberFormat.format(item.value)
            numberFormat.maximumFractionDigits = 4
            numberFormat.minimumFractionDigits = 0
            val quantity = numberFormat.format(item.quantity)

            binding.etQuantity.setText(quantity)
            binding.tvKarat.text = item.description
            binding.tvEstimatedValue.text = String.format("$%s", estimated)
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEstimateTableDataBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}

private class EstimateTableDiffCallback : DiffUtil.ItemCallback<EstimateItem>() {
    override fun areItemsTheSame(oldItem: EstimateItem, newItem: EstimateItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: EstimateItem, newItem: EstimateItem): Boolean {
        return oldItem == newItem
    }
}

sealed class EstimateItem {

    data class DataItem(val index: Int,
                            var quantity: Float,
                            val description: String,
                            var value: Float
                            ) : EstimateItem() {
        override val id = index
    }

    object Header : EstimateItem() {
        override val id = -1
    }

    abstract val id: Int
}
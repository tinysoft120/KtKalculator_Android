package com.midstatesrecycling.ktkalculator.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.midstatesrecycling.ktkalculator.databinding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_FOOTER = 2

class PriceTableAdapter:
    ListAdapter<PriceItem, RecyclerView.ViewHolder>(PriceTableDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<PriceItem.DataItem>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(PriceItem.Header, PriceItem.Footer)
                else -> listOf(PriceItem.Header) + list + listOf(PriceItem.Footer)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val estimateItem = getItem(position) as PriceItem.DataItem
                holder.bind(estimateItem)
            }
            is HeaderViewHolder -> {}
            is FooterViewHolder -> {}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            PriceItem.Header -> ITEM_VIEW_TYPE_HEADER
            PriceItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            is PriceItem.DataItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPriceTableHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding.root)
            }
        }
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPriceTableFooterBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding.root)
            }
        }
    }

    class ItemViewHolder private constructor(private val binding: ItemPriceTableDataBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PriceItem.DataItem) {
            binding.tvUnit1.text = item.dwtUnit
            binding.tvValue1.text = item.dwtValue
            binding.tvUnit2.text = item.gramUnit
            binding.tvValue2.text = item.gramValue
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPriceTableDataBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}

private class PriceTableDiffCallback : DiffUtil.ItemCallback<PriceItem>() {
    override fun areItemsTheSame(oldItem: PriceItem, newItem: PriceItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: PriceItem, newItem: PriceItem): Boolean {
        return oldItem == newItem
    }
}

sealed class PriceItem {

    data class DataItem(val index: Int,
                            var dwtUnit: String,
                            val dwtValue: String,
                            var gramUnit: String,
                            var gramValue: String
                            ) : PriceItem() {
        override val id = index
    }

    object Header : PriceItem() {
        override val id = -1
    }

    object Footer : PriceItem() {
        override val id = -1
    }

    abstract val id: Int
}
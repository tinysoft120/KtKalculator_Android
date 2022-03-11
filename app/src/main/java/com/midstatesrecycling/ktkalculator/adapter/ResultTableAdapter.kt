package com.midstatesrecycling.ktkalculator.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.databinding.*
import com.midstatesrecycling.ktkalculator.logic.Logic
import com.midstatesrecycling.ktkalculator.logic.MeasureUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_SEP = 2

class ResultTableAdapter:
    ListAdapter<ResultItem, RecyclerView.ViewHolder>(ResultTableDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<ResultItem>?) {
        adapterScope.launch {
            val items = list ?: listOf()
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val estimateItem = getItem(position) as ResultItem.DataItem
                holder.bind(estimateItem)
            }
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as ResultItem.Header
                holder.bind(headerItem.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            ITEM_VIEW_TYPE_SEP -> SeparatorViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ResultItem.Header -> ITEM_VIEW_TYPE_HEADER
            is ResultItem.DataItem -> ITEM_VIEW_TYPE_ITEM
            ResultItem.Separator -> ITEM_VIEW_TYPE_SEP
        }
    }

    class HeaderViewHolder(private val binding: ItemResultTableHeaderBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvTitle.text = title
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemResultTableHeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }


    class ItemViewHolder private constructor(private val binding: ItemResultTableDataBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResultItem.DataItem) {
            binding.tvKey.text = item.description
            binding.tvResultValue.text = item.result
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemResultTableDataBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }


    class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): SeparatorViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemResultTableSepBinding.inflate(layoutInflater, parent, false)
                return SeparatorViewHolder(binding.root)
            }
        }
    }
}

private class ResultTableDiffCallback : DiffUtil.ItemCallback<ResultItem>() {
    override fun areItemsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ResultItem, newItem: ResultItem): Boolean {
        return oldItem == newItem
    }
}

sealed class ResultItem {

    abstract val id: Int

    data class DataItem(val index: Int,
                        val description: String,
                        val result: String
    ) : ResultItem() {
        override val id = index
    }

    data class Header(val title: String) : ResultItem() {
        override val id = -1
    }

    object Separator : ResultItem() {
        override val id = -1
    }
}
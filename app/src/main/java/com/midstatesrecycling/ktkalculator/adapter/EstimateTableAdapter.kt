package com.midstatesrecycling.ktkalculator.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.midstatesrecycling.ktkalculator.App
import com.midstatesrecycling.ktkalculator.R
import com.midstatesrecycling.ktkalculator.databinding.ItemEstimateTableDataBinding
import com.midstatesrecycling.ktkalculator.databinding.ItemEstimateTableHeaderBinding
import com.midstatesrecycling.ktkalculator.extensions.toDoubleValue
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

class EstimateTableAdapter : //RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    ListAdapter<EstimateItem, RecyclerView.ViewHolder>(EstimateTableDiffCallback()) {
    interface OnItemChangeListener {
        fun onChangedQuantity(item: EstimateItem.DataItem, position: Int)
    }

    //private val mTableData: MutableList<EstimateItem> = mutableListOf()
    private lateinit var listener: OnItemChangeListener
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    //override fun getItemCount(): Int {
    //    return mTableData.count()
    //}

    @SuppressLint("NotifyDataSetChanged")
    fun addHeaderAndSubmitList(list: List<EstimateItem.DataItem>?) {
        //mTableData.clear()
        val items = when (list) {
            null -> listOf(EstimateItem.Header)
            else -> listOf(EstimateItem.Header) + list
        }
        //mTableData.addAll(items)
        submitList(items)

        //adapterScope.launch {
        //    withContext(Dispatchers.Main) {
        //    }
        //}
    }

    fun setOnItemChangeListener(listener: OnItemChangeListener) {
        this.listener = listener
    }

    fun updateListItem(index: Int, newValue: Double) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                val item = (currentList[index] as? EstimateItem.DataItem)
                item?.let {
                    it.value = newValue
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val estimateItem = getItem(position) as EstimateItem.DataItem
                holder.bind(estimateItem, position)
            }
            is HeaderViewHolder -> {
                holder.bind(Logic.currentMeasureUnit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEstimateTableDataBinding.inflate(layoutInflater, parent, false)
                ItemViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
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

    inner class ItemViewHolder constructor(private val binding: ItemEstimateTableDataBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: EstimateItem.DataItem, position: Int) {
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2
            val estimated = numberFormat.format(item.value)
            numberFormat.maximumFractionDigits = 4
            numberFormat.minimumFractionDigits = 0
            val quantity = numberFormat.format(item.quantity)

            if (quantity.toDoubleValue() == 0.0) {
                binding.etQuantity.setText("")
            } else {
                binding.etQuantity.setText(quantity)
            }
            binding.tvKarat.text = item.description
            if (estimated.toDoubleValue() == 0.0) {
                binding.tvEstimatedValue.text = ""
            } else {
                binding.tvEstimatedValue.text = String.format("$%s", estimated)
            }

            binding.etQuantity.imeOptions =
                if (position == itemCount-1) {
                    EditorInfo.IME_ACTION_DONE
                } else {
                    EditorInfo.IME_ACTION_NEXT
                }
            binding.etQuantity.doAfterTextChanged {
                if (adapterPosition == RecyclerView.NO_POSITION) return@doAfterTextChanged

                val newValue = it.toString().toDoubleValue()
                val updated = currentList[adapterPosition] as EstimateItem.DataItem
                if (updated.quantity != newValue) {
                    updated.quantity = newValue
                    // notifyItemChanged(adapterPosition)
                }
            }
            binding.etQuantity.setOnFocusChangeListener { _, hasFocus ->
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnFocusChangeListener

                if (!hasFocus) {
                    val updated = currentList[adapterPosition] as EstimateItem.DataItem
                    listener.onChangedQuantity(updated, adapterPosition)
                }
            }
            binding.etQuantity.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    v.clearFocus()
                }
                return@setOnEditorActionListener false
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
                            var quantity: Double,
                            val description: String,
                            var value: Double
                            ) : EstimateItem() {
        override val id = index
    }

    object Header : EstimateItem() {
        override val id = -1
    }

    abstract val id: Int
}
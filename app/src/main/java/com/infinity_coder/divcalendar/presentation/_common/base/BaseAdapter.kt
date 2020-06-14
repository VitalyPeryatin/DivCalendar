package com.infinity_coder.divcalendar.presentation._common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseAdapter<V> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<V>>() {

    protected val items = mutableListOf<V>()

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<V>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        abstract fun bind(model: T, position: Int)
    }
}
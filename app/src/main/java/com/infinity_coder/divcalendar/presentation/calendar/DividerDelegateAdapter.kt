package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R

object DividerAdapter : KDelegateAdapter<DividerItem>() {
    override fun getLayoutId() = R.layout.item_divider

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean =
        items[position] == DividerItem

    override fun onBind(item: DividerItem, viewHolder: KViewHolder) {}
}

object DividerItem: IComparableItem {
    override fun id(): Any = this

    override fun content(): Any = this
}
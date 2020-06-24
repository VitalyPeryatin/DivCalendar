package com.infinity_coder.divcalendar.presentation.tabs.calendar.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.tabs.calendar.models.DividerPresentationModel

class DividerDelegateAdapter : KDelegateAdapter<DividerPresentationModel>() {

    override fun getLayoutId() = R.layout.item_divider_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean =
        items[position] == DividerPresentationModel

    override fun onBind(item: DividerPresentationModel, viewHolder: KViewHolder) {}
}
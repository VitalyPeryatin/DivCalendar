package com.infinity_coder.divcalendar.presentation.tabs.calendar.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.tabs.calendar.models.HeaderPaymentPresentationModel
import kotlinx.android.synthetic.main.item_header_payment_calendar.*

class HeaderPaymentRecyclerDelegateAdapter : KDelegateAdapter<HeaderPaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_header_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is HeaderPaymentPresentationModel
    }

    override fun onBind(item: HeaderPaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            headerPaymentMonth.text =
                headerPaymentMonth.context.resources.getStringArray(R.array.months_nominative_case)[item.month]
        }
    }
}
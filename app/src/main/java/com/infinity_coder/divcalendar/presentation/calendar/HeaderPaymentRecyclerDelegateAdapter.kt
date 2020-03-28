package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.models.HeaderPaymentVM
import kotlinx.android.synthetic.main.item_header_payment_calendar.*

class HeaderPaymentRecyclerDelegateAdapter : KDelegateAdapter<HeaderPaymentVM>() {

    override fun getLayoutId() = R.layout.item_header_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is HeaderPaymentVM
    }

    override fun onBind(item: HeaderPaymentVM, viewHolder: KViewHolder) {
        viewHolder.run {
            header_payment_month.text = item.name
        }
    }

}
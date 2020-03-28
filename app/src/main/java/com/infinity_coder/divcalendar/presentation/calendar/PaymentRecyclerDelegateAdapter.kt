package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.loadImg
import com.infinity_coder.divcalendar.presentation.models.PaymentVM
import kotlinx.android.synthetic.main.item_payment_calendar.*

class PaymentRecyclerDelegateAdapter : KDelegateAdapter<PaymentVM>(){

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentVM
    }

    override fun onBind(item: PaymentVM, viewHolder: KViewHolder) {
        viewHolder.run {
            payment_name.text = item.name
            payment_count.text = item.count
            payment_date.text = item.date
            payment_dividends.text = item.dividends
            payment_logo.loadImg(item.logo)
        }
    }
}
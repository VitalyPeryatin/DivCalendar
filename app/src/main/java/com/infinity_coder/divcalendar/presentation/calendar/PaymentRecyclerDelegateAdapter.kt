package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.loadImg
import com.infinity_coder.divcalendar.presentation.models.PaymentVM
import kotlinx.android.synthetic.main.item_payment_calendar.*

class PaymentRecyclerDelegateAdapter : KDelegateAdapter<PaymentVM>() {

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentVM
    }

    override fun onBind(item: PaymentVM, viewHolder: KViewHolder) {
        viewHolder.run {
            paymentName.text = item.name
            paymentCount.text = item.count
            paymentDate.text = item.date
            paymentDividends.text = item.dividends
            paymentLogo.loadImg(item.logo)
        }
    }
}
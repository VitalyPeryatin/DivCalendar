package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.models.FooterPaymentVM
import kotlinx.android.synthetic.main.item_footer_payment_calendar.*

class FooterPaymentRecyclerDelegateAdapter : KDelegateAdapter<FooterPaymentVM>() {

    override fun getLayoutId() = R.layout.item_footer_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is FooterPaymentVM
    }

    override fun onBind(item: FooterPaymentVM, viewHolder: KViewHolder) {
        viewHolder.run {
            footer_payment_monthly_income.text = item.income
        }
    }
}
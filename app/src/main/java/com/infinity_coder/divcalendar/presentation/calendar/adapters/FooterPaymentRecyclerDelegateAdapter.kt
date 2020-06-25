package com.infinity_coder.divcalendar.presentation.calendar.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation.calendar.models.FooterPaymentPresentationModel
import kotlinx.android.synthetic.main.item_footer_payment_calendar.*

class FooterPaymentRecyclerDelegateAdapter : KDelegateAdapter<FooterPaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_footer_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is FooterPaymentPresentationModel
    }

    override fun onBind(item: FooterPaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            val context = footerPaymentMonthlyIncome.context
            val income = "${item.income} ${SecurityCurrencyDelegate.getCurrencyBadge(context, item.currentCurrency)}"
            footerPaymentMonthlyIncome.text = context.getString(R.string.monthly_income_label, income)
        }
    }
}
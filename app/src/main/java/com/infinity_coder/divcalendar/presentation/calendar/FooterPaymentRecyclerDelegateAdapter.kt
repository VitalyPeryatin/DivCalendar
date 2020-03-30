package com.infinity_coder.divcalendar.presentation.calendar

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.models.FooterPaymentPresentationModel
import kotlinx.android.synthetic.main.item_footer_payment_calendar.*
import kotlin.Int

class FooterPaymentRecyclerDelegateAdapter : KDelegateAdapter<FooterPaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_footer_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is FooterPaymentPresentationModel
    }

    override fun onBind(item: FooterPaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.footerPaymentMonthlyIncome.text = item.income
    }

}
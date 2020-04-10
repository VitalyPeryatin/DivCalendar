package com.infinity_coder.divcalendar.presentation.calendar.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain._common.DisplayTaxesDelegate
import com.infinity_coder.divcalendar.presentation.calendar.models.FooterPaymentPresentationModel
import kotlinx.android.synthetic.main.item_footer_payment_calendar.*

class FooterPaymentRecyclerDelegateAdapter : KDelegateAdapter<FooterPaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_footer_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is FooterPaymentPresentationModel
    }

    override fun onBind(item: FooterPaymentPresentationModel, viewHolder: KViewHolder) {
        val currencyStringId = when (item.currentCurrency) {
            RateRepository.RUB_RATE -> R.string.monthly_income_label_rub
            RateRepository.USD_RATE -> R.string.monthly_income_label_usd
            else -> R.string.value_currency_undefined
        }
        viewHolder.run {
            var totalPayments = footerPaymentMonthlyIncome.context.getString(currencyStringId, item.income)
            totalPayments = DisplayTaxesDelegate.displayTaxes(totalPayments)

            footerPaymentMonthlyIncome.text = totalPayments
        }
    }
}
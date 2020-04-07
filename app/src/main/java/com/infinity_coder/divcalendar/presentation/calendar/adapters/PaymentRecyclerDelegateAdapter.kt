package com.infinity_coder.divcalendar.presentation.calendar.adapters

import android.content.Context
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import com.infinity_coder.divcalendar.presentation.calendar.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.item_payment_calendar.*

class PaymentRecyclerDelegateAdapter : KDelegateAdapter<PaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentPresentationModel
    }

    override fun onBind(item: PaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            paymentName.text = item.name
            paymentCount.text =
                paymentCount.context.getString(R.string.count_securities, item.count)
            paymentDate.text = paymentDate.context.getDate(item.date)

            val currencyStringId = when (item.currentCurrency) {
                RateRepository.USD_RATE -> R.string.value_currency_usd
                RateRepository.RUB_RATE -> R.string.value_currency_rub
                else -> R.string.value_currency_undefined
            }
            paymentDividends.text = paymentDividends.context.getString(currencyStringId, item.dividends)
            SimpleGlide.loadImage(paymentLogo, item.logo, paymentLogo)
        }
    }

    private fun Context.getDate(date: String): String {
        val months = resources.getStringArray(R.array.months_genitive)
        return DateFormatter.formatDate(date, months)
    }
}
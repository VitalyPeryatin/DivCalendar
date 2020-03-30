package com.infinity_coder.divcalendar.presentation.calendar

import android.content.Context
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.loadImg
import com.infinity_coder.divcalendar.presentation.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.item_payment_calendar.*
import kotlin.Int

class PaymentRecyclerDelegateAdapter : KDelegateAdapter<PaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentPresentationModel
    }

    override fun onBind(item: PaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            paymentName.text = item.name
            paymentCount.text = item.count
            paymentDate.text = paymentDate.context.getDate(item.date)
            paymentDividends.text = item.dividends
            paymentLogo.loadImg(item.logo)
        }
    }

    private fun Context.getDate(date: String): String {
        val splitDate = date.split("-")
        val months = resources.getStringArray(R.array.months_genitive)
        return "${splitDate[2].toInt()} ${months[splitDate[1].toInt()]} ${splitDate[0]}"
    }

}
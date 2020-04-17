package com.infinity_coder.divcalendar.presentation.calendar.adapters

import android.content.Context
import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import com.infinity_coder.divcalendar.presentation._common.dpToPx
import com.infinity_coder.divcalendar.presentation.calendar.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.item_payment_calendar.*

class PaymentRecyclerDelegateAdapter : KDelegateAdapter<PaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentPresentationModel
    }

    override fun onBind(item: PaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {

            if(DateFormatter.isExpired(item.date)){
                paymentBlackout.visibility = View.VISIBLE
                paymentRoot.elevation = paymentRoot.context.dpToPx(1f)
            }else{
                paymentBlackout.visibility = View.GONE
                paymentRoot.elevation = paymentRoot.context.dpToPx(2f)
            }

            paymentName.text = item.name
            paymentCount.text = paymentCount.context.getString(R.string.count_securities, item.count)
            paymentDate.text = item.date

            paymentDividends.text = item.dividends

            SimpleGlide.loadImage(paymentLogo, item.logo, paymentLogo)
        }
    }

}
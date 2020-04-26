package com.infinity_coder.divcalendar.presentation.calendar.adapters

import android.annotation.SuppressLint
import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import com.infinity_coder.divcalendar.presentation._common.extensions.dpToPx
import com.infinity_coder.divcalendar.presentation.calendar.models.PaymentPresentationModel
import kotlinx.android.synthetic.main.item_payment_calendar.*

class PaymentRecyclerDelegateAdapter(
    private val itemClickListener: OnItemClickListener? = null
) : KDelegateAdapter<PaymentPresentationModel>() {

    override fun getLayoutId() = R.layout.item_payment_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is PaymentPresentationModel
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(item: PaymentPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {

            if (item.expired) {
                paymentBlackout.visibility = View.VISIBLE
                paymentRoot.elevation = paymentRoot.context.dpToPx(1f)
            } else {
                paymentBlackout.visibility = View.GONE
                paymentRoot.elevation = paymentRoot.context.dpToPx(2f)
            }

            paymentRoot.setOnClickListener {
                if (item.expired) {
                    itemClickListener?.onItemClick(item)
                }
            }

            paymentName.text = item.name
            val count = DecimalFormatStorage.formatter.format(item.count)
            paymentCount.text = paymentCount.context.getString(R.string.count_securities, count)

            if (item.forecast) {
                paymentDate.text = "${item.presentationDate} *"
            } else {
                paymentDate.text = item.presentationDate
            }

            paymentDividends.text = item.dividends

            SimpleGlide.loadImage(paymentLogo, item.logo, paymentLogo)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: PaymentPresentationModel)
    }
}
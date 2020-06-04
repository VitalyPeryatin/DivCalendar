package com.infinity_coder.divcalendar.presentation.search.adapters

import android.view.View
import android.view.ViewGroup
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import com.infinity_coder.divcalendar.presentation._common.base.BaseAdapter
import com.infinity_coder.divcalendar.presentation._common.extensions.inflate
import com.infinity_coder.divcalendar.presentation._common.extensions.visibility
import kotlinx.android.synthetic.main.item_security_search.*

class SecurityRecyclerAdapter(private var clickListener: OnClickListener? = null) : BaseAdapter<SecurityNetModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val view = parent.inflate(R.layout.item_security_search)
        val viewHolder = SecurityViewHolder(view)
        viewHolder.containerView.setOnClickListener {
            clickListener?.onClick(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    class SecurityViewHolder(override val containerView: View) : BaseAdapter.BaseViewHolder<SecurityNetModel>(containerView) {

        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun bind(security: SecurityNetModel, position: Int) {
            nameTextView.text = security.name
            sourceTextView.text = security.ticker
            typeTextView.text = SecurityTypeDelegate.getTitle(containerView.context, security.type)
            exchangeDashTextView.visibility(security.exchange.isNotEmpty(), View.INVISIBLE)
            exchangeTextView.visibility(security.exchange.isNotEmpty(), View.INVISIBLE)
            exchangeTextView.text = security.exchange
            yearYieldTextView.text = containerView.context.getString(R.string.yield_in_year, security.yearYield)

            val securityColor = SecurityTypeDelegate.getColor(containerView.context, security.type)
            securityTypeView.setBackgroundColor(securityColor)

            SimpleGlide.loadImage(containerView, security.logo, logoImageView)
        }
    }

    interface OnClickListener {
        fun onClick(security: SecurityNetModel)
    }
}
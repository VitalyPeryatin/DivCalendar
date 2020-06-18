package com.infinity_coder.divcalendar.presentation.portfolio

import android.view.View
import android.view.ViewGroup
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import com.infinity_coder.divcalendar.presentation._common.base.BaseAdapter
import com.infinity_coder.divcalendar.presentation._common.extensions.inflate
import kotlinx.android.synthetic.main.item_security_portfolio.*

class SecurityRecyclerAdapter(
    private val onClickListener: OnItemClickListener? = null
) : BaseAdapter<SecurityDbModel>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val view = parent.inflate(R.layout.item_security_portfolio)
        val viewHolder = SecurityViewHolder(view)
        viewHolder.containerView.setOnClickListener {
            onClickListener?.onItemClick(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    class SecurityViewHolder(override val containerView: View) : BaseAdapter.BaseViewHolder<SecurityDbModel>(containerView) {

        private val resources = containerView.resources

        override fun bind(model: SecurityDbModel, position: Int) {
            val securityColor = SecurityTypeDelegate.getColor(containerView.context, model.type)
            securityTypeView.setBackgroundColor(securityColor)

            nameTextView.text = model.name
            nameTextView.isSelected = true

            val count = DecimalFormatStorage.formatterWithPoints.format(model.count)
            countTextView.text = resources.getString(R.string.sec_count, count)

            totalPriceTextView.text = SecurityCurrencyDelegate.getValueWithCurrencyConsiderCopecks(
                containerView.context,
                model.totalPrice,
                model.currency
            )

            yearYieldTextView.text = when (model.yearYield) {
                0f -> resources.getString(R.string.security_without_payout)
                else -> resources.getString(R.string.yield_in_year, model.yearYield)
            }

            if (model.logo.isNotEmpty()) {
                SimpleGlide.loadImage(logoImageView, model.logo, logoImageView)
            } else {
                logoImageView.setImageResource(R.drawable.ic_default_security_logo_transparent)
                logoImageView.circleBackgroundColor = model.color
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(securityPackage: SecurityDbModel)
    }
}
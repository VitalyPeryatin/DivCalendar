package com.infinity_coder.divcalendar.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.presentation._common.DecimalFormatStorage
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_security_portfolio.*

class SecurityRecyclerAdapter(
    private val onClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<SecurityRecyclerAdapter.SecurityViewHolder>() {

    private var securityList: List<SecurityDbModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_security_portfolio, parent, false)
        return SecurityViewHolder(view, onClickListener)
    }

    override fun getItemCount(): Int = securityList.size

    override fun onBindViewHolder(holder: SecurityViewHolder, position: Int) {
        holder.bind(securityList[position])
    }

    fun setSecurities(securities: List<SecurityDbModel>) {
        this.securityList = securities
        notifyDataSetChanged()
    }

    class SecurityViewHolder(
        override val containerView: View,
        private val onClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val resources = containerView.resources

        fun bind(securityPackage: SecurityDbModel) {
            securityPortfolioCardLayout.setOnClickListener {
                onClickListener?.onItemClick(securityPackage)
            }
            val securityColor = SecurityTypeDelegate.getColor(containerView.context, securityPackage.type)
            securityTypeView.setBackgroundColor(securityColor)
            nameTextView.text = securityPackage.name
            nameTextView.isSelected = true
            val count = DecimalFormatStorage.formatter.format(securityPackage.count)
            countTextView.text = resources.getString(R.string.sec_count, count)
            totalPriceTextView.text = SecurityCurrencyDelegate.getValueWithCurrency(
                containerView.context,
                securityPackage.totalPrice,
                securityPackage.currency
            )
            yearYieldTextView.text = resources.getString(R.string.yield_in_year, securityPackage.yearYield)

            if (securityPackage.logo.isNotEmpty()) {
                SimpleGlide.loadImage(logoImageView, securityPackage.logo, logoImageView)
            } else {
                logoImageView.setImageResource(R.drawable.ic_default_security_logo_transparent)
                logoImageView.circleBackgroundColor = securityPackage.color
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(securityPackage: SecurityDbModel)
    }
}
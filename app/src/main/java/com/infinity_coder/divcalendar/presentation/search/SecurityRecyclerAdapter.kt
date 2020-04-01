package com.infinity_coder.divcalendar.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_security_search.*

class SecurityRecyclerAdapter(
    private var clickListener: OnClickListener? = null
) : RecyclerView.Adapter<SecurityRecyclerAdapter.SecurityViewHolder>() {

    private var securities: List<SecurityNetworkModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_security_search, parent, false)
        return SecurityViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int = securities.size

    override fun onBindViewHolder(holder: SecurityViewHolder, position: Int) {
        holder.bind(securities[position])
    }

    fun setSecurities(securities: List<SecurityNetworkModel>) {
        this.securities = securities
        notifyDataSetChanged()
    }

    class SecurityViewHolder(
        override val containerView: View,
        private var clickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(security: SecurityNetworkModel) {
            nameTextView.text = security.name
            tickerTextView.text = security.ticker
            val securityColor = SecurityTypeDelegate.getColor(containerView.context, security.type)
            securityTypeView.setBackgroundColor(securityColor)
            typeTextView.text = getSecurityTypeText(security.type)
            exchangeTextView.text = security.exchange
            yearYieldTextView.text = containerView.context.getString(R.string.yield_in_year, security.yield)
            SimpleGlide.loadSVG(containerView, security.logo, logoImageView)

            containerView.setOnClickListener {
                clickListener?.onClick(security)
            }
        }

        private fun getSecurityTypeText(type: String): String {
            val resources = containerView.context.resources
            return when (type) {
                SecurityTypeDelegate.SECURITY_TYPE_STOCK -> resources.getString(R.string.stocks)
                SecurityTypeDelegate.SECURITY_TYPE_BOND -> resources.getString(R.string.bonds)
                else -> resources.getString(R.string.stocks)
            }
        }
    }

    interface OnClickListener {
        fun onClick(security: SecurityNetworkModel)
    }
}
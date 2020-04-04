package com.infinity_coder.divcalendar.presentation.search.adapters

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
        return SecurityViewHolder(
            view,
            clickListener
        )
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
            sourceTextView.text = security.ticker
            val securityColor = SecurityTypeDelegate.getColor(containerView.context, security.type)
            securityTypeView.setBackgroundColor(securityColor)
            typeTextView.text = SecurityTypeDelegate.getTitle(containerView.context, security.type)
            exchangeTextView.text = security.exchange
            yearYieldTextView.text =
                containerView.context.getString(R.string.yield_in_year, security.yield)
            SimpleGlide.loadImage(containerView, security.logo, logoImageView)

            containerView.setOnClickListener {
                clickListener?.onClick(security)
            }
        }
    }

    interface OnClickListener {
        fun onClick(security: SecurityNetworkModel)
    }
}
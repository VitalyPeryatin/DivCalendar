package com.infinity_coder.divcalendar.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.ShortSecurityNetworkModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_security_search.*

class SecurityRecyclerAdapter(
    private var clickListener: OnClickListener? = null
) : RecyclerView.Adapter<SecurityRecyclerAdapter.SecurityViewHolder>() {

    private var securities: List<ShortSecurityNetworkModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_security_search, parent, false)
        return SecurityViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int = securities.size

    override fun onBindViewHolder(holder: SecurityViewHolder, position: Int) {
        holder.bind(securities[position])
    }

    fun setSecurities(securities: List<ShortSecurityNetworkModel>) {
        this.securities = securities
        notifyDataSetChanged()
    }

    class SecurityViewHolder(
        override val containerView: View,
        private var clickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(security: ShortSecurityNetworkModel) {
            nameTextView.text = security.name
            tickerTextView.text = security.secid

            containerView.setOnClickListener {
                clickListener?.onClick(security)
            }
        }
    }

    interface OnClickListener {
        fun onClick(security: ShortSecurityNetworkModel)
    }
}
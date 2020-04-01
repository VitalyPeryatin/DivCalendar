package com.infinity_coder.divcalendar.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_security_portfolio.*

class SecurityRecyclerAdapter(
    private val onClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<SecurityRecyclerAdapter.SecurityViewHolder>() {

    private var securityList: List<SecurityPackageDbModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_security_portfolio, parent, false)
        return SecurityViewHolder(view, onClickListener)
    }

    override fun getItemCount(): Int = securityList.size

    override fun onBindViewHolder(holder: SecurityViewHolder, position: Int) {
        holder.bind(securityList[position])
    }

    fun setSecurities(securities: List<SecurityPackageDbModel>) {
        this.securityList = securities
        notifyDataSetChanged()
    }

    class SecurityViewHolder(
        override val containerView: View,
        private val onClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val resources = containerView.resources

        fun bind(securityPackage: SecurityPackageDbModel) {
            securityPortfolioCardLayout.setOnClickListener {
                onClickListener?.onItemClick(securityPackage)
            }
            val securityColor =
                SecurityTypeDelegate.getColor(containerView.context, securityPackage.type)
            securityTypeView.setBackgroundColor(securityColor)
            nameTextView.text = securityPackage.name
            countTextView.text = resources.getString(R.string.sec_count, securityPackage.count)
            totalPriceTextView.text =
                resources.getString(R.string.value_currency_rub, securityPackage.totalPrice)
            yearYieldTextView.text =
                resources.getString(R.string.yield_in_year, securityPackage.yearYield)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(securityPackage: SecurityPackageDbModel)
    }
}
package com.infinity_coder.divcalendar.presentation.portfolio.changeportfolio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_portfolio.*

class ChangePortfolioRecyclerAdapter(
    var onItemClickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ChangePortfolioRecyclerAdapter.ChangePortfolioViewHolder>() {

    private var portfolios: List<PortfolioDbModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangePortfolioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_portfolio, parent, false)
        return ChangePortfolioViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return portfolios.size
    }

    override fun onBindViewHolder(holder: ChangePortfolioViewHolder, position: Int) {
        holder.bind(portfolios[position])
    }

    fun setPortfolios(portfolios: List<PortfolioDbModel>) {
        this.portfolios = portfolios
        notifyDataSetChanged()
    }

    class ChangePortfolioViewHolder(
        override val containerView: View,
        var onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(portfolio: PortfolioDbModel) {
            nameTextView.text = portfolio.name
            containerView.setOnClickListener {
                onItemClickListener?.onItemClick(portfolio)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(portfolio: PortfolioDbModel)
    }
}
package com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.adapter

import android.view.View
import android.view.ViewGroup
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.presentation._common.base.BaseAdapter
import com.infinity_coder.divcalendar.presentation._common.extensions.inflate
import kotlinx.android.synthetic.main.item_portfolio.*

class ChangePortfolioRecyclerAdapter(
    var onItemClickListener: OnItemClickListener? = null
) : BaseAdapter<PortfolioDbModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangePortfolioViewHolder {
        val view = parent.inflate(R.layout.item_portfolio)
        val viewHolder = ChangePortfolioViewHolder(view)
        viewHolder.containerView.setOnClickListener {
            onItemClickListener?.onItemClick(items[viewHolder.adapterPosition])
        }
        viewHolder.editButton.setOnClickListener {
            onItemClickListener?.onEdit(items[viewHolder.adapterPosition])
        }
        viewHolder.deleteButton.setOnClickListener {
            onItemClickListener?.onDelete(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    class ChangePortfolioViewHolder(override val containerView: View) : BaseAdapter.BaseViewHolder<PortfolioDbModel>(containerView) {

        override fun bind(model: PortfolioDbModel, position: Int) {
            nameTextView.text = model.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(portfolio: PortfolioDbModel)

        fun onDelete(portfolio: PortfolioDbModel)

        fun onEdit(portfolio: PortfolioDbModel)
    }
}
package com.infinity_coder.divcalendar.presentation.searchstocks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_stock_search.*

class StockRecyclerAdapter(
    private var clickListener: OnClickListener? = null
) : RecyclerView.Adapter<StockRecyclerAdapter.StockViewHolder>() {

    private var stockList: List<ShortStockNetworkModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_stock_search, parent, false)
        return StockViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int = stockList.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stockList[position])
    }

    fun setStocks(stocks: List<ShortStockNetworkModel>) {
        this.stockList = stocks
        notifyDataSetChanged()
    }

    class StockViewHolder(
        override val containerView: View,
        private var clickListener: OnClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(stock: ShortStockNetworkModel) {
            nameTextView.text = stock.name
            tickerTextView.text = stock.secid

            containerView.setOnClickListener {
                clickListener?.onClick(stock)
            }
        }
    }

    interface OnClickListener {
        fun onClick(stock: ShortStockNetworkModel)
    }
}
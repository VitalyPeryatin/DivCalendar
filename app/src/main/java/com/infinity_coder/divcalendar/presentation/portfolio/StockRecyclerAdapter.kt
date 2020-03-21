package com.infinity_coder.divcalendar.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import kotlinx.android.extensions.LayoutContainer

class StockRecyclerAdapter : RecyclerView.Adapter<StockRecyclerAdapter.StockViewHolder>() {

    private var stockList: List<ShortStockNetworkModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_stock_portfolio, parent, false)
        return StockViewHolder(view)
    }

    override fun getItemCount(): Int = stockList.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {

    }

    fun setStocks(stocks: List<ShortStockNetworkModel>) {
        this.stockList = stocks
        notifyDataSetChanged()
    }

    class StockViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(stock: ShortStockNetworkModel) {

        }
    }
}
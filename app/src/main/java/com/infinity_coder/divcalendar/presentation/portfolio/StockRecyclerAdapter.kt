package com.infinity_coder.divcalendar.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_stock_portfolio.*

class StockRecyclerAdapter : RecyclerView.Adapter<StockRecyclerAdapter.StockViewHolder>() {

    private var stockList: List<SecPackageDbModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_stock_portfolio, parent, false)
        return StockViewHolder(view)
    }

    override fun getItemCount(): Int = stockList.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stockList[position])
    }

    fun setStocks(stocks: List<SecPackageDbModel>) {
        this.stockList = stocks
        notifyDataSetChanged()
    }

    class StockViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val resources = containerView.resources

        fun bind(stockPackage: SecPackageDbModel) {
            nameTextView.text = stockPackage.name
            countTextView.text = resources.getString(R.string.sec_count, stockPackage.count)
            totalPriceTextView.text = resources.getString(R.string.value_currency_rub, stockPackage.totalPrice)
            yearYieldTextView.text = resources.getString(R.string.yield_in_year, stockPackage.yearYield)
        }
    }
}
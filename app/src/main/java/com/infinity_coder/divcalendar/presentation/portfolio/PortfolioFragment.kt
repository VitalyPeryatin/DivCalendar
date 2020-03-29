package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.searchstocks.SearchStocksActivity
import kotlinx.android.synthetic.main.fragment_portfolio.*

class PortfolioFragment : Fragment() {

    private val viewModel: PortfolioViewModel by lazy {
        viewModel {
            PortfolioViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stocksRecyclerView.layoutManager = LinearLayoutManager(context)
        stocksRecyclerView.adapter = StockRecyclerAdapter()

        portfolioToolbar.title = context!!.resources.getString(R.string.portfolio)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(portfolioToolbar)

        viewModel.getStocksLiveData().observe(viewLifecycleOwner, Observer {
            setStocks(it)
        })

        addStockButton.setOnClickListener {
            val intent = SearchStocksActivity.getIntent(context!!)
            startActivity(intent)
        }
    }

    private fun setStocks(stocks: List<StockPackageDbModel>) {
        val adapter = stocksRecyclerView.adapter as? StockRecyclerAdapter
        adapter?.setStocks(stocks)
    }
}
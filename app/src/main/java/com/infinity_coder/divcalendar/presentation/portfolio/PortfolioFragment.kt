package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
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

        addStockButton.setOnClickListener {
            val intent = SearchStocksActivity.getIntent(context!!)
            startActivity(intent)
        }
    }
}
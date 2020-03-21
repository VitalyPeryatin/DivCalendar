package com.infinity_coder.divcalendar.presentation.searchstocks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.activity_search_stocks.*

class SearchStocksActivity : AppCompatActivity() {

    private val viewModel: SearchStocksViewModel by lazy {
        viewModel {
            SearchStocksViewModel()
        }
    }

    private val stockClickListener = object : StockRecyclerAdapter.OnClickListener {
        override fun onClick(stock: ShortStockNetworkModel) {
            Toast.makeText(this@SearchStocksActivity, stock.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stocks)

        setSupportActionBar(searchStocksToolbar)

        stocksRecyclerView.layoutManager = LinearLayoutManager(this)
        stocksRecyclerView.adapter = StockRecyclerAdapter(stockClickListener)

        viewModel.getStocksLiveData().observe(this, Observer { stocks ->
            setStocks(stocks)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.actionSearch)
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText ?: ""
                viewModel.requestStocksByQuery(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setStocks(stocks: List<ShortStockNetworkModel>) {
        val adapter = stocksRecyclerView.adapter as? StockRecyclerAdapter
        adapter?.setStocks(stocks)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchStocksActivity::class.java)
        }
    }
}
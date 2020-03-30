package com.infinity_coder.divcalendar.presentation.searchstocks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.StockPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.activity_search_stocks.*

class SearchStocksActivity : AppCompatActivity(), AddStockBottomDialog.OnClickListener {

    private var addStockDialog: AddStockBottomDialog? = null

    val viewModel: SearchStocksViewModel by lazy {
        viewModel {
            SearchStocksViewModel()
        }
    }

    private val stockClickListener = object : StockRecyclerAdapter.OnClickListener {
        override fun onClick(stock: ShortStockNetworkModel) {
            addStockDialog = AddStockBottomDialog.newInstance(stock)
            addStockDialog?.show(
                supportFragmentManager.beginTransaction(),
                AddStockBottomDialog::class.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stocks)

        stocksRecyclerView.layoutManager = LinearLayoutManager(this)
        stocksRecyclerView.adapter = StockRecyclerAdapter(stockClickListener)

        backButton.setOnClickListener {
            onBackPressed()
        }

        closeButton.setOnClickListener {
            queryEditText.text.clear()
        }

        viewModel.requestStocksByQuery("")

        queryEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                if (s.isNotEmpty()) {
                    closeButton.visibility = View.VISIBLE
                } else {
                    closeButton.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.requestStocksByQuery(s.toString())
            }

        })

        viewModel.getFilteredStocksLiveData().observe(this, Observer { stocks ->
            setStocks(stocks)
        })
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

    override fun onAddStockPackageClick(stockPackage: StockPackageDbModel) {
        viewModel.appendStockPackage(stockPackage)
        dismissAddStockDialog()
    }

    private fun dismissAddStockDialog() {
        addStockDialog?.dismiss()
        addStockDialog = null
    }
}
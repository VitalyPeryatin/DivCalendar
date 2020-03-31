package com.infinity_coder.divcalendar.presentation.search

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
import com.infinity_coder.divcalendar.data.db.model.SecPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.ShortStockNetworkModel
import com.infinity_coder.divcalendar.presentation.search.addsec.AddSecBottomDialog
import kotlinx.android.synthetic.main.activity_search_stocks.*

class SearchSecActivity : AppCompatActivity(), AddSecBottomDialog.OnClickListener {

    val viewModel: SearchSecViewModel by lazy { SearchSecViewModel() }

    private var addStockDialog: AddSecBottomDialog? = null

    private val stockClickListener = object : SecRecyclerAdapter.OnClickListener {
        override fun onClick(stock: ShortStockNetworkModel) {
            addStockDialog = AddSecBottomDialog.newInstance(stock)
            addStockDialog?.show(
                supportFragmentManager.beginTransaction(),
                AddSecBottomDialog::class.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_stocks)

        initUI()

        viewModel.requestStocksByQuery("")
        viewModel.getFilteredStocksLiveData().observe(this, Observer(this::setStocks))
    }

    private fun initUI() {
        stocksRecyclerView.layoutManager = LinearLayoutManager(this)
        stocksRecyclerView.adapter = SecRecyclerAdapter(stockClickListener)

        backButton.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryEditText.text.clear()
        }
        queryEditText.addTextChangedListener(getQueryTextWatcher())
    }

    private fun getQueryTextWatcher() = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s == null) return

            if (s.isNotEmpty()) {
                clearButton.visibility = View.VISIBLE
            } else {
                clearButton.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.requestStocksByQuery(s.toString())
        }

    }

    private fun setStocks(stocks: List<ShortStockNetworkModel>) {
        val adapter = stocksRecyclerView.adapter as? SecRecyclerAdapter
        adapter?.setStocks(stocks)
    }

    override fun onAddSecPackageClick(stockPackage: SecPackageDbModel) {
        viewModel.appendStockPackage(stockPackage)
        dismissAddStockDialog()
    }

    private fun dismissAddStockDialog() {
        addStockDialog?.dismiss()
        addStockDialog = null
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchSecActivity::class.java)
        }
    }
}
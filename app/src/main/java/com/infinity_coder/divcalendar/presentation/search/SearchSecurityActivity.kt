package com.infinity_coder.divcalendar.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.SecurityMarketDelegate
import com.infinity_coder.divcalendar.presentation._common.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.search.adapters.SearchSecurityPagerAdapter
import kotlinx.android.synthetic.main.activity_search_securities.*

class SearchSecurityActivity : AppCompatActivity() {

    val viewModel: SearchSecurityViewModel by lazy {
        viewModel { SearchSecurityViewModel() }
    }

    private lateinit var securityPagerAdapter: SearchSecurityPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_securities)

        initUI()
    }

    private fun initUI() {
        securityPagerAdapter = SearchSecurityPagerAdapter(this)
        viewPager.adapter = securityPagerAdapter

        segmentedButton {
            initialCheckedIndex = viewModel.getCurrentMarketIndex()

            initWithItems { SecurityMarketDelegate.getTitles(context) }

            onSegmentChecked { segment -> setMarketByTitle(segment.text.toString()) }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = SecurityTypeDelegate.getTitles(this)[position]
        }.attach()

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
            executeQuery(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun executeQuery(s: Editable?) {
        val query = s.toString()
        if (query.isBlank()) {
            clearButton.visibility = View.GONE
        } else {
            clearButton.visibility = View.VISIBLE
        }
        viewModel.executeQuery(query)
    }

    private fun setMarketByTitle(title: String) {
        val market = SecurityMarketDelegate.getMarketByTitle(this@SearchSecurityActivity, title)
        viewModel.setMarket(market)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchSecurityActivity::class.java)
        }
    }
}
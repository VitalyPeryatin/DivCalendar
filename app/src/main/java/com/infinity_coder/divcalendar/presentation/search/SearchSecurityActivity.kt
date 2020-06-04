package com.infinity_coder.divcalendar.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.base.AbstractSubscriptionActivity
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityMarketDelegate
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityTypeDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.visibility
import com.infinity_coder.divcalendar.presentation._common.text_watchers.OpenTextWatcher
import com.infinity_coder.divcalendar.presentation.search.adapters.SearchSecurityPagerAdapter
import kotlinx.android.synthetic.main.activity_search_securities.*

class SearchSecurityActivity : AbstractSubscriptionActivity() {

    val viewModel: SearchSecurityViewModel by lazy {
        ViewModelProvider(this).get(SearchSecurityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_securities)

        initUI()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun initUI() {
        viewPager.adapter = SearchSecurityPagerAdapter(SecurityTypeDelegate.securityTypes, this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = SecurityTypeDelegate.getTitle(this, position)
        }.attach()

        initSegmentedButton()

        initQueryEditText()

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            queryEditText.text.clear()
        }
    }

    private fun initSegmentedButton() {
        segmentedButton {
            initialCheckedIndex = SecurityMarketDelegate.getMarketIndex(viewModel.getCurrentMarket())

            initWithItems { SecurityMarketDelegate.getTitles(context) }

            onSegmentChecked {
                val market = SecurityMarketDelegate.getMarketByTitle(context, it.text.toString())
                viewModel.setMarket(market)
            }
        }
    }

    private fun initQueryEditText() {
        queryEditText.addTextChangedListener(object : OpenTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                clearButton.visibility(!query.isBlank())
                viewModel.executeQuery(query)
            }
        })

        queryEditText.setOnEditorActionListener { _, _, _ ->
            queryEditText.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(queryEditText.windowToken, 0)
            return@setOnEditorActionListener true
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchSecurityActivity::class.java)
        }
    }
}
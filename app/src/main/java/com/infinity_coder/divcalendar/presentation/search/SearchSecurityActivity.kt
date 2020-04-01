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
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.search.addsecurity.AddSecurityBottomDialog
import kotlinx.android.synthetic.main.activity_search_securities.*

class SearchSecurityActivity : AppCompatActivity(), AddSecurityBottomDialog.OnClickListener {

    val viewModel: SearchSecurityViewModel by lazy {
        viewModel { SearchSecurityViewModel() }
    }

    private var addSecurityDialog: AddSecurityBottomDialog? = null

    private val secClickListener = object : SecurityRecyclerAdapter.OnClickListener {
        override fun onClick(security: SecurityNetworkModel) {
            addSecurityDialog = AddSecurityBottomDialog.newInstance(security)
            addSecurityDialog?.show(
                supportFragmentManager,
                AddSecurityBottomDialog::class.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_securities)

        initUI()

        viewModel.searchedSecurities.observe(this, Observer(this::setSecurities))
        viewModel.state.observe(this, Observer(this::setState))
        viewModel.securityType.observe(this, Observer(this::setSecurityType))
        viewModel.market.observe(this, Observer(this::setMarket))
    }

    private fun initUI() {
        securitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(secClickListener)

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
            viewModel.search(s.toString())
        }

    }

    private fun setSecurities(securities: List<SecurityNetworkModel>) {
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.setSecurities(securities)
    }

    private fun dismissAddSecurityDialog() {
        addSecurityDialog?.dismiss()
        addSecurityDialog = null
    }

    private fun setState(state: Int) {
        when (state) {
            SearchSecurityViewModel.VIEW_STATE_SEARCH_SECURITY_CONTENT -> showContent()

            SearchSecurityViewModel.VIEW_STATE_SEARCH_SECURITY_LOADING -> showLoading()

            SearchSecurityViewModel.VIEW_STATE_SEARCH_SECURITY_EMPTY -> showEmptyLayout()

            SearchSecurityViewModel.VIEW_STATE_SEARCH_SECURITY_NO_NETWORK -> showNoNetwork()

            SearchSecurityViewModel.VIEW_STATE_SEARCH_SECURITY_START_SEARCH -> showStartSearchLayout()
        }
    }

    private fun showContent() {
        contentLayout.visibility = View.VISIBLE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        startSearchLayout.visibility = View.GONE
    }

    private fun showLoading() {
        contentLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        startSearchLayout.visibility = View.GONE
    }

    private fun showEmptyLayout() {
        contentLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
        startSearchLayout.visibility = View.GONE
    }

    private fun showNoNetwork() {
        contentLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        startSearchLayout.visibility = View.GONE
    }

    private fun showStartSearchLayout() {
        contentLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        startSearchLayout.visibility = View.VISIBLE
    }

    override fun onAddSecPackageClick(securityPackage: SecurityPackageDbModel) {
        viewModel.appendSecurityPackage(securityPackage)
        dismissAddSecurityDialog()
    }

    private fun setSecurityType(type: String) {

    }

    private fun setMarket(market: String) {

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchSecurityActivity::class.java)
        }
    }
}
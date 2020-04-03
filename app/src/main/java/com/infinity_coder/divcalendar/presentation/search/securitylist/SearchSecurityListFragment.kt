package com.infinity_coder.divcalendar.presentation.search.securitylist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.data.network.model.SecurityNetworkModel
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityViewModel
import com.infinity_coder.divcalendar.presentation.search.adapters.SecurityRecyclerAdapter
import com.infinity_coder.divcalendar.presentation.search.addsecurity.AddSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_CONTENT
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_EMPTY
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_LOADING
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_NO_NETWORK
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_START_SEARCH
import kotlinx.android.synthetic.main.fragment_portfolio.securitiesRecyclerView
import kotlinx.android.synthetic.main.fragment_search_security_list.*

class SearchSecurityListFragment : Fragment(R.layout.fragment_search_security_list),
    AddSecurityBottomDialog.OnClickListener {

    private lateinit var parentViewModel: SearchSecurityViewModel
    private val viewModel: SearchSecurityListViewModel by lazy {
        viewModel { SearchSecurityListViewModel() }
    }

    private var addSecurityDialog: AddSecurityBottomDialog? = null
    private lateinit var parentActivity: SearchSecurityActivity

    private val secClickListener = object : SecurityRecyclerAdapter.OnClickListener {
        override fun onClick(security: SecurityNetworkModel) {
            addSecurityDialog = AddSecurityBottomDialog.newInstance(security)
            addSecurityDialog?.show(
                childFragmentManager,
                AddSecurityBottomDialog::class.toString()
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentActivity = context as SearchSecurityActivity
        parentViewModel = parentActivity.viewModel
        viewModel.securityType = arguments!!.getString(SECURITY_TYPE_ARGUMENT, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        showStartSearchLayout()

        parentViewModel.queryLiveData.observe(viewLifecycleOwner, Observer(this::updateQuery))
        parentViewModel.marketLiveData.observe(viewLifecycleOwner, Observer(this::updateMarket))
        viewModel.searchedSecurities.observe(viewLifecycleOwner, Observer(this::setSecurities))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))
    }

    override fun onResume() {
        super.onResume()
        searchIfResumed(getCurrentQuery(), getCurrentMarket())
    }

    private fun initUI() {
        securitiesRecyclerView.layoutManager = LinearLayoutManager(context)
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(secClickListener)
    }

    private fun updateQuery(query: String) {
        searchIfResumed(query, getCurrentMarket())
    }

    private fun updateMarket(market: String) {
        searchIfResumed(getCurrentQuery(), market)
    }

    private fun searchIfResumed(query: String, market: String) {
        if (isResumed) {
            viewModel.search(query, market)
        }
    }

    private fun getCurrentMarket(): String {
        return parentViewModel.marketLiveData.value ?: ""
    }

    private fun getCurrentQuery(): String {
        return parentViewModel.queryLiveData.value ?: ""
    }

    override fun onAddSecPackageClick(securityPackage: SecurityPackageDbModel) {
        viewModel.appendSecurityPackage(securityPackage)
        dismissAddSecurityDialog()
    }

    private fun setSecurities(securities: List<SecurityNetworkModel>) {
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.setSecurities(securities)
    }

    private fun setState(state: Int) {
        when (state) {
            VIEW_STATE_SEARCH_SECURITY_CONTENT -> showContent()

            VIEW_STATE_SEARCH_SECURITY_LOADING -> showLoading()

            VIEW_STATE_SEARCH_SECURITY_EMPTY -> showEmptyLayout()

            VIEW_STATE_SEARCH_SECURITY_NO_NETWORK -> showNoNetwork()

            VIEW_STATE_SEARCH_SECURITY_START_SEARCH -> showStartSearchLayout()
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

    private fun dismissAddSecurityDialog() {
        addSecurityDialog?.dismiss()
        addSecurityDialog = null
    }

    companion object {

        private const val SECURITY_TYPE_ARGUMENT = "securityType"

        fun newInstance(securityType: String): SearchSecurityListFragment {
            val fragment = SearchSecurityListFragment()
            fragment.arguments = bundleOf(SECURITY_TYPE_ARGUMENT to securityType)
            return fragment
        }
    }
}
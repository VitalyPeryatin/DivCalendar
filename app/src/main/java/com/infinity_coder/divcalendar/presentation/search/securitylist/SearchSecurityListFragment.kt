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
import kotlin.properties.Delegates

class SearchSecurityListFragment : Fragment(R.layout.fragment_search_security_list),
    AddSecurityBottomDialog.OnDialogClickListener {

    private var currentState by Delegates.observable(
        VIEW_STATE_SEARCH_SECURITY_START_SEARCH, { _, old, new -> changeViewState(new, old) }
    )

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
        currentState = state
    }

    private fun changeViewState(newState: Int, oldState: Int) {
        when (oldState) {
            VIEW_STATE_SEARCH_SECURITY_CONTENT -> contentLayout.visibility = View.GONE
            VIEW_STATE_SEARCH_SECURITY_LOADING -> loadingLayout.visibility = View.GONE
            VIEW_STATE_SEARCH_SECURITY_EMPTY -> emptyLayout.visibility = View.GONE
            VIEW_STATE_SEARCH_SECURITY_NO_NETWORK -> noNetworkLayout.visibility = View.GONE
            VIEW_STATE_SEARCH_SECURITY_START_SEARCH -> startSearchLayout.visibility = View.GONE
        }

        when (newState) {
            VIEW_STATE_SEARCH_SECURITY_CONTENT -> contentLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_LOADING -> loadingLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_EMPTY -> emptyLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_START_SEARCH -> startSearchLayout.visibility = View.VISIBLE
        }
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
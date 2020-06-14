package com.infinity_coder.divcalendar.presentation.search.securitylist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.SecurityNetModel
import com.infinity_coder.divcalendar.presentation._common.extensions.executeIfResumed
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityViewModel
import com.infinity_coder.divcalendar.presentation.search.adapters.SecurityRecyclerAdapter
import com.infinity_coder.divcalendar.presentation.search.addsecurity.AddSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_CONTENT
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_LOADING
import com.infinity_coder.divcalendar.presentation.search.securitylist.SearchSecurityListViewModel.Companion.VIEW_STATE_SEARCH_SECURITY_NO_NETWORK
import kotlinx.android.synthetic.main.fragment_portfolio.securitiesRecyclerView
import kotlinx.android.synthetic.main.fragment_search_security_list.*

class SearchSecurityListFragment : Fragment(R.layout.fragment_search_security_list) {

    val viewModel: SearchSecurityListViewModel by lazy {
        ViewModelProvider(this).get(SearchSecurityListViewModel::class.java)
    }

    private lateinit var parentViewModel: SearchSecurityViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentViewModel = (context as SearchSecurityActivity).viewModel

        viewModel.securityType = requireArguments().getString(SECURITY_TYPE_ARGUMENT, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.queryLiveData.observe(viewLifecycleOwner, Observer(this::updateQuery))
        parentViewModel.marketLiveData.observe(viewLifecycleOwner, Observer(this::updateMarket))
        viewModel.searchedSecurities.observe(viewLifecycleOwner, Observer(this::setSecurities))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))

        initUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.search(parentViewModel.getCurrentQuery(), parentViewModel.getCurrentMarket())
    }

    private fun updateQuery(query: String) {
        executeIfResumed { viewModel.search(query, parentViewModel.getCurrentMarket()) }
    }

    private fun updateMarket(market: String) {
        executeIfResumed { viewModel.search(parentViewModel.getCurrentQuery(), market) }
    }

    private fun setSecurities(securities: List<SecurityNetModel>) {
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.updateItems(securities)
    }

    private fun setState(state: Int) {
        contentLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE

        when (state) {
            VIEW_STATE_SEARCH_SECURITY_CONTENT -> contentLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_LOADING -> loadingLayout.visibility = View.VISIBLE
            VIEW_STATE_SEARCH_SECURITY_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
        }
    }

    private fun initUI() {
        val securityClickListener = object : SecurityRecyclerAdapter.OnClickListener {
            override fun onClick(security: SecurityNetModel) {
                val addSecurityDialog = AddSecurityBottomDialog.newInstance(security)
                addSecurityDialog.show(childFragmentManager, AddSecurityBottomDialog.TAG)
            }
        }
        securitiesRecyclerView.layoutManager = LinearLayoutManager(context)
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(securityClickListener)
    }

    companion object {
        private const val SECURITY_TYPE_ARGUMENT = "securityType"

        fun newInstance(securityType: String): SearchSecurityListFragment {
            return SearchSecurityListFragment().apply {
                arguments = bundleOf(SECURITY_TYPE_ARGUMENT to securityType)
            }
        }
    }
}
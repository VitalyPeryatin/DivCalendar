package com.infinity_coder.divcalendar.presentation.portfolio

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioWithSecurities
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.presentation._common.executeIfSubscribed
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.buysubscription.PremiumSubscriptionObservable
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.managesecurity.ChangeSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*

class PortfolioFragment : Fragment(R.layout.fragment_portfolio),
    ChangeSecurityBottomDialog.OnClickListener,
    ChangePortfolioBottomDialog.OnChangePortfolioClickListener {

    private var changePackageDialog: ChangeSecurityBottomDialog? = null
    private var premiumSubscriptionObservable: PremiumSubscriptionObservable? = null

    private val viewModel: PortfolioViewModel by lazy {
        viewModel { PortfolioViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.portfolio, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePortfolioItem -> {
                executeIfSubscribed(this::openChangePortfolioDialog)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun openChangePortfolioDialog() {
        val changePortfolioDialog = ChangePortfolioBottomDialog.newInstance()
        changePortfolioDialog.show(childFragmentManager, ChangePortfolioBottomDialog::class.toString())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is PremiumSubscriptionObservable) {
            premiumSubscriptionObservable = parentFragment
        } else if (context is PremiumSubscriptionObservable) {
            premiumSubscriptionObservable = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.portfolio.observe(viewLifecycleOwner, Observer(this::setPortfolio))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadSecurities()
    }

    private fun initUI() {
        portfolioToolbar.title = context!!.resources.getString(R.string.portfolio)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(portfolioToolbar)

        emptyLayout.emptyTextView.text = resources.getText(R.string.collect_portfolio)

        securitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(getOnItemClickListener())

        addSecurityButton.setOnClickListener {
            val intent = SearchSecurityActivity.getIntent(requireContext())
            startActivity(intent)
        }
    }

    private fun getOnItemClickListener() = object : SecurityRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(securityPackage: SecurityPackageDbModel) {
            openChangePackageDialog(securityPackage)
        }
    }

    private fun openChangePackageDialog(securityPackage: SecurityPackageDbModel) {
        changePackageDialog = ChangeSecurityBottomDialog.newInstance(securityPackage)
        changePackageDialog?.show(childFragmentManager, ChangeSecurityBottomDialog::class.toString())
    }

    private fun setPortfolio(portfolio: PortfolioWithSecurities) {
        portfolioToolbar.subtitle = portfolio.portfolio.name
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.setSecurities(portfolio.securities)
    }

    private fun setState(state: Int) {
        contentLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE

        when (state) {
            PortfolioViewModel.VIEW_STATE_PORTFOLIO_CONTENT -> contentLayout.visibility = View.VISIBLE
            PortfolioViewModel.VIEW_STATE_PORTFOLIO_EMPTY -> emptyLayout.visibility = View.VISIBLE
        }
    }

    override fun onChangePackageClick(securityPackage: SecurityPackageDbModel) {
        viewModel.changeSecurityPackage(securityPackage)
        changePackageDialog?.dismiss()
    }

    override fun onPortfolioChange() {
        viewModel.loadSecurities()
    }
}
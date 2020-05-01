package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.presentation._common.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.extensions.executeIfSubscribed
import com.infinity_coder.divcalendar.presentation._common.extensions.setActionBar
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.managesecurity.ChangeSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.fragment_portfolio.emptyLayout
import kotlinx.android.synthetic.main.fragment_portfolio.rubRadioButton
import kotlinx.android.synthetic.main.fragment_portfolio.usdRadioButton
import kotlinx.android.synthetic.main.layout_stub_empty.view.*

class PortfolioFragment : Fragment(R.layout.fragment_portfolio),
    ChangeSecurityBottomDialog.OnClickListener,
    ChangePortfolioBottomDialog.OnChangePortfolioClickListener {

    private var changePackageDialog: ChangeSecurityBottomDialog? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.portfolio.observe(viewLifecycleOwner, Observer(this::setPortfolio))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))
        viewModel.totalPortfolioCost.observe(viewLifecycleOwner, Observer(this::setTotalPortfolioCost))
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadSecurities()
    }

    private fun initUI() {
        portfolioToolbar.title = context!!.resources.getString(R.string.portfolio)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(portfolioToolbar)

        initCurrencyRadioGroup()

        emptyLayout.emptyTextView.text = resources.getText(R.string.collect_portfolio)

        securitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(getOnItemClickListener())
        securitiesRecyclerView.addOnScrollListener(getScrollListener())

        addSecurityButton.setOnClickListener {
            val intent = SearchSecurityActivity.getIntent(requireContext())
            startActivity(intent)
        }
    }

    private fun initCurrencyRadioGroup() {
        val checkedCurrencyRadioButton = when (viewModel.getDisplayCurrency()) {
            RateRepository.RUB_RATE -> rubRadioButton
            RateRepository.USD_RATE -> usdRadioButton
            else -> null
        }
        checkedCurrencyRadioButton?.isChecked = true

        rubRadioButton.setOnCheckedChangeListener(this::checkCurrency)
        usdRadioButton.setOnCheckedChangeListener(this::checkCurrency)
    }

    private fun checkCurrency(radioButton: CompoundButton, isChecked: Boolean) {
        if (!isChecked) return

        when (radioButton) {
            rubRadioButton -> viewModel.setDisplayCurrency(RateRepository.RUB_RATE)
            usdRadioButton -> viewModel.setDisplayCurrency(RateRepository.USD_RATE)
        }
    }

    private fun getOnItemClickListener() = object : SecurityRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(securityPackage: SecurityDbModel) {
            openChangePackageDialog(securityPackage)
        }
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && !addSecurityButton.isShown) {
                    addSecurityButton.show()
                } else if (dy > 0 && addSecurityButton.isShown) {
                    addSecurityButton.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            }
        }
    }

    private fun openChangePackageDialog(securityPackage: SecurityDbModel) {
        changePackageDialog = ChangeSecurityBottomDialog.newInstance(securityPackage)
        changePackageDialog?.show(childFragmentManager, ChangeSecurityBottomDialog::class.toString())
    }

    private fun setPortfolio(portfolio: PortfolioDbModel) {
        portfolioToolbar.subtitle = portfolio.name
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

    private fun setTotalPortfolioCost(totalPortfolioCost: Double) {
        val currentCurrency = viewModel.getDisplayCurrency()
        val totalPortfolioCostWithCurrency = SecurityCurrencyDelegate.getValueWithCurrency(requireContext(), totalPortfolioCost, currentCurrency)
        totalPortfolioCostTextView.text = getString(R.string.total_portfolio_cost, totalPortfolioCostWithCurrency)
        totalPortfolioCostTextView.isSelected = true
    }

    override fun onChangePackageClick(securityPackage: SecurityDbModel) {
        viewModel.changeSecurityPackage(securityPackage)
        changePackageDialog?.dismiss()
    }

    override fun onPortfolioChange() {
        viewModel.loadSecurities()
    }
}
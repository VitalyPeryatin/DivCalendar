package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation._common.base.UpdateCallback
import com.infinity_coder.divcalendar.presentation._common.extensions.executeIfSubscribed
import com.infinity_coder.divcalendar.presentation.main.MainActivity
import com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changeportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changesecurity.ChangeSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changesorting.SortingPortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsFragment
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*
import java.math.BigDecimal

class PortfolioFragment : Fragment(R.layout.fragment_portfolio),
    ChangeSecurityBottomDialog.ChangeSecurityCallback,
    ChangePortfolioBottomDialog.OnChangePortfolioClickListener,
    SortingPortfolioBottomDialog.SortingPortfolioCallback,
    UpdateCallback {

    val viewModel: PortfolioViewModel by lazy {
        ViewModelProvider(this).get(PortfolioViewModel::class.java)
    }

    override fun onUpdate() {
        initCurrencyRadioGroup()
        viewModel.loadSecurities()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> {
                val fragment = SettingsFragment.newInstance()
                (requireActivity() as MainActivity).startFragment(fragment)
            }

            R.id.changePortfolioItem -> executeIfSubscribed {
                val changePortfolioDialog = ChangePortfolioBottomDialog.newInstance()
                changePortfolioDialog.show(childFragmentManager, ChangePortfolioBottomDialog.TAG)
            }

            R.id.sortPortfolioItem -> {
                val sortingPortfolioDialog = SortingPortfolioBottomDialog.newInstance()
                sortingPortfolioDialog.show(childFragmentManager, SortingPortfolioBottomDialog.TAG)
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.portfolio.observe(viewLifecycleOwner, Observer(this::setPortfolio))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))
        viewModel.totalPortfolioCost.observe(viewLifecycleOwner, Observer(this::setTotalPortfolioCost))

        initUI()
    }

    private fun setPortfolio(portfolio: PortfolioDbModel) {
        portfolioToolbar.subtitle = portfolio.name
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.updateItems(portfolio.securities)
    }

    private fun setState(state: Int) {
        contentLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE

        when (state) {
            PortfolioViewModel.VIEW_STATE_PORTFOLIO_CONTENT -> contentLayout.visibility = View.VISIBLE
            PortfolioViewModel.VIEW_STATE_PORTFOLIO_EMPTY -> emptyLayout.visibility = View.VISIBLE
        }
    }

    private fun setTotalPortfolioCost(totalPortfolioCost: BigDecimal) {
        val currentCurrency = viewModel.getDisplayCurrency()
        val totalPortfolioCostWithCurrency = SecurityCurrencyDelegate.getValueWithCurrencyConsiderCopecks(requireContext(), totalPortfolioCost, currentCurrency)
        totalPortfolioCostTextView.text = getString(R.string.total_portfolio_cost, totalPortfolioCostWithCurrency)
        totalPortfolioCostTextView.isSelected = true
    }

    private fun initUI() {
        portfolioToolbar.setTitle(R.string.portfolio)
        portfolioToolbar.inflateMenu(R.menu.portfolio)
        portfolioToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected)

        initCurrencyRadioGroup()
        rubRadioButton.setOnCheckedChangeListener(this::checkCurrency)
        usdRadioButton.setOnCheckedChangeListener(this::checkCurrency)

        emptyLayout.emptyTextView.text = resources.getText(R.string.collect_portfolio)

        securitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(getOnItemClickListener())
        securitiesRecyclerView.addOnScrollListener(getScrollListener())

        addSecurityButton.setOnClickListener {
            val fragment = SearchSecurityFragment.newInstance()
            (requireActivity() as MainActivity).startFragment(fragment)
        }
    }

    private fun initCurrencyRadioGroup() {
        val checkedCurrencyRadioButton = when (viewModel.getDisplayCurrency()) {
            RateRepository.RUB_RATE -> rubRadioButton
            RateRepository.USD_RATE -> usdRadioButton
            else -> null
        }
        checkedCurrencyRadioButton?.isChecked = true
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
            val changePackageDialog = ChangeSecurityBottomDialog.newInstance(securityPackage)
            changePackageDialog.show(childFragmentManager, ChangeSecurityBottomDialog.TAG)
        }
    }

    private fun getScrollListener() = object : RecyclerView.OnScrollListener() {
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

    override fun onChangeSecurityPackage() {
        viewModel.loadSecurities()
    }

    override fun onPortfolioChange() {
        viewModel.loadSecurities()
    }

    override fun onChangeSortType() {
        viewModel.loadSecurities()
    }
}
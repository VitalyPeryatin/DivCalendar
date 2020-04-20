package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.data.db.model.SecurityDbModel
import com.infinity_coder.divcalendar.presentation._common.extensions.executeIfSubscribed
import com.infinity_coder.divcalendar.presentation._common.extensions.setActionBar
import com.infinity_coder.divcalendar.presentation._common.extensions.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.managesecurity.ChangeSecurityBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import kotlinx.android.synthetic.main.fragment_portfolio.*
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
        securitiesRecyclerView.addOnScrollListener(getScrollListener())

        addSecurityButton.setOnClickListener {
            val intent = SearchSecurityActivity.getIntent(requireContext())
            startActivity(intent)
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

    override fun onChangePackageClick(securityPackage: SecurityDbModel) {
        viewModel.changeSecurityPackage(securityPackage)
        changePackageDialog?.dismiss()
    }

    override fun onPortfolioChange() {
        viewModel.loadSecurities()
    }
}
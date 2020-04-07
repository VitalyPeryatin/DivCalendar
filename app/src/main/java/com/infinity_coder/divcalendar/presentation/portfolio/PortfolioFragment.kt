package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.changepackage.ChangePackageBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*

class PortfolioFragment : Fragment(R.layout.fragment_portfolio), ChangePackageBottomDialog.OnClickListener {

    private var changePackageDialog: ChangePackageBottomDialog? = null

    private val viewModel: PortfolioViewModel by lazy {
        viewModel { PortfolioViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.securities.observe(viewLifecycleOwner, Observer(this::setSecurities))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::setState))
    }

    private fun initUI() {
        portfolioToolbar.title = context!!.resources.getString(R.string.portfolio)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(portfolioToolbar)

        emptyLayout.emptyTextView.text = resources.getText(R.string.collect_portfolio)

        securitiesRecyclerView.layoutManager = LinearLayoutManager(context)
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(getOnItemClickListener())

        addSecurityButton.setOnClickListener {
            val intent = SearchSecurityActivity.getIntent(context!!)
            startActivity(intent)
        }
    }

    private fun getOnItemClickListener() = object : SecurityRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(securityPackage: SecurityPackageDbModel) {
            openChangePackageDialog(securityPackage)
        }
    }

    private fun openChangePackageDialog(securityPackage: SecurityPackageDbModel) {
        changePackageDialog = ChangePackageBottomDialog.newInstance(securityPackage)
        changePackageDialog?.show(childFragmentManager, ChangePackageBottomDialog::class.toString())
    }

    private fun setSecurities(securities: List<SecurityPackageDbModel>) {
        val adapter = securitiesRecyclerView.adapter as? SecurityRecyclerAdapter
        adapter?.setSecurities(securities)
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
}
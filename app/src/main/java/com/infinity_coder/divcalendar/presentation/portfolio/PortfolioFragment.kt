package com.infinity_coder.divcalendar.presentation.portfolio

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.SecurityPackageDbModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.changepackage.ChangePackageBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.changeportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.search.SearchSecurityActivity
import kotlinx.android.synthetic.main.fragment_portfolio.*

class PortfolioFragment : Fragment(), ChangePackageBottomDialog.OnClickListener {

    private var changePackageDialog: ChangePackageBottomDialog? = null

    private val viewModel: PortfolioViewModel by lazy {
        viewModel { PortfolioViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.portfolio, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePortfolioItem -> openChangePortfolioDialog()
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

        securitiesRecyclerView.layoutManager = LinearLayoutManager(context)
        securitiesRecyclerView.adapter = SecurityRecyclerAdapter(getOnItemClickListener())

        portfolioToolbar.title = context!!.resources.getString(R.string.portfolio)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(portfolioToolbar)

        viewModel.getSecuritiesLiveData().observe(viewLifecycleOwner, Observer(this::setSecurities))

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

    override fun onChangePackageClick(securityPackage: SecurityPackageDbModel) {
        viewModel.changeSecurityPackage(securityPackage)
        changePackageDialog?.dismiss()
    }
}
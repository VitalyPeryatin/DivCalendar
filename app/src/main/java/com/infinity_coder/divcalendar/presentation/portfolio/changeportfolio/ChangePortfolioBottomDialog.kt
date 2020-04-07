package com.infinity_coder.divcalendar.presentation.portfolio.changeportfolio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.changeportfolio.adapter.ChangePortfolioRecyclerAdapter
import com.infinity_coder.divcalendar.presentation.portfolio.createportfolio.CreatePortfolioDialog
import kotlinx.android.synthetic.main.bottom_dialog_change_portfolio.*

class ChangePortfolioBottomDialog : BottomDialog(),
    CreatePortfolioDialog.OnCreatePortfolioClickListener {

    private var clickListener: OnDialogClickListener? = null

    private val viewModel: ChangePortfolioViewModel by lazy {
        viewModel { ChangePortfolioViewModel() }
    }
    private val portfoliosAdapter = ChangePortfolioRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_change_portfolio, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnDialogClickListener) {
            clickListener = parentFragment
        } else if (context is OnDialogClickListener) {
            clickListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.portfolios.observe(viewLifecycleOwner, Observer(this::setPortfolios))

        viewModel.loadAllPortfolios()
    }

    private fun setPortfolios(portfolios: List<PortfolioDbModel>) {
        portfoliosAdapter.setPortfolios(portfolios)
    }

    private fun initUI() {
        portfoliosRecyclerView.layoutManager = LinearLayoutManager(context)
        portfoliosRecyclerView.adapter = portfoliosAdapter

        createNewPortfolioButton.setOnClickListener {
            showCreateNewPortfolioDialog()
        }
    }

    private fun showCreateNewPortfolioDialog() {
        val createPortfolioDialog = CreatePortfolioDialog.newInstance()
        createPortfolioDialog.show(childFragmentManager, CreatePortfolioDialog::class.toString())
    }

    companion object {
        fun newInstance(): ChangePortfolioBottomDialog {
            return ChangePortfolioBottomDialog()
        }
    }

    interface OnDialogClickListener {
    }

    override fun onPortfolioCreated() {
        viewModel.loadAllPortfolios()
    }
}
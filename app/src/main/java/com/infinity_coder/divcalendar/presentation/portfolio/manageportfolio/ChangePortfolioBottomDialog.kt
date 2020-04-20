package com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PortfolioDbModel
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.adapter.ChangePortfolioRecyclerAdapter
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.dialogs.CreatePortfolioDialog
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.dialogs.DeletePortfolioDialog
import com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.dialogs.RenamePortfolioDialog
import kotlinx.android.synthetic.main.bottom_dialog_change_portfolio.*

class ChangePortfolioBottomDialog : BottomDialog(),
    CreatePortfolioDialog.OnCreatePortfolioClickListener,
    DeletePortfolioDialog.DeletePortfolioClickListener,
    RenamePortfolioDialog.RenamePortfolioClickListener {

    private var clickListener: OnChangePortfolioClickListener? = null
    private var deletePortfolioDialog: DeletePortfolioDialog? = null

    val viewModel: ChangePortfolioViewModel by lazy {
        viewModel { ChangePortfolioViewModel() }
    }

    private val portfoliosAdapter = ChangePortfolioRecyclerAdapter(object : ChangePortfolioRecyclerAdapter.OnItemClickListener {
        override fun onItemClick(portfolio: PortfolioDbModel) {
            setCurrentPortfolio(portfolio.name)
        }

        override fun onDelete(portfolio: PortfolioDbModel) {
            viewModel.requestConfirmationOnDeletePortfolio(portfolio)
        }

        override fun onEdit(portfolio: PortfolioDbModel) {
            showRenamePortfolioDialog(portfolio)
        }
    })

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
        if (parentFragment is OnChangePortfolioClickListener) {
            clickListener = parentFragment
        } else if (context is OnChangePortfolioClickListener) {
            clickListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.portfolios.observe(viewLifecycleOwner, Observer(this::setPortfolios))
        viewModel.showDeletePortfolioDialogEvent.observe(viewLifecycleOwner, Observer(this::showDeletePortfolioDialog))
        viewModel.hideDeletePortfolioDialogEvent.observe(viewLifecycleOwner, Observer { hideDeletePortfolioDialog() })
        viewModel.errorMessageEvent.observe(viewLifecycleOwner, Observer(this::showError))
        viewModel.currentPortfolioEvent.observe(viewLifecycleOwner, Observer(this::setCurrentPortfolio))

        viewModel.loadAllPortfolios()
    }

    private fun setPortfolios(portfolios: List<PortfolioDbModel>) {
        portfoliosAdapter.setPortfolios(portfolios)
        portfoliosRecyclerView.scrollToPosition(portfoliosAdapter.itemCount - 1)
    }

    private fun setCurrentPortfolio(portfolioName: String) {
        viewModel.setCurrentPortfolio(portfolioName)
        clickListener?.onPortfolioChange()
        dismiss()
    }

    private fun showError(errorType: Int) {
        val message = when (errorType) {
            ChangePortfolioViewModel.ERROR_CODE_SMALL_COUNT_PORTFOLIO -> {
                resources.getString(R.string.small_count_portfolio)
            }
            else -> {
                return
            }
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showRenamePortfolioDialog(portfolio: PortfolioDbModel) {
        val dialog = RenamePortfolioDialog.newInstance(portfolio.name)
        dialog.show(childFragmentManager, RenamePortfolioDialog::class.toString())
    }

    private fun showDeletePortfolioDialog(portfolio: PortfolioDbModel) {
        deletePortfolioDialog = DeletePortfolioDialog.newInstance(portfolio.name)
        deletePortfolioDialog?.show(childFragmentManager, DeletePortfolioDialog::class.toString())
    }

    private fun hideDeletePortfolioDialog() {
        deletePortfolioDialog?.dismiss()
        deletePortfolioDialog = null
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

    override fun onPortfolioCreated(portfolioName: String) {
        setCurrentPortfolio(portfolioName)
    }

    override fun onDelete(name: String) {
        viewModel.deletePortfolio(name)
    }

    override fun renamePortfolio(from: String, to: String) {
        clickListener?.onPortfolioChange()
    }

    companion object {
        fun newInstance(): ChangePortfolioBottomDialog {
            return ChangePortfolioBottomDialog()
        }
    }

    interface OnChangePortfolioClickListener {
        fun onPortfolioChange()
    }
}
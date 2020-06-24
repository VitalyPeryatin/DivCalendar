package com.infinity_coder.divcalendar.presentation.tabs.portfolio.manageportfolio.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.tabs.portfolio.manageportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.tabs.portfolio.manageportfolio.ChangePortfolioViewModel
import kotlinx.android.synthetic.main.dialog_create_portfolio.*

class CreatePortfolioDialog : DialogFragment() {

    private var clickListener: OnCreatePortfolioClickListener? = null

    private val parentViewModel: ChangePortfolioViewModel by lazy {
        val parentFragment = parentFragment as ChangePortfolioBottomDialog
        parentFragment.viewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnCreatePortfolioClickListener) {
            clickListener = parentFragment
        } else if (context is OnCreatePortfolioClickListener) {
            clickListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_create_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.errorMessageEvent.observe(viewLifecycleOwner, Observer(this::showError))
        parentViewModel.createPortfolioEvent.observe(viewLifecycleOwner, Observer { dismiss() })

        createPortfolioButton.setOnClickListener {
            addPortfolio()
        }
    }

    private fun addPortfolio() {
        val name = portfolioNameEditText.text.toString()
        parentViewModel.requestCreatePortfolio(name)
    }

    private fun showError(errorCode: Int) {
        when (errorCode) {
            ChangePortfolioViewModel.ERROR_CODE_EMPTY_PORTFOLIO_NAME -> {
                portfolioNameInputLayout.error = resources.getString(R.string.empty_portfolio_name_error)
            }
            ChangePortfolioViewModel.ERROR_CODE_NOT_UNIQUE_NAME -> {
                portfolioNameInputLayout.error = resources.getString(R.string.not_new_portfolio_name_error)
            }
        }
    }

    companion object {

        const val TAG = "CreatePortfolioDialog"

        fun newInstance(): CreatePortfolioDialog {
            return CreatePortfolioDialog()
        }
    }

    interface OnCreatePortfolioClickListener {
        fun onPortfolioCreated(portfolioName: String)
    }
}
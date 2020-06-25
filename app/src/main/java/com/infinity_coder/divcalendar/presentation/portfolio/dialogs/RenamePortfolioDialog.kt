package com.infinity_coder.divcalendar.presentation.portfolio.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changeportfolio.ChangePortfolioBottomDialog
import com.infinity_coder.divcalendar.presentation.portfolio.dialogs.changeportfolio.ChangePortfolioViewModel
import kotlinx.android.synthetic.main.dialog_rename_portfolio.*

class RenamePortfolioDialog : DialogFragment() {

    private lateinit var oldPortfolioName: String

    private var clickListener: RenamePortfolioClickListener? = null

    private val parentViewModel: ChangePortfolioViewModel by lazy {
        val parentFragment = parentFragment as ChangePortfolioBottomDialog
        parentFragment.viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_rename_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldPortfolioName = requireArguments().getString(ARGUMENT_PORTFOLIO_NAME, "")

        initUI()

        parentViewModel.errorMessageEvent.observe(viewLifecycleOwner, Observer(this::showError))
        parentViewModel.renamePortfolioEvent.observe(viewLifecycleOwner, Observer(this::rename))
    }

    private fun initUI() {
        changeButton.setOnClickListener { requestRename() }
        titleTextView.text = resources.getString(
            R.string.edit_portfolio_with_name,
            oldPortfolioName
        )
    }

    private fun showError(errorCode: Int) {
        when (errorCode) {
            ChangePortfolioViewModel.ERROR_CODE_EMPTY_PORTFOLIO_NAME -> {
                nameInputLayout.error = resources.getString(R.string.empty_portfolio_name_error)
            }
            ChangePortfolioViewModel.ERROR_CODE_NOT_UNIQUE_NAME -> {
                nameInputLayout.error = resources.getString(R.string.not_new_portfolio_name_error)
            }
        }
    }

    private fun requestRename() {
        val newPortfolioName = nameEditText.text.toString()
        parentViewModel.requestRenamePortfolio(oldPortfolioName, newPortfolioName)
    }

    private fun rename(newName: String) {
        nameInputLayout.error = null
        clickListener?.renamePortfolio(oldPortfolioName, newName)
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is RenamePortfolioClickListener) {
            clickListener = parentFragment
        } else if (context is RenamePortfolioClickListener) {
            clickListener = context
        }
    }

    companion object {

        private const val ARGUMENT_PORTFOLIO_NAME = "portfolio_name"

        const val TAG = "RenamePortfolioDialog"

        fun newInstance(name: String): RenamePortfolioDialog {
            val dialog = RenamePortfolioDialog()
            dialog.arguments = bundleOf(ARGUMENT_PORTFOLIO_NAME to name)
            return dialog
        }
    }

    interface RenamePortfolioClickListener {
        fun renamePortfolio(from: String, to: String)
    }
}
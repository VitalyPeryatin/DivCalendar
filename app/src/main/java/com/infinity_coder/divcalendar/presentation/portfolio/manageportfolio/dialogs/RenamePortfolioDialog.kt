package com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.dialog_rename_portfolio.*

class RenamePortfolioDialog : DialogFragment() {

    private lateinit var oldPortfolioName: String

    private var clickListener: RenamePortfolioClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_rename_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldPortfolioName = requireArguments().getString(ARGUMENT_PORTFOLIO_NAME, "")

        editButton.setOnClickListener { rename() }
        titleTextView.text = resources.getString(R.string.edit_portfolio_with_name, oldPortfolioName)
    }

    private fun rename() {
        val newPortfolioName = nameEditText.text.toString()
        when {
            newPortfolioName.isBlank() -> {
                nameInputLayout.error = resources.getString(R.string.empty_portfolio_name_error)
            }
            newPortfolioName == oldPortfolioName -> {
                nameInputLayout.error = resources.getString(R.string.not_new_portfolio_name_error)
            }
            else -> {
                nameInputLayout.error = null
                clickListener?.renamePortfolio(oldPortfolioName, newPortfolioName)
            }
        }
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
package com.infinity_coder.divcalendar.presentation.portfolio.manageportfolio.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import kotlinx.android.synthetic.main.dialog_delete_porfolio.*

class DeletePortfolioDialog : DialogFragment() {

    private lateinit var portfolioName: String

    private var clickListener: DeletePortfolioClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_delete_porfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        portfolioName = requireArguments().getString(ARGUMENT_PORTFOLIO_NAME, "")

        initUI()
    }

    private fun initUI() {
        deleteButton.setOnClickListener {
            clickListener?.onDelete(portfolioName)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        messageTextView.text = resources.getString(R.string.sure_remove_portfolio, portfolioName)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is DeletePortfolioClickListener) {
            clickListener = parentFragment
        } else if (context is DeletePortfolioClickListener) {
            clickListener = context
        }
    }

    companion object {

        private const val ARGUMENT_PORTFOLIO_NAME = "portfolio_name"

        const val TAG = "DeletePortfolioDialog"

        fun newInstance(name: String): DeletePortfolioDialog {
            val dialog = DeletePortfolioDialog()
            dialog.arguments = bundleOf(ARGUMENT_PORTFOLIO_NAME to name)
            return dialog
        }
    }

    interface DeletePortfolioClickListener {
        fun onDelete(name: String)
    }
}
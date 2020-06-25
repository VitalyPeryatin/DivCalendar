package com.infinity_coder.divcalendar.presentation.portfolio.dialogs

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is DeletePortfolioClickListener) {
            clickListener = parentFragment
        } else if (context is DeletePortfolioClickListener) {
            clickListener = context
        }
    }

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

    companion object {

        const val TAG = "DeletePortfolioDialog"

        private const val ARGUMENT_PORTFOLIO_NAME = "portfolio_name"

        fun newInstance(name: String): DeletePortfolioDialog {
            return DeletePortfolioDialog()
                .apply {
                arguments = bundleOf(ARGUMENT_PORTFOLIO_NAME to name)
            }
        }
    }

    interface DeletePortfolioClickListener {
        fun onDelete(name: String)
    }
}
package com.infinity_coder.divcalendar.presentation.portfolio.createportfolio

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.viewModel
import kotlinx.android.synthetic.main.dialog_create_portfolio.*

class CreatePortfolioDialog : DialogFragment() {

    private var clickListener: OnCreatePortfolioClickListener? = null

    private val viewModel: CreatePortfolioViewModel by lazy {
        viewModel { CreatePortfolioViewModel() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_create_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPortfolioButton.setOnClickListener {
            addPortfolio()
        }
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

    private fun addPortfolio() {
        val name = portfolioNameEditText.text.toString()
        viewModel.addPortfolio(name)
        clickListener?.onPortfolioCreated()
        dismiss()
    }

    companion object {
        fun newInstance(): CreatePortfolioDialog {
            return CreatePortfolioDialog()
        }
    }

    interface OnCreatePortfolioClickListener {
        fun onPortfolioCreated()
    }
}
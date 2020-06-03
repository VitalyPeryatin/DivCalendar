package com.infinity_coder.divcalendar.presentation.portfolio.sorting

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.domain.models.SortType
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import kotlinx.android.synthetic.main.bottom_dialog_sorting_portfolio.*
import kotlinx.android.synthetic.main.fragment_portfolio.*

class SortingPortfolioBottomDialog : BottomDialog() {

    private var callback: SortingPortfolioCallback? = null

    private val viewModel: SortingPortfolioViewModel by lazy {
        ViewModelProvider(this).get(SortingPortfolioViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is SortingPortfolioCallback) {
            callback = parentFragment
        } else if (context is SortingPortfolioCallback) {
            callback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_sorting_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.nextPayoutDateRadioButton -> viewModel.setCurrentSortType(SortType.PAYMENT_DATE)
                R.id.profitabilityRadioButton -> viewModel.setCurrentSortType(SortType.PROFITABILITY)
                R.id.alphabeticallyRadioButton -> viewModel.setCurrentSortType(SortType.ALPHABETICALLY)
            }
        }

        sortTypeRadioGroup.clearCheck()
        val radioButton = when (viewModel.getCurrentSortType()) {
            SortType.PAYMENT_DATE -> nextPayoutDateRadioButton
            SortType.PROFITABILITY -> profitabilityRadioButton
            SortType.ALPHABETICALLY -> alphabeticallyRadioButton
        }
        radioButton.isChecked = true
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (viewModel.isUpdatePortfolio()) {
            callback?.onUpdatePortfolio()
        }
    }

    companion object {

        const val TAG = "SortingPortfolioBottomDialog"

        fun newInstance(): SortingPortfolioBottomDialog {
            return SortingPortfolioBottomDialog()
        }
    }

    interface SortingPortfolioCallback {
        fun onUpdatePortfolio()
    }
}
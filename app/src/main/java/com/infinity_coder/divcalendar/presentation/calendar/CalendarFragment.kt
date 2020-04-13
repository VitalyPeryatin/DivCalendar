package com.infinity_coder.divcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.repositories.RateRepository
import com.infinity_coder.divcalendar.presentation._common.getColorExt
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.calendar.adapters.*
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by lazy {
        viewModel { CalendarViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.payments.observe(viewLifecycleOwner, Observer(this::updatePayments))
        viewModel.isIncludeTaxes.observe(viewLifecycleOwner, Observer(this::setIsIncludedTexes))
    }

    override fun onStart() {
        super.onStart()

        viewModel.updateData()
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar)
            (activity as AppCompatActivity).setActionBar(this)
        }

        val checkedCurrencyRadioButton =
            when (viewModel.getDisplayCurrency()) {
                RateRepository.RUB_RATE -> rubRadioButton
                RateRepository.USD_RATE -> usdRadioButton
                else -> null
            }

        checkedCurrencyRadioButton?.isChecked = true

        rubRadioButton.setOnCheckedChangeListener(this::checkCurrency)
        usdRadioButton.setOnCheckedChangeListener(this::checkCurrency)

        calendarPaymentsRecyclerView.layoutManager = LinearLayoutManager(context)
        (calendarPaymentsRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        calendarPaymentsRecyclerView.adapter = DiffUtilCompositeAdapter.Builder()
            .add(getChartAdapter()).add(DividerDelegateAdapter())
            .add(HeaderPaymentRecyclerDelegateAdapter())
            .add(PaymentRecyclerDelegateAdapter())
            .add(FooterPaymentRecyclerDelegateAdapter()).build()
    }

    private fun checkCurrency(radioButton: CompoundButton, isChecked: Boolean) {
        if (!isChecked) return

        when (radioButton) {
            rubRadioButton -> viewModel.setDisplayCurrency(RateRepository.RUB_RATE)
            usdRadioButton -> viewModel.setDisplayCurrency(RateRepository.USD_RATE)
        }
    }

    private fun getChartAdapter(): ChartPaymentRecyclerDelegateAdapter {
        val adapter = ChartPaymentRecyclerDelegateAdapter()
        adapter.onItemClickListener = object : ChartPaymentRecyclerDelegateAdapter.ChartItemClickListener {
            override fun onClick(numberMonth: Int) {
                calendarPaymentsRecyclerView.smoothScrollToPosition(
                    viewModel.getFooterPositionByMonthNumber(numberMonth)
                )
            }
        }
        return adapter
    }

    private fun setIsIncludedTexes(isIncludedTaxes: Boolean) {
        val colorId: Int
        if (isIncludedTaxes) {
            taxesTextView.text = resources.getString(R.string.taxes_included)
            colorId = R.color.colorAccent
        } else {
            taxesTextView.text = resources.getString(R.string.taxes_excluding)
            colorId = R.color.colorSecondary
        }
        taxesTextView.setTextColor(resources.getColorExt(colorId, requireContext().theme))
    }

    private fun updatePayments(payments: List<IComparableItem>) {
        (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(payments)
    }

    private fun updateState(state: Int) {
        calendarContent.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE

        when (state) {
            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> calendarContent.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> loadingLayout.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> emptyLayout.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
        }
    }
}
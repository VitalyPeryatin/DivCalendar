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
import com.infinity_coder.divcalendar.presentation._common.SpinnerInteractionListener
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.calendar.adapters.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.layout_stub_empty.view.*
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by lazy {
        viewModel { CalendarViewModel() }
    }

    private var items:Array<String> = arrayOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.payments.observe(viewLifecycleOwner, Observer(this::updatePayments))
        viewModel.currentYear.observe(viewLifecycleOwner, Observer(this::updateCurrentYear))
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar)
            (activity as AppCompatActivity).setActionBar(this)
        }

        initSpinnerYear()

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
            .add(getChartAdapter())
            .add(DividerDelegateAdapter())
            .add(HeaderPaymentRecyclerDelegateAdapter())
            .add(PaymentRecyclerDelegateAdapter())
            .add(FooterPaymentRecyclerDelegateAdapter())
            .build()

        emptySecuritiesLayout.emptyTextView.text = resources.getString(R.string.empty_securities)
    }

    private fun initSpinnerYear(){
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        items = Array(MAX_YEARS_CHOICE) { index -> (currentYear + index).toString() }
        yearSpinner.adapter = SpinnerAdapter(requireContext(), items).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }

        val spinnerInteractionListener = SpinnerInteractionListener{
            viewModel.selectYear(it as String)
        }
        yearSpinner.onItemSelectedListener = spinnerInteractionListener
        yearSpinner.setOnTouchListener(spinnerInteractionListener)
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

    private fun updatePayments(payments: List<IComparableItem>) {
        (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(payments)
    }

    private fun updateCurrentYear(year:String) {
        emptyLayout.emptyTextView.text = resources.getString(R.string.empty_payments, year)
        yearSpinner.setSelection(items.indexOf(year))
    }

    private fun updateState(state: Int) {
        when (state) {
            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> showContentState()
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> showLoadingState()
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> showEmptyState()
            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> showNoNetworkState()
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY_SECURITIES -> showEmptySecuritiesState()
        }
    }

    private fun showContentState(){
        calendarContent.visibility = View.VISIBLE
        yearSpinner.visibility = View.VISIBLE
        currencyRadioGroup.visibility = View.VISIBLE
        calendarPaymentsRecyclerView.visibility = View.VISIBLE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.GONE
    }

    private fun showLoadingState(){
        calendarContent.visibility = View.GONE
        yearSpinner.visibility = View.GONE
        currencyRadioGroup.visibility = View.GONE
        calendarPaymentsRecyclerView.visibility = View.GONE
        loadingLayout.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.GONE
    }

    private fun showEmptyState(){
        calendarContent.visibility = View.VISIBLE
        yearSpinner.visibility = View.VISIBLE
        currencyRadioGroup.visibility = View.INVISIBLE
        calendarPaymentsRecyclerView.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.VISIBLE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.GONE
    }

    private fun showNoNetworkState(){
        calendarContent.visibility = View.GONE
        yearSpinner.visibility = View.GONE
        currencyRadioGroup.visibility = View.GONE
        calendarPaymentsRecyclerView.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.VISIBLE
        emptySecuritiesLayout.visibility = View.GONE
    }

    private fun showEmptySecuritiesState(){
        calendarContent.visibility = View.GONE
        yearSpinner.visibility = View.GONE
        currencyRadioGroup.visibility = View.GONE
        calendarPaymentsRecyclerView.visibility = View.GONE
        loadingLayout.visibility = View.GONE
        emptyLayout.visibility = View.GONE
        noNetworkLayout.visibility = View.GONE
        emptySecuritiesLayout.visibility = View.VISIBLE
    }

    companion object{
        private const val MAX_YEARS_CHOICE = 2
    }
}
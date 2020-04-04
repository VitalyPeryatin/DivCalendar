package com.infinity_coder.divcalendar.presentation.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.calendar.adapters.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlin.properties.Delegates

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var currentState by Delegates.observable(
        CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT, { _, old, new -> changeViewState(new, old) }
    )

    private val viewModel: CalendarViewModel by lazy {
        viewModel { CalendarViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.payments.observe(viewLifecycleOwner, Observer(this::updatePayments))
    }

    private fun initUI() {
        calendarToolbar.run {
            setTitle(R.string.calendar)
            (activity as AppCompatActivity).setActionBar(this)
        }

        calendarPaymentsRecyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(getChartAdapter())
                .add(DividerDelegateAdapter())
                .add(HeaderPaymentRecyclerDelegateAdapter())
                .add(PaymentRecyclerDelegateAdapter())
                .add(FooterPaymentRecyclerDelegateAdapter())
                .build()
        }
    }

    private fun getChartAdapter(): ChartPaymentRecyclerDelegateAdapter {
        val adapter =
            ChartPaymentRecyclerDelegateAdapter()
        adapter.onItemClickListener =
            object : ChartPaymentRecyclerDelegateAdapter.ChartItemClickListener {
                override fun onClick(numberMonth: Int) {
                    calendarPaymentsRecyclerView.smoothScrollToPosition(
                        viewModel.getPositionMonth(numberMonth)
                    )
                }
            }
        return adapter
    }

    private fun updatePayments(payments: List<IComparableItem>) {
        (calendarPaymentsRecyclerView.adapter as DiffUtilCompositeAdapter).swapData(payments)
    }

    private fun updateState(state: Int) {
        currentState = state
    }

    private fun changeViewState(newState: Int, oldState: Int) {
        when (oldState) {
            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> calendarContent.visibility = View.GONE
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> loadingLayout.visibility = View.GONE
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> emptyLayout.visibility = View.GONE
            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> noNetworkLayout.visibility = View.GONE
        }

        when (newState) {
            CalendarViewModel.VIEW_STATE_CALENDAR_CONTENT -> calendarContent.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_LOADING -> loadingLayout.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_EMPTY -> emptyLayout.visibility = View.VISIBLE
            CalendarViewModel.VIEW_STATE_CALENDAR_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
        }
    }
}
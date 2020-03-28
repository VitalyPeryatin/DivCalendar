package com.infinity_coder.divcalendar.presentation.calendar

import android.os.Bundle
import android.util.Log
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
import com.infinity_coder.divcalendar.presentation._common.visibilityGone
import com.infinity_coder.divcalendar.presentation.global.VerticalItemDecoration
import com.infinity_coder.divcalendar.presentation.mappers.PaymentMapperViewModel
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by lazy {
        viewModel {
            CalendarViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel.paymentMapperViewModel = PaymentMapperViewModel(requireContext())
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
    }

    private fun initUI(){
        initCalendarToolbar()
        initPaymentsRecyclerView()
    }

    private fun initCalendarToolbar(){
        calendar_toolbar.setTitle(R.string.calendar_title)
        (activity as AppCompatActivity).setActionBar(calendar_toolbar)
    }

    private fun initPaymentsRecyclerView(){
        calendar_payments_recycler_view.run {
            layoutManager = LinearLayoutManager(context)
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(DividerAdapter)
                .add(HeaderPaymentRecyclerDelegateAdapter())
                .add(PaymentRecyclerDelegateAdapter())
                .add(FooterPaymentRecyclerDelegateAdapter())
                .build()
        }
    }

    private fun updateState(state:State){
        when(state){
            is State.Progress -> {
                calendar_progress_bar.visibilityGone(true)
                calendar_payments_recycler_view.visibilityGone(false)
            }
            is State.Data<*> -> {
                calendar_progress_bar.visibilityGone(false)
                calendar_payments_recycler_view.visibilityGone(true)
                (calendar_payments_recycler_view.adapter as DiffUtilCompositeAdapter).swapData(state.data as List<IComparableItem>)
            }
            is State.Error -> {

            }
        }
    }
}
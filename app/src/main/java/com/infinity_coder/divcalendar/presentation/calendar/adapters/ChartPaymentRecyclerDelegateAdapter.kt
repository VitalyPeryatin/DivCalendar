package com.infinity_coder.divcalendar.presentation.calendar.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.presentation.calendar.models.ChartPresentationModel
import kotlinx.android.synthetic.main.item_chart_calendar.*

class ChartPaymentRecyclerDelegateAdapter : KDelegateAdapter<ChartPresentationModel>(),
    OnChartValueSelectedListener {

    var onItemClickListener: ChartItemClickListener? = null

    private var monthlyPayments: List<Pair<Int, List<PaymentNetworkModel>>>? = null
    private var chart: BarChart? = null

    override fun getLayoutId() = R.layout.item_chart_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is ChartPresentationModel
    }

    override fun onBind(item: ChartPresentationModel, viewHolder: KViewHolder) {
        monthlyPayments = item.monthlyPayments
        chart = viewHolder.chart

        viewHolder.run {
            chart.onBindChart(item)
            annualIncome.text =
                annualIncome.context.getString(R.string.annual_income, item.annualIncome.toFloat())
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        if (entry == null || monthlyPayments == null) return

        val firstPayment = monthlyPayments
            ?.find { it.first == (entry.x.toInt() + 1) }
            ?: return

        if (firstPayment.second.isNotEmpty())
            onItemClickListener?.onClick(firstPayment.first)
        chart?.highlightValues(null)
    }

    private fun BarChart.onBindChart(item: ChartPresentationModel) {
        axisRight.isEnabled = false
        legend.isEnabled = false
        description.isEnabled = false
        axisLeft.setDrawGridLines(false)
        axisLeft.axisMinimum = 0f

        val months = context.resources.getStringArray(R.array.months_nominative_case)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return months[value.toInt()]
            }
        }

        val set = BarDataSet(createBarEntries(item.monthlyPayments), "").apply {
            colors = item.colors
        }
        val data = BarData(set).apply {
            setDrawValues(false)
        }

        setData(data)
        setOnChartValueSelectedListener(this@ChartPaymentRecyclerDelegateAdapter)
        invalidate()
    }

    private fun createBarEntries(monthlyPayments: List<Pair<Int, List<PaymentNetworkModel>>>): List<BarEntry> {
        return monthlyPayments.map {
            val month = (it.first - 1).toFloat()
            val dividends = if (it.second.isEmpty()) {
                floatArrayOf(0f)
            } else {
                it.second.map { payment -> payment.dividends.toFloat() }.toFloatArray()
            }
            return@map BarEntry(month, dividends)
        }
    }

    interface ChartItemClickListener {
        fun onClick(numberMonth: Int)
    }
}
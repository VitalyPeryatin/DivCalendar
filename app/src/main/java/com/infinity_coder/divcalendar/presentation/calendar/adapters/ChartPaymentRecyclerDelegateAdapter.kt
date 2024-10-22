package com.infinity_coder.divcalendar.presentation.calendar.adapters

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
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
import com.infinity_coder.divcalendar.presentation._common.delegate.SecurityCurrencyDelegate
import com.infinity_coder.divcalendar.presentation.calendar.models.ChartPresentationModel
import com.infinity_coder.divcalendar.presentation.calendar.models.MonthlyPayment
import kotlinx.android.synthetic.main.item_chart_calendar.*

class ChartPaymentRecyclerDelegateAdapter : KDelegateAdapter<ChartPresentationModel>(),
    OnChartValueSelectedListener {

    var onItemClickListener: ChartItemClickListener? = null

    private var monthlyPayments: List<MonthlyPayment>? = null
    private var chart: BarChart? = null

    override fun getLayoutId() = R.layout.item_chart_calendar

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is ChartPresentationModel
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(item: ChartPresentationModel, viewHolder: KViewHolder) {
        monthlyPayments = item.monthlyPayments
        chart = viewHolder.chart
        val context = viewHolder.containerView.context

        viewHolder.run {
            chart.onBindChart(item)

            annualIncomeTextView.text = "${item.annualIncome} ${SecurityCurrencyDelegate.getCurrencyBadge(context, item.currentCurrency)}"
            annualYieldTextView.text = context.getString(R.string.value_percent, item.annualYield)
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        if (entry == null || monthlyPayments == null) return

        val paymentsForMonth = monthlyPayments
            ?.find { it.month == entry.x.toInt() }
            ?: return

        if (paymentsForMonth.payments.isNotEmpty())
            onItemClickListener?.onClick(paymentsForMonth.month)
        chart?.highlightValues(null)
    }

    private fun BarChart.onBindChart(item: ChartPresentationModel) {
        axisRight.isEnabled = false
        legend.isEnabled = false
        description.isEnabled = false
        axisLeft.setDrawGridLines(false)
        axisLeft.axisMinimum = 0f
        axisLeft.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)

        val months = context.resources.getStringArray(R.array.months_first_letter)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelCount = 12
        xAxis.textColor = ContextCompat.getColor(context, R.color.colorTextPrimary)
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

    private fun createBarEntries(monthlyPayments: List<MonthlyPayment>): List<BarEntry> {
        return monthlyPayments.map {
            val month = it.month.toFloat()
            val payments = if (it.payments.isEmpty())
                floatArrayOf(0f)
            else
                it.payments.map { payment -> payment.dividends.toFloat() }.toFloatArray()
            return@map BarEntry(month, payments)
        }
    }

    interface ChartItemClickListener {
        fun onClick(numberMonth: Int)
    }
}
package com.infinity_coder.divcalendar.presentation.views

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.infinity_coder.divcalendar.R
import java.text.DecimalFormat

class XYMarkerView(
    context: Context?,
    private val xAxisValueFormatter: ValueFormatter
) :
    MarkerView(context, R.layout.custom_marker_view) {
    private val tvContent: TextView
    private val format: DecimalFormat
    // runs every time the MarkerView is redrawn, can be used to update the
// content (user-interface)
    override fun refreshContent(
        e: Entry,
        highlight: Highlight
    ) {
        tvContent.text = String.format(
            "x: %s, y: %s",
            xAxisValueFormatter.getFormattedValue(e.x),
            format.format(e.y.toDouble())
        )
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        tvContent = findViewById(R.id.tvContent)
        format = DecimalFormat("###.0")
    }
}
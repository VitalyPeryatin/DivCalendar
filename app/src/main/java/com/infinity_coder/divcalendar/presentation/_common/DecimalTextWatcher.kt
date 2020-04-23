package com.infinity_coder.divcalendar.presentation._common

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.text.DecimalFormat

class DecimalTextWatcher(
    private val editText:EditText,
    private val formatter:DecimalFormat
):TextWatcher {

    var hasFractionalPart = false

    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(s: Editable?) {
        val value = s.toString().replace(formatter.decimalFormatSymbols.groupingSeparator.toString(),"")

        Log.d("DecimalTextLog","$hasFractionalPart  ${value.split(".")}")
        if(hasFractionalPart && value.split(".").last().isEmpty()){
            Log.d("DecimalTextLog","fuck")
        }else{
            val answer = formatter.format(value.toFloat()).replace(formatter.decimalFormatSymbols.decimalSeparator.toString(),".")
            editText.removeTextChangedListener(this)
            editText.setText(answer)
            editText.setSelection(editText.length())
            editText.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        hasFractionalPart = s.toString().contains(".")
    }

}
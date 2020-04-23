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
        val str = s.toString()
        val splitPart = str.split(".")

        if(str.isEmpty() || (hasFractionalPart && splitPart.last().isEmpty()))
           return

        if(splitPart.size == 2 && splitPart.last().length > formatter.maximumFractionDigits){
            changeEditText { editText.setText(str.substring(0,str.length - 1)) }
            return
        }

        val value = str.replace(formatter.decimalFormatSymbols.groupingSeparator.toString(),"").toDouble()
        val valueFormatted = formatter.format(value).replace(formatter.decimalFormatSymbols.decimalSeparator.toString(),".")
        changeEditText { editText.setText(valueFormatted) }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        hasFractionalPart = s.toString().contains(".")
    }

    private fun changeEditText(func:()->Unit){
        editText.removeTextChangedListener(this)
        func.invoke()
        editText.setSelection(editText.length())
        editText.addTextChangedListener(this)
    }
}
package com.infinity_coder.divcalendar.presentation._common.text_watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

class DecimalCountTextWatcher(
    private val editText: EditText,
    private val decimalFormat:DecimalFormat
):TextWatcher{

    private var enteredNumericBeforeChange:String = ""
    private var selectionStartBeforeUserChange:Int = 0

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        enteredNumericBeforeChange = s.toString()
        selectionStartBeforeUserChange = editText.selectionStart
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val enteredNumeric = s.toString().replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(),"")

        if(checkIfZeroFirstNumber(enteredNumeric) || checkIfNumberMoreThanIntegerMaxValue(enteredNumeric)){
            returnThePastStateEditText()
            return
        }

        if(enteredNumeric.isNotEmpty()) {
            formatNumber(enteredNumeric)
        }
    }

    private fun checkIfZeroFirstNumber(enteredNumeric:String):Boolean {
        return enteredNumeric.isNotEmpty() && enteredNumeric.first() == '0'
    }

    private fun checkIfNumberMoreThanIntegerMaxValue(enteredNumeric:String):Boolean{
        if(enteredNumeric.isEmpty()) return false

        return try {
            enteredNumeric.toInt()
            false
        }catch (e:NumberFormatException){
            true
        }
    }

    private fun returnThePastStateEditText() {
        changeEditText {
            editText.setText(enteredNumericBeforeChange)
            editText.setSelection(selectionStartBeforeUserChange)
        }
    }

    private fun formatNumber(enteredNumeric: String){
        val formattedEnteredNumeric = decimalFormat.format(enteredNumeric.toInt())

        changeEditText {
            val selectionStartBeforeChange = editText.selectionStart

            editText.setText(formattedEnteredNumeric)

            when {
                checkIfSpaceIsAdded(formattedEnteredNumeric) -> {
                    editText.setSelection(selectionStartBeforeChange + 1)
                }
                checkIfSpaceIsRemoved(selectionStartBeforeChange, formattedEnteredNumeric) -> {
                    editText.setSelection(selectionStartBeforeChange - 1)
                }
                else -> {
                    editText.setSelection(selectionStartBeforeChange)
                }
            }
        }
    }

    private fun checkIfSpaceIsAdded(formattedEnteredNumeric:String):Boolean{
        return formattedEnteredNumeric.length > enteredNumericBeforeChange.length + 1
    }

    private fun checkIfSpaceIsRemoved(selectionStartBeforeChange:Int,formattedEnteredNumeric:String):Boolean{
        return selectionStartBeforeChange!= 0 && formattedEnteredNumeric.length + 1 < enteredNumericBeforeChange.length
    }

    private fun changeEditText(func:()->Unit){
        editText.removeTextChangedListener(this)
        func.invoke()
        editText.addTextChangedListener(this)
    }
}
package com.infinity_coder.divcalendar.presentation._common.text_watchers

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

class DecimalPriceTextWatcher(
    private val editText: EditText,
    private val decimalFormat: DecimalFormat,
    private val maxNumberInIntegerPart: Int = 12,
    private val maxNumberInFractionalPart: Int = 6
) : TextWatcher {

    private var enteredNumericBeforeChange:String = ""
    private var selectionStartBeforeUserChange:Int = 0

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        enteredNumericBeforeChange = s.toString()
        selectionStartBeforeUserChange = editText.selectionStart
    }

    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val enteredNumeric = s.toString().replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(),"")

        if(checkIfFirstCharacterSeparator(enteredNumeric)){
            changeEditText {
                editText.setText("0$EDIT_TEXT_SEPARATOR")
                editText.setSelection(editText.length())
            }
            return
        }

        if(checkIfCountOfNumberInIntegerPartGreaterPossible(enteredNumeric)
            || checkIfCountOfNumberInFractionalPartGreaterPossible(enteredNumeric)
            || checkIfFirstCharacterSeparatorButNotFirstOnePrinted(enteredNumeric)
        ){
            returnThePastStateEditText()
            return
        }

        if(enteredNumeric.isNotEmpty()) {
            formatNumber(enteredNumeric)
        }
    }

    private fun checkIfFirstCharacterSeparator(enteredNumeric: String):Boolean {
        return enteredNumeric.isNotEmpty() && enteredNumeric.length == 1 && enteredNumeric.first() == EDIT_TEXT_SEPARATOR
    }

    private fun checkIfFirstCharacterSeparatorButNotFirstOnePrinted(enteredNumeric: String):Boolean{
        return enteredNumeric.isNotEmpty() && enteredNumeric.length != 1 && enteredNumeric.first() == EDIT_TEXT_SEPARATOR
    }

    private fun checkIfCountOfNumberInIntegerPartGreaterPossible(enteredNumeric: String):Boolean{
        val numericParts = getNumericParts(enteredNumeric)
        return numericParts.integerPart.length > maxNumberInIntegerPart
    }

    private fun checkIfCountOfNumberInFractionalPartGreaterPossible(enteredNumeric: String):Boolean{
        val numericParts = getNumericParts(enteredNumeric)
        return if(numericParts.fractionalPart == null || numericParts.fractionalPart.isEmpty())
            false
        else
            numericParts.fractionalPart.length > maxNumberInFractionalPart
    }

    private fun returnThePastStateEditText() {
        changeEditText {
            editText.setText(enteredNumericBeforeChange)
            editText.setSelection(selectionStartBeforeUserChange)
        }
    }

    private fun formatNumber(enteredNumeric: String){
        val numericParts = getNumericParts(enteredNumeric)

        val formattedEnteredNumeric = decimalFormat.format(numericParts.integerPart.toDouble()).run {
            if(numericParts.fractionalPart!=null){
                return@run "$this.${numericParts.fractionalPart}"
            }
            return@run this
        }

        changeEditText {
            val selectionStartBeforeChange = editText.selectionStart
            val lengthBeforeChange = editText.length()
            editText.setText(formattedEnteredNumeric)
            editText.setSelection(selectionStartBeforeChange + (formattedEnteredNumeric.length - lengthBeforeChange))
            /*when {
                checkIfSpaceIsAdded(formattedEnteredNumeric) -> {
                    editText.setSelection(selectionStartBeforeChange + 1)
                }
                checkIfSpaceIsRemoved(selectionStartBeforeChange, formattedEnteredNumeric) -> {
                    editText.setSelection(selectionStartBeforeChange - 1)
                }
                else -> {
                    editText.setSelection(selectionStartBeforeChange)
                }
            }*/
        }
    }

    private fun checkIfSpaceIsAdded(formattedEnteredNumeric:String):Boolean{
        return formattedEnteredNumeric.length > enteredNumericBeforeChange.length + 1
    }

    private fun checkIfSpaceIsRemoved(selectionStartBeforeChange:Int,formattedEnteredNumeric:String):Boolean{
        return selectionStartBeforeChange!= 0 && formattedEnteredNumeric.length + 1 < enteredNumericBeforeChange.length
    }

    private fun getNumericParts(enteredNumeric: String):NumericParts{
        val integerPart:String
        var fractionalPart:String? = null

        enteredNumeric.split(EDIT_TEXT_SEPARATOR).run {
            integerPart = this[0]
            if(this.size > 1)
                fractionalPart = this[1]
        }

        return NumericParts(integerPart, fractionalPart)
    }

    private fun changeEditText(func:()->Unit){
        editText.removeTextChangedListener(this)
        func.invoke()
        editText.addTextChangedListener(this)
    }

    companion object{
        private const val EDIT_TEXT_SEPARATOR = '.'
    }

    data class NumericParts(
        val integerPart:String = "",
        val fractionalPart:String? = null
    )
}
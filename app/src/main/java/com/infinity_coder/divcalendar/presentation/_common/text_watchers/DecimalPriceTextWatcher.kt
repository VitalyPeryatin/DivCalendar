package com.infinity_coder.divcalendar.presentation._common.text_watchers

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.text.DecimalFormat

class DecimalPriceTextWatcher(
    private val editText: EditText,
    private val decimalFormat: DecimalFormat,
    private val maxNumberInIntegerPart: Int = 12,
    private val maxNumberInFractionalPart: Int = 100
) : TextWatcher {

    private var enteredNumericBeforeChange: String = ""
    private var selectionStartBeforeUserChange: Int = 0
    private var isAddition = false
    private var currentSeparator = LOCAL_SEPARATOR

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isAddition = after == 1
        enteredNumericBeforeChange = s.toString()
        selectionStartBeforeUserChange = editText.selectionStart
    }

    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        defineCurrentSeparator(s.toString())
        val enteredNumeric = s.toString()
            .replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "")
            .replace(currentSeparator.toString(), LOCAL_SEPARATOR.toString())

        if (checkIfFirstCharacterSeparatorWhenAdding(enteredNumeric) ||
            checkIfCountOfNumberInIntegerPartGreaterPossible(enteredNumeric) ||
            checkIfCountOfNumberInFractionalPartGreaterPossible(enteredNumeric)
        ) {
            returnThePastStateEditText()
            return
        }

        if (checkIfFirstCharacterSeparatorWhenRemoved(enteredNumeric)) return

        if (enteredNumeric.isNotEmpty()) {
            formatNumber(enteredNumeric)
        }
    }

    private fun checkIfFirstCharacterSeparatorWhenAdding(enteredNumeric: String): Boolean {
        return enteredNumeric.isNotEmpty() && enteredNumeric.first() == LOCAL_SEPARATOR && isAddition
    }

    private fun checkIfFirstCharacterSeparatorWhenRemoved(enteredNumeric: String): Boolean {
        return enteredNumeric.isNotEmpty() && enteredNumeric.first() == LOCAL_SEPARATOR && !isAddition
    }

    private fun checkIfCountOfNumberInIntegerPartGreaterPossible(enteredNumeric: String): Boolean {
        val numericParts = getNumericParts(enteredNumeric)
        return numericParts.integerPart.length > maxNumberInIntegerPart
    }

    private fun checkIfCountOfNumberInFractionalPartGreaterPossible(enteredNumeric: String): Boolean {
        val numericParts = getNumericParts(enteredNumeric)
        return if (numericParts.fractionalPart == null || numericParts.fractionalPart.isEmpty())
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

    private fun formatNumber(enteredNumeric: String) {
        val numericParts = getNumericParts(enteredNumeric)

        val formattedEnteredNumeric = decimalFormat.format(numericParts.integerPart.toDouble()).run {
            if (numericParts.fractionalPart != null) {
                return@run "$this$currentSeparator${numericParts.fractionalPart}"
            }
            return@run this
        }

        changeEditText {
            val selectionStartBeforeChange = editText.selectionStart
            editText.setText(formattedEnteredNumeric)
            when {
                checkIfSpaceIsAdded(formattedEnteredNumeric) -> {
                    editText.setSelection(selectionStartBeforeChange + 1)
                }
                checkIfSpaceIsRemoved(formattedEnteredNumeric) -> {
                    if (selectionStartBeforeChange != 0)
                        editText.setSelection(selectionStartBeforeChange - 1)
                    else
                        editText.setSelection(selectionStartBeforeChange)
                }
                else -> {
                    if (enteredNumericBeforeChange.length == formattedEnteredNumeric.length){
                        editText.setSelection(selectionStartBeforeUserChange)
                        Log.d("LogEdit","fuck")
                    }
                    else
                        editText.setSelection(selectionStartBeforeChange)
                }
            }
        }
    }

    private fun checkIfSpaceIsAdded(formattedEnteredNumeric: String): Boolean {
        val beforeSpace = enteredNumericBeforeChange.count { it == decimalFormat.decimalFormatSymbols.groupingSeparator }
        val afterSpace = formattedEnteredNumeric.count { it == decimalFormat.decimalFormatSymbols.groupingSeparator }
        return afterSpace > beforeSpace
    }

    private fun checkIfSpaceIsRemoved(formattedEnteredNumeric: String): Boolean {
        val beforeSpace = enteredNumericBeforeChange.count { it == decimalFormat.decimalFormatSymbols.groupingSeparator }
        val afterSpace = formattedEnteredNumeric.count { it == decimalFormat.decimalFormatSymbols.groupingSeparator }
        return afterSpace < beforeSpace
    }

    private fun getNumericParts(enteredNumeric: String): NumericParts {
        val integerPart: String
        var fractionalPart: String? = null

        enteredNumeric.split(LOCAL_SEPARATOR).let {
            integerPart = it[0]
            if (it.size > 1)
                fractionalPart = it[1]
        }

        return NumericParts(integerPart, fractionalPart)
    }

    private fun changeEditText(func: () -> Unit) {
        editText.removeTextChangedListener(this)
        func.invoke()
        editText.addTextChangedListener(this)
    }

    private fun defineCurrentSeparator(currentEnteredNumeric:String){
        for(separator in POSSIBLE_SEPARATORS){
            if(currentEnteredNumeric.contains(separator)){
                currentSeparator = separator
            }
        }
    }

    companion object {
        private const val LOCAL_SEPARATOR = '.'
        private val POSSIBLE_SEPARATORS = listOf('.',',')
    }

    data class NumericParts(
        val integerPart: String = "",
        val fractionalPart: String? = null
    )
}
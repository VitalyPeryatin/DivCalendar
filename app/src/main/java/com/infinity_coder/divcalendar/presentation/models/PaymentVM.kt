package com.infinity_coder.divcalendar.presentation.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class PaymentVM(
    val name:String,
    val logo:String,
    val count:String,
    val dividends:String,
    val date:String
): IComparableItem {
    override fun id() = name

    override fun content() = this

}
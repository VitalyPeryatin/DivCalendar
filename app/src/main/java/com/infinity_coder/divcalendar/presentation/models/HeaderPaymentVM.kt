package com.infinity_coder.divcalendar.presentation.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class HeaderPaymentVM(
    val id:Int,
    val name:String
):IComparableItem{

    override fun id() = id

    override fun content() = name

}
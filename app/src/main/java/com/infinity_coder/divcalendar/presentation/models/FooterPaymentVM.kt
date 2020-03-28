package com.infinity_coder.divcalendar.presentation.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class FooterPaymentVM(
    val id:Int,
    val income:String
):IComparableItem{

    override fun id() = id

    override fun content() = this

}
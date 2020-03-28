package com.infinity_coder.divcalendar.presentation.mappers

import android.content.Context
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.network.model.PaymentNetworkModel
import com.infinity_coder.divcalendar.presentation.calendar.DividerItem
import com.infinity_coder.divcalendar.presentation.models.FooterPaymentVM
import com.infinity_coder.divcalendar.presentation.models.HeaderPaymentVM
import com.infinity_coder.divcalendar.presentation.models.PaymentVM

class PaymentMapperViewModel(private val context: Context) {

    fun mapPaymentsToViewModels(payments:List<PaymentNetworkModel>):List<IComparableItem>{

        val items = mutableListOf<IComparableItem>()

        val groupMonth = payments.groupBy { it.date.split("-")[1] }.toList()

        for(i in groupMonth.indices) {
            items.add(getHeaderPayment(groupMonth[i].first.toInt()))
            items.addAll(getPayments(groupMonth[i].second))
            items.add(getFooterPayment(groupMonth[i].first.toInt(),groupMonth[i].second))
            if(i != groupMonth.size - 1){
                items.add(DividerItem)
            }
        }

        return items
    }

    private fun getHeaderPayment(month:Int):HeaderPaymentVM{
        return HeaderPaymentVM(
            id = month,
            name = context.resources.getStringArray(R.array.months_nominative_case)[month]
        )
    }

    private fun getPayments(payments:List<PaymentNetworkModel>):List<PaymentVM>{
        return payments.map { payment ->
            PaymentVM(
                name = payment.name,
                logo = payment.logo,
                count = payment.count.toString(),
                dividends = "${payment.dividends}₽",
                date = mapDateToViewModel(payment.date)
            )
        }
    }

    private fun mapDateToViewModel(date:String):String{
        val splitDate = date.split("-")
        val months = context.resources.getStringArray(R.array.months_genitive)
        return "${splitDate[2].toInt()} ${months[splitDate[1].toInt()]} ${splitDate[0]}"
    }

    private fun getFooterPayment(month: Int, payments:List<PaymentNetworkModel>):FooterPaymentVM{
        return  FooterPaymentVM(
            id = month,
            income = "${payments.sumByDouble { payment -> payment.dividends }}₽"
        )
    }

}
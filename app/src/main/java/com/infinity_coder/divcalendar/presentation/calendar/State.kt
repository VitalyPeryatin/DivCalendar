package com.infinity_coder.divcalendar.presentation.calendar

sealed class State{
    object Progress:State()
    data class Data<T>(val data:T):State()
    data class Error(val error:String):State()
}
package com.infinity_coder.divcalendar.presentation.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinity_coder.divcalendar.R

class TabsContainerViewModel:ViewModel() {

    private val _itemMenuId = MutableLiveData<Int>(R.id.portfolioItem)
    val itemMenuId:LiveData<Int>
        get() = _itemMenuId

    fun switchScreen(itemMenuId: Int){
        _itemMenuId.value = itemMenuId
    }
}
package com.infinity_coder.divcalendar.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.repositories.NewsPostsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _newsPosts = MutableLiveData<List<PostDbModel>>()
    val newsPost:LiveData<List<PostDbModel>>
        get() = _newsPosts

    private val _state = MutableLiveData<Int>(VIEW_STATE_NEWS_CONTENT)
    val state:LiveData<Int>
        get() = _state

    fun loadNewsPosts() = viewModelScope.launch {
        _state.postValue(VIEW_STATE_NEWS_LOADING)
        // TODO: Удалить задержку, когда будем получать реальные данные
        delay(1000L)
        val posts = NewsPostsRepository.getPosts()
        _newsPosts.postValue(posts)
        _state.postValue(VIEW_STATE_NEWS_CONTENT)
    }

    companion object {
        const val VIEW_STATE_NEWS_LOADING = 1
        const val VIEW_STATE_NEWS_CONTENT = 2
        const val VIEW_STATE_NEWS_EMPTY = 3
        const val VIEW_STATE_NEWS_NO_NETWORK = 4
    }
}
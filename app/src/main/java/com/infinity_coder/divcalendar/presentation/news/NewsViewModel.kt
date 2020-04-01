package com.infinity_coder.divcalendar.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.domain.NewsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _newsPosts = MutableLiveData<List<PostDbModel>>()
    val newsPost: LiveData<List<PostDbModel>>
        get() = _newsPosts

    private val _state = MutableLiveData<Int>(VIEW_STATE_NEWS_CONTENT)
    val state: LiveData<Int>
        get() = _state


    private val newsInteractor = NewsInteractor()

    fun loadNewsPosts() = viewModelScope.launch {
        try {
            newsInteractor.getPosts()
                .flowOn(Dispatchers.IO)
                .onStart { _state.postValue(VIEW_STATE_NEWS_LOADING) }
                .collect(this@NewsViewModel::collectPosts)
        } catch (e: Exception) {
            _state.postValue(VIEW_STATE_NEWS_NO_NETWORK)
        }
    }

    private suspend fun collectPosts(posts: List<PostDbModel>) {
        _newsPosts.postValue(posts)

        if (posts.isNullOrEmpty()) {
            _state.postValue(VIEW_STATE_NEWS_EMPTY)
        } else {
            _state.postValue(VIEW_STATE_NEWS_CONTENT)
        }
    }

    companion object {
        const val VIEW_STATE_NEWS_LOADING = 1
        const val VIEW_STATE_NEWS_CONTENT = 2
        const val VIEW_STATE_NEWS_EMPTY = 3
        const val VIEW_STATE_NEWS_NO_NETWORK = 4
    }
}
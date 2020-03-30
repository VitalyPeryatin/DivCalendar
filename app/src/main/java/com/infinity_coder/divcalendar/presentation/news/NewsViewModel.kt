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

    private val newsPosts = MutableLiveData<List<PostDbModel>>()
    private val state = MutableLiveData<Int>(VIEW_STATE_NEWS_CONTENT)

    fun getNewsPostsLiveData(): LiveData<List<PostDbModel>> {
        return newsPosts
    }

    fun getStateLiveData(): LiveData<Int> {
        return state
    }

    fun loadNewsPosts() = viewModelScope.launch {
        state.postValue(VIEW_STATE_NEWS_LOADING)
        // TODO: Удалить задержку, когда будем получать реальные данные
        delay(1000L)
        val posts = NewsPostsRepository.getPosts()
        newsPosts.postValue(posts)
        state.postValue(VIEW_STATE_NEWS_CONTENT)
    }

    companion object {
        const val VIEW_STATE_NEWS_LOADING = 1
        const val VIEW_STATE_NEWS_CONTENT = 2
        const val VIEW_STATE_NEWS_EMPTY = 3
        const val VIEW_STATE_NEWS_NO_NETWORK = 4
    }
}
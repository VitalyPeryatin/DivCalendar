package com.infinity_coder.divcalendar.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.domain.NewsInteractor
import com.infinity_coder.divcalendar.presentation._common.logException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewsViewModel : ViewModel() {

    private val _newsPosts = MutableLiveData<List<NewsPostDbModel>>()
    val newsPost: LiveData<List<NewsPostDbModel>>
        get() = _newsPosts

    private val _state = MutableLiveData(VIEW_STATE_NEWS_CONTENT)
    val state: LiveData<Int>
        get() = _state

    private val newsInteractor = NewsInteractor()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadNewsPosts() = viewModelScope.launch {
        newsInteractor.getPosts()
            .flowOn(Dispatchers.IO)
            .onStart { _state.value = VIEW_STATE_NEWS_LOADING }
            .onEach(this@NewsViewModel::collectPosts)
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }

    private suspend fun collectPosts(posts: List<NewsPostDbModel>) {
        _newsPosts.value = posts

        if (posts.isNullOrEmpty()) {
            _state.value = VIEW_STATE_NEWS_EMPTY
        } else {
            _state.value = VIEW_STATE_NEWS_CONTENT
        }
    }

    private fun handleError(error: Throwable) {
        logException(this, error)
        if (error is HttpException) {
            _state.value = VIEW_STATE_NEWS_EMPTY_SECURITIES
        } else {
            _state.value = VIEW_STATE_NEWS_NO_NETWORK
        }
    }

    companion object {
        const val VIEW_STATE_NEWS_LOADING = 1
        const val VIEW_STATE_NEWS_CONTENT = 2
        const val VIEW_STATE_NEWS_EMPTY = 3
        const val VIEW_STATE_NEWS_NO_NETWORK = 4
        const val VIEW_STATE_NEWS_EMPTY_SECURITIES = 5
    }
}
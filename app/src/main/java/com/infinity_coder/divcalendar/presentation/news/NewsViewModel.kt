package com.infinity_coder.divcalendar.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.exceptions.EmptySecuritiesException
import com.infinity_coder.divcalendar.domain.NewsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _newsPosts = MutableLiveData<List<PostDbModel>>()
    val newsPost: LiveData<List<PostDbModel>>
        get() = _newsPosts

    private val _state = MutableLiveData<Int>(VIEW_STATE_NEWS_CONTENT)
    val state: LiveData<Int>
        get() = _state


    private val newsInteractor = NewsInteractor()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadNewsPosts() = viewModelScope.launch {
        newsInteractor.getPosts()
            .flowOn(Dispatchers.IO)
            .onStart { _state.postValue(VIEW_STATE_NEWS_LOADING) }
            .onEach(this@NewsViewModel::collectPosts)
            .catch { handleError(it) }
            .launchIn(viewModelScope)
    }

    private suspend fun collectPosts(posts: List<PostDbModel>) {
        _newsPosts.postValue(posts)

        if (posts.isNullOrEmpty()) {
            _state.postValue(VIEW_STATE_NEWS_EMPTY)
        } else {
            _state.postValue(VIEW_STATE_NEWS_CONTENT)
        }
    }

    private fun handleError(error:Throwable){
        if(error is EmptySecuritiesException){
            _state.postValue(VIEW_STATE_NEWS_EMPTY_SECURITIES)
        }else {
            _state.postValue(VIEW_STATE_NEWS_NO_NETWORK)
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
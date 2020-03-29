package com.infinity_coder.divcalendar.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.repositories.NewsPostsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val newsPosts = MutableLiveData<List<PostDbModel>>()

    fun getNewsPostsLiveData(): LiveData<List<PostDbModel>> {
        return newsPosts
    }

    fun loadNewsPosts() = viewModelScope.launch {
        val posts = NewsPostsRepository.getPosts()
        newsPosts.postValue(posts)
    }
}
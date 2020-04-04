package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.repositories.NewsRepository
import com.infinity_coder.divcalendar.domain._common.LinkDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsInteractor {
    suspend fun getPosts():Flow<List<PostDbModel>> =
        NewsRepository.getPosts(DEFAULT_LIMIT, DEFAULT_OFFSET)
            .map { posts -> posts.filter { LinkDelegate.isValidURL(it.link) } }

    companion object {
        private const val DEFAULT_LIMIT = 50
        private const val DEFAULT_OFFSET = 0
    }
}
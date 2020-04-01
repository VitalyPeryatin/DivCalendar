package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.repositories.NewsRepository
import com.infinity_coder.divcalendar.domain._common.LinkDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class NewsInteractor {
    suspend fun getPosts(): Flow<List<PostDbModel>> =
        NewsRepository.getPosts()
            .map { posts -> posts.filter { LinkDelegate.isValidURL(it.link) } }
}
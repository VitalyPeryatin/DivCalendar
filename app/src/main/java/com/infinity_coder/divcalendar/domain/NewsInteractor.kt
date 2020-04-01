package com.infinity_coder.divcalendar.domain

import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.repositories.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class NewsInteractor {
    suspend fun getPosts(): Flow<List<PostDbModel>> = NewsRepository.getPosts()
}
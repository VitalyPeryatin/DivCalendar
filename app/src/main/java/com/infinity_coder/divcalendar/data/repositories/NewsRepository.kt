package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao

    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun getPosts(): Flow<List<PostDbModel>> =
        flowOf(getPostsFromDatabase(), getPostsFromNetworkAndSaveToDB())
            .flattenConcat()
            .distinctUntilChanged()

    private suspend fun getPostsFromDatabase(): Flow<List<PostDbModel>> =
        flowOf(newsDao.getPosts())

    private suspend fun getPostsFromNetworkAndSaveToDB(): Flow<List<PostDbModel>> {
        return getPostsFromNetwork()
            .map { PostDbModel.from(it) }
            .onEach { savePostToDatabase(it) }
    }

    private suspend fun savePostToDatabase(posts: List<PostDbModel>) {
        newsDao.deleteAll()
        newsDao.insertPosts(posts)
    }

    private fun getPostsFromNetwork(): Flow<List<PostNetworkModel>> {
        return flowOf(
            listOf(
                PostNetworkModel(
                    title = "Заголовок",
                    text = "Текст",
                    poster = "",
                    date = "",
                    source = "",
                    link = ""
                )
            )
        )
    }
}
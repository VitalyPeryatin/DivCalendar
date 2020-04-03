package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao
    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private val divCalendarApi = RetrofitService.divCalendarApi

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPosts(limit: Int, offset: Int): Flow<List<PostDbModel>> = flow {
        emit(getPostsFromNetworkAndSaveToDB(limit, offset))
    }.catch {
        val postsFromDatabase = getPostsFromDatabase()
        if (postsFromDatabase.isEmpty()) {
            throw it
        } else {
            emit(getPostsFromDatabase())
        }
    }

    private suspend fun getPostsFromDatabase(): List<PostDbModel> {
        return newsDao.getPosts()
    }

    private suspend fun getPostsFromNetworkAndSaveToDB(limit: Int, offset: Int): List<PostDbModel> {
        val dbPosts = getPostsFromNetwork(limit, offset).map { PostDbModel.from(it) }
        savePostToDatabase(dbPosts)
        return dbPosts
    }

    private suspend fun savePostToDatabase(posts: List<PostDbModel>) {
        newsDao.deleteAll()
        newsDao.insertPosts(posts)
    }

    private suspend fun getPostsFromNetwork(limit: Int, offset: Int): List<PostNetworkModel> {
        val securities = portfolioDao.getAllSecuritiesPackage().map { it.secid }

        val body = hashMapOf(
            "securities" to securities,
            "limit" to limit,
            "offset" to offset
        )
        return divCalendarApi.fetchPosts(body)
    }
}
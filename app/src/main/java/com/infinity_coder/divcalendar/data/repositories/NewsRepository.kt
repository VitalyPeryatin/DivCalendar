package com.infinity_coder.divcalendar.data.repositories

import android.util.Log
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.exceptions.EmptySecuritiesException
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao
    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private val divCalendarApi = RetrofitService.divCalendarApi

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPosts(limit: Int, offset: Int): Flow<List<PostDbModel>> = flow {
        try {
            emit(getPostsFromNetworkAndSaveToDB(limit, offset))
        } catch (e: Exception) {
            if (e is EmptySecuritiesException) {
                throw e
            } else {
                val postsFromDatabase = getPostsFromDatabase()
                if (postsFromDatabase.isEmpty()) {
                    throw e
                } else {
                    emit(getPostsFromDatabase())
                }
            }
        }
    }

    private suspend fun getPostsFromDatabase(): List<PostDbModel> {
        Log.d("History", "start fetch data from  database")
        delay(3000)
        Log.d("History", "end fetch data from database")
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

        if (securities.isEmpty())
            throw EmptySecuritiesException()

        val body = hashMapOf(
            "securities" to securities,
            "limit" to limit,
            "offset" to offset
        )
        return divCalendarApi.fetchPosts(body)
    }
}
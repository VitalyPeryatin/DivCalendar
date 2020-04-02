package com.infinity_coder.divcalendar.data.repositories

import android.util.Log
import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.exceptions.EmptySecuritiesException
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao
    private val portfolioDao = DivCalendarDatabase.roomDatabase.portfolioDao

    private val divCalendarApi = RetrofitService.divCalendarApi

    suspend fun getPosts(limit:Int, offset:Int): Flow<List<PostDbModel>> =
        flowOf(getPostsFromDatabase(), getPostsFromNetworkAndSaveToDB(limit,offset))
            .flattenConcat()
            .distinctUntilChanged()

    private suspend fun getPostsFromDatabase(): Flow<List<PostDbModel>> =
        flowOf(newsDao.getPosts())

    private suspend fun getPostsFromNetworkAndSaveToDB(limit: Int, offset: Int): Flow<List<PostDbModel>> {
        return getPostsFromNetwork(limit,offset)
            .map { PostDbModel.from(it) }
            .onEach { savePostToDatabase(it) }
    }

    private suspend fun savePostToDatabase(posts: List<PostDbModel>) {
        newsDao.deleteAll()
        newsDao.insertPosts(posts)
    }

    private suspend fun getPostsFromNetwork(limit: Int, offset: Int): Flow<List<PostNetworkModel>> {
        val securities = portfolioDao.getAllSecuritiesPackage().map { it.secid }

        delay(1000)
        if(securities.isEmpty())
            throw EmptySecuritiesException()

        val body = hashMapOf(
            "securities" to securities,
            "limit" to limit,
            "offset" to offset
        )
        return flowOf(divCalendarApi.fetchPosts(body))
    }
}
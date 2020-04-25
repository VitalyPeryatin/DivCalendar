package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.data.network.RetrofitService
import com.infinity_coder.divcalendar.data.network.model.NewsPostNetModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object NewsPostRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao
    private val securityDao = DivCalendarDatabase.roomDatabase.securityDao

    private val divCalendarApi
        get() = RetrofitService.divCalendarApi

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPosts(limit: Int, offset: Int): Flow<List<NewsPostDbModel>> = flow {
        val postsFromDatabase = getPostsFromDatabase()
        emitIf(postsFromDatabase) { postsFromDatabase.isNotEmpty() }
        emit(getPostsFromNetworkAndSaveToDB(limit, offset))
    }

    private suspend fun getPostsFromDatabase(): List<NewsPostDbModel> {
        val tickers = getTickersFromCurrentPortfolio()
        return newsDao.getPosts(tickers)
    }

    private suspend fun getPostsFromNetworkAndSaveToDB(limit: Int, offset: Int): List<NewsPostDbModel> {
        val dbPosts = getPostsFromNetwork(limit, offset).map { NewsPostDbModel.from(it) }
        savePostToDatabase(dbPosts)
        return dbPosts
    }

    private suspend fun savePostToDatabase(posts: List<NewsPostDbModel>) {
        newsDao.deleteAll()
        newsDao.insertPosts(posts)
    }

    private suspend fun getPostsFromNetwork(limit: Int, offset: Int): List<NewsPostNetModel.Response> {
        val tickers = getTickersFromCurrentPortfolio()
        val body = NewsPostNetModel.Request(tickers, limit, offset)
        return divCalendarApi.fetchPosts(body)
    }

    private suspend fun getTickersFromCurrentPortfolio(): List<String> {
        val currentPortfolioId = PortfolioRepository.getCurrentPortfolioId()
        return securityDao.getSecurityPackagesForPortfolio(currentPortfolioId).map { it.ticker }
    }
}
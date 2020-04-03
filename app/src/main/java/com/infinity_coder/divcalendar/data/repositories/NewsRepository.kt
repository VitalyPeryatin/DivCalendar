package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getPosts(): Flow<List<PostDbModel>> = flow {
        emit(getPostsFromDatabase())
        emit(getPostsFromNetworkAndSaveToDB())
    }.distinctUntilChanged()

    private suspend fun getPostsFromDatabase(): List<PostDbModel> =
        newsDao.getPosts()

    private suspend fun getPostsFromNetworkAndSaveToDB(): List<PostDbModel> {
        val networkPosts = getPostsFromNetwork()
        val dbPosts = PostDbModel.from(networkPosts)
        savePostToDatabase(dbPosts)
        return dbPosts
    }

    private suspend fun savePostToDatabase(posts: List<PostDbModel>) {
        newsDao.deleteAll()
        newsDao.insertPosts(posts)
    }

    private fun getPostsFromNetwork(): List<PostNetworkModel> {
        return listOf(
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "DFSF",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            ),
            PostNetworkModel(
                title = "Обзор стратегий FINTARGET.RU",
                text = "На  рынки продолжает поступать негатив относительно состояния мировой экономики, под большим давлением акции банков. В российской экономике возможен шквал банкротств и невозвраты кредитов. Сбербанк одна...",
                logo = "https://s3-symbol-logo.tradingview.com/mts--big.svg",
                ticker = "NLMK",
                date = "12.02.2020",
                link = "https://bcs-express.ru/novosti-i-analitika/daidzhest-strategii-fintarget"
            )
        )

    }
}

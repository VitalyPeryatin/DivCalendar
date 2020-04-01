package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.data.network.model.PostNetworkModel
import kotlinx.coroutines.flow.*

object NewsRepository {

    private val newsDao = DivCalendarDatabase.roomDatabase.newsDao

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
        )
    }
}
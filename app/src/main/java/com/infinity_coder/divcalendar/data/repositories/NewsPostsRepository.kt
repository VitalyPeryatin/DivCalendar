package com.infinity_coder.divcalendar.data.repositories

import com.infinity_coder.divcalendar.data.db.DivCalendarDatabase
import com.infinity_coder.divcalendar.data.db.model.PostDbModel

object NewsPostsRepository {

    private val postsDao = DivCalendarDatabase.roomDatabase.postDao

    suspend fun getPosts(): List<PostDbModel> {
        return listOf(
            PostDbModel(
                title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                author = "Medium",
                payload = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                author = "Dohod",
                payload = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                author = "Medium",
                payload = "qwerty",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "4",
                author = "Medium",
                payload = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.rty",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                author = "Dohod",
                payload = "qwerty",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. ",
                author = "Medium",
                payload = "qwerty",
                date = "22.03.2020"
            ),
            PostDbModel(
                title = "7",
                author = "Dohod",
                payload = "qwerty",
                date = "22.03.2020"
            )
        )
    }
}
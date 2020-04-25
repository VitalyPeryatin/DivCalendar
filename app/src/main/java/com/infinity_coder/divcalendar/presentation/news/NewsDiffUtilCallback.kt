package com.infinity_coder.divcalendar.presentation.news

import androidx.recyclerview.widget.DiffUtil
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.presentation._common.md5

class NewsDiffUtilCallback(
    private val oldPosts: List<NewsPostDbModel>,
    private val newPosts: List<NewsPostDbModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost = oldPosts[oldItemPosition]
        val newPost = newPosts[newItemPosition]
        return md5(oldPost.title + oldPost.text) == md5(newPost.title + newPost.text)
    }

    override fun getOldListSize() = oldPosts.size

    override fun getNewListSize() = newPosts.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost = oldPosts[oldItemPosition]
        val newPost = newPosts[newItemPosition]
        return oldPost == newPost
    }
}
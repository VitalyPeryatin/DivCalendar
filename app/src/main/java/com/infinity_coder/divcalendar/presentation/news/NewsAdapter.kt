package com.infinity_coder.divcalendar.presentation.news

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.NewsPostDbModel
import com.infinity_coder.divcalendar.domain._common.DateFormatter
import com.infinity_coder.divcalendar.presentation._common.SimpleGlide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_post.*

class NewsAdapter(
    var onItemClickListener: NewsItemClickListener? = null
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsPosts: List<NewsPostDbModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return NewsViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int = newsPosts.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val isLastPosition = position == newsPosts.size - 1
        holder.bind(newsPosts[position], isLastPosition)
    }

    fun setPosts(posts: List<NewsPostDbModel>) {
        val newsDiffUtilCallback = NewsDiffUtilCallback(this.newsPosts, posts)
        val newsDiffResult = DiffUtil.calculateDiff(newsDiffUtilCallback)
        this.newsPosts = posts
        newsDiffResult.dispatchUpdatesTo(this)
    }

    class NewsViewHolder(
        override val containerView: View,
        private val onItemClickListener: NewsItemClickListener?
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(post: NewsPostDbModel, isLastItem: Boolean = false) {
            titleTextView.text = post.title
            sourceTextView.text = post.source
            textTextView.text = post.text
            timeAgoTextView.text = timeAgoTextView.context.getDate(post.date)
            SimpleGlide.loadImage(containerView, post.logo, logoImageView)

            postItemLayout.setOnClickListener {
                onItemClickListener?.onClick(post)
            }

            separatorView.visibility = if(isLastItem) View.GONE else View.VISIBLE
        }

        private fun Context.getDate(date: String): String {
            val month = resources.getStringArray(R.array.months_genitive)
            return DateFormatter.formatDateForPosts(date, month)
        }
    }

    interface NewsItemClickListener {
        fun onClick(post: NewsPostDbModel)
    }
}
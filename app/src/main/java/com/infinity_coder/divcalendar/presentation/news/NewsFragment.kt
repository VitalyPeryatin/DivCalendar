package com.infinity_coder.divcalendar.presentation.news

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.data.db.model.PostDbModel
import com.infinity_coder.divcalendar.presentation._common.setActionBar
import com.infinity_coder.divcalendar.presentation._common.viewModel
import com.infinity_coder.divcalendar.presentation.browser.BrowserActivity
import kotlinx.android.synthetic.main.fragment_news.*
import kotlin.properties.Delegates

class NewsFragment : Fragment(R.layout.fragment_news) {

    private var currentState by Delegates.observable(
        NewsViewModel.VIEW_STATE_NEWS_EMPTY, { _, old, new -> changeViewState(new, old) }
    )

    private val adapter = NewsAdapter()

    private val viewModel: NewsViewModel by lazy {
        viewModel { NewsViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsToolbar.title = context!!.resources.getString(R.string.news)
        val parentActivity = (activity as AppCompatActivity)
        parentActivity.setActionBar(newsToolbar)

        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.onItemClickListener = getItemClickListener()
        newsRecyclerView.adapter = adapter

        viewModel.newsPost.observe(viewLifecycleOwner, Observer(this::updateNewsPosts))
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.loadNewsPosts()
    }

    private fun getItemClickListener() = object : NewsAdapter.NewsItemClickListener {
        override fun onClick(post: PostDbModel) {
            openNewsArticle(post)
        }
    }

    private fun updateNewsPosts(posts: List<PostDbModel>) {
        adapter.setPosts(posts)
    }

    fun openNewsArticle(post: PostDbModel) {
        BrowserActivity.openActivity(context!!, post.link)
    }

    private fun updateState(state: Int) {
        currentState = state
    }

    private fun changeViewState(newState: Int, oldState: Int) {
        when (oldState) {
            NewsViewModel.VIEW_STATE_NEWS_CONTENT -> contentLayout.visibility = View.GONE
            NewsViewModel.VIEW_STATE_NEWS_LOADING -> loadingLayout.visibility = View.GONE
            NewsViewModel.VIEW_STATE_NEWS_EMPTY -> emptyLayout.visibility = View.GONE
            NewsViewModel.VIEW_STATE_NEWS_NO_NETWORK -> noNetworkLayout.visibility = View.GONE
        }

        when (newState) {
            NewsViewModel.VIEW_STATE_NEWS_CONTENT -> contentLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_LOADING -> loadingLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_EMPTY -> emptyLayout.visibility = View.VISIBLE
            NewsViewModel.VIEW_STATE_NEWS_NO_NETWORK -> noNetworkLayout.visibility = View.VISIBLE
        }
    }
}
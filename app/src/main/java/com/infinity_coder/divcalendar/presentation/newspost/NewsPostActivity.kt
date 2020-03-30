package com.infinity_coder.divcalendar.presentation.newspost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.viewModel

class NewsPostActivity : AppCompatActivity() {

    private val viewModel: NewsPostViewModel by lazy {
        viewModel { NewsPostViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_post)

        val postId = intent.getLongExtra(BUNDLE_POST_ID, -1)
    }

    companion object {

        private const val BUNDLE_POST_ID = "post_id"

        fun getIntent(context: Context, postId: Long): Intent {
            val intent = Intent(context, NewsPostActivity::class.java)
            intent.putExtra(BUNDLE_POST_ID, postId)
            return intent
        }
    }
}
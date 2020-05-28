package com.infinity_coder.divcalendar.presentation.onboarding

import android.content.Context
import android.text.*
import android.text.Annotation
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinity_coder.divcalendar.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_page.*


class OnboardingPageAdapter(
    private val onboardingPageCallback: OnboardingPageCallback?
):RecyclerView.Adapter<OnboardingPageAdapter.OnboardingPageViewHolder>(){

    private val pages = mutableListOf<OnboardingPageModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingPageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_page, parent, false)
        return OnboardingPageViewHolder(view)
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: OnboardingPageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    fun update(data:List<OnboardingPageModel>){
        pages.clear()
        pages.addAll(data)
        notifyDataSetChanged()
    }

    inner class OnboardingPageViewHolder(override val containerView: View):RecyclerView.ViewHolder(containerView), LayoutContainer{

        fun bind(page:OnboardingPageModel){
            iconImageView.setImageResource(page.icon)
            titleTextView.setText(page.title)

            if(page.message == R.string.onboarding_help_message) {
                messageTextView.apply{
                    text = createLink(context, page.message)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            }else{
                messageTextView.setText(page.message)
            }
        }

        private fun createLink(context: Context,message:Int):SpannableString{
            val fullText = context.getText(message) as SpannedString
            val spannableString = SpannableString(fullText)

            val annotations = fullText.getSpans(0, fullText.length, Annotation::class.java)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onboardingPageCallback?.onClickTelegramChannelLink()
                }

                override fun updateDrawState(ds: TextPaint) {

                }
            }
            annotations?.find {
                it.value == "help_link"
            }?.let {
                spannableString.apply {
                    setSpan(
                        clickableSpan,
                        fullText.getSpanStart(it),
                        fullText.getSpanEnd(it),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                        fullText.getSpanStart(it),
                        fullText.getSpanEnd(it),
                        0
                    )
                    setSpan(
                        UnderlineSpan(),
                        fullText.getSpanStart(it),
                        fullText.getSpanEnd(it),
                        0
                    )
                }
            }
            return spannableString
        }
    }

    interface OnboardingPageCallback{

        fun onClickTelegramChannelLink()

    }
}
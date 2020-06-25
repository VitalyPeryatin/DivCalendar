package com.infinity_coder.divcalendar.presentation.onboarding.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingPageModel(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    @StringRes
    val message: Int
)
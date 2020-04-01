package com.infinity_coder.divcalendar.domain._common

object LinkDelegate {

    private const val REGEX_URL = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?\$"

    fun isValidURL(url: String): Boolean {
        return Regex(REGEX_URL).matches(url)
    }
}
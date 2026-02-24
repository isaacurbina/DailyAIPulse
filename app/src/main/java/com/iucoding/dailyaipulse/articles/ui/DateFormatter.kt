package com.iucoding.dailyaipulse.articles.ui

import android.content.Context
import com.iucoding.dailyaipulse.R
import timber.log.Timber
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun formatDate(rawDate: String, context: Context): String {
    return try {
        val articleDate = ZonedDateTime.parse(rawDate).toLocalDate()
        val today = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(articleDate, today)

        when (daysBetween) {
            0L -> context.getString(R.string.article_date_today)
            1L -> context.getString(R.string.article_date_yesterday)
            else -> context.getString(R.string.article_date_days_ago, daysBetween)
        }
    } catch (e: Exception) {
        Timber.e(e, "Could not parse date")
        rawDate.substringBefore("T")
    }
}

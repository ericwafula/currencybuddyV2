package tech.ericwathome.core.domain.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val ISO_8601_DATE = "yyyy-MM-dd"
const val ISO_8601_DATE_BASIC = "yyyyMMdd"
const val ISO_8601_DATETIME = "yyyy-MM-dd'T'HH:mm:ss"
const val ISO_8601_DATETIME_ZULU = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val ISO_8601_DATETIME_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val ISO_8601_DATETIME_OFFSET = "yyyy-MM-dd'T'HH:mm:ssXXX"
const val ISO_8601_WEEK_DATE = "YYYY-'W'ww-e"
const val ISO_8601_ORDINAL_DATE = "yyyy-DDD"

object DateUtils {
    fun String.toLocalDateTime(format: String = ISO_8601_DATE): LocalDateTime {
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))
    }

    fun LocalDateTime.formated(format: String = ISO_8601_DATETIME): String {
        return this.format(DateTimeFormatter.ofPattern(format))
    }

    fun String.toLocalDate(format: String = ISO_8601_DATETIME): LocalDateTime {
        return LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))
    }

    fun LocalDate.formated(format: String = ISO_8601_DATE): String {
        return this.format(DateTimeFormatter.ofPattern(format))
    }
}
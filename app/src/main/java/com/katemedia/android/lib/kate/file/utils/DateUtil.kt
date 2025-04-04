package com.katemedia.android.lib.kate.file.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class DateUtil {

    companion object {
        /**
         * Date format pattern used to parse HTTP date headers in RFC 1123 format.
         */
        private const val PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz"

        /**
         * Date format pattern used to parse HTTP date headers in RFC 1036 format.
         */
        private const val PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz"

        /**
         * Date format pattern used to parse HTTP date headers in ANSI C
         * `asctime()` format.
         */
        private const val PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy"

        private val DEFAULT_PATTERNS: Collection<String?> =
            listOf(PATTERN_ASCTIME, PATTERN_RFC1036, PATTERN_RFC1123)

        private var DEFAULT_TWO_DIGIT_YEAR_START: Date? = null

        init {
            DEFAULT_TWO_DIGIT_YEAR_START = Calendar.getInstance().apply {
                set(2000, Calendar.JANUARY, 1, 0, 0)
            }.time
        }

        private val GMT: TimeZone = TimeZone.getTimeZone("GMT")

        /**
         * This class should not be instantiated.
         */
        private fun DateUtil() {}


        /**
         * Parses a date value.  The formats used for parsing the date value are retrieved from
         * the default http params.
         *
         * @param dateValue the date value to parse
         * @return the parsed date
         * @throws DateParseException if the value could not be parsed using any of the
         * supported date formats
         */
        fun parseDate(dateValue: String?): Date? {
            return parseDate(dateValue, null, null)
        }

        /**
         * Parses the date value using the given date formats.
         *
         * @param dateValue   the date value to parse
         * @param dateFormats the date formats to use
         * @return the parsed date
         * @throws DateParseException if none of the dataFormats could parse the dateValue
         */
        fun parseDate(dateValue: String?, dateFormats: Collection<String?>?): Date? {
            return parseDate(dateValue, dateFormats, null)
        }

        /**
         * Parses the date value using the given date formats.
         *
         * @param dateValue   the date value to parse
         * @param dateFormats the date formats to use
         * @param startDate   During parsing, two digit years will be placed in the range
         * `startDate` to `startDate + 100 years`. This value may
         * be `null`. When `null` is given as a parameter, year
         * `2000` will be used.
         * @return the parsed date
         * @throws DateParseException if none of the dataFormats could parse the dateValue
         */
        fun parseDate(
            dateValue: String?,
            dateFormats: Collection<String?>?,
            startDate: Date?
        ): Date? {
            var dateValue = dateValue
            var dateFormats = dateFormats
            var startDate: Date? = startDate

            requireNotNull(dateValue) { "dateValue is null" }

            if (dateFormats == null) dateFormats = DEFAULT_PATTERNS
            if (startDate == null) startDate = DEFAULT_TWO_DIGIT_YEAR_START

            // trim single quotes around date if present
            // see issue #5279
            if (dateValue.length > 1 && dateValue.startsWith("'")
                && dateValue.endsWith("'")
            ) {
                dateValue = dateValue.substring(1, dateValue.length - 1)
            }

            var dateParser: SimpleDateFormat? = null
            for (format in dateFormats) {
                if (dateParser == null) {
                    dateParser = SimpleDateFormat(format, Locale.US)
                    dateParser.setTimeZone(TimeZone.getTimeZone("GMT"))
                    dateParser.set2DigitYearStart(startDate)
                } else {
                    dateParser.applyPattern(format)
                }
                try {
                    return dateParser.parse(dateValue)
                } catch (pe: ParseException) {
                    // ignore this exception, we will try the next format
                }
            }

            throw RuntimeException("Unable to parse the date $dateValue")
        }

        /**
         * Formats the given date according to the RFC 1123 pattern.
         *
         * @param date The date to format.
         * @return An RFC 1123 formatted date string.
         * @see .PATTERN_RFC1123
         */
        fun formatDate(date: Date?): String? {
            return formatDate(date, PATTERN_RFC1123)
        }

        /**
         * Formats the given date according to the specified pattern.  The pattern
         * must conform to that used by the [simple date][SimpleDateFormat] class.
         *
         * @param date    The date to format.
         * @param pattern The pattern to use for formatting the date.
         * @return A formatted date string.
         * @throws IllegalArgumentException If the given date pattern is invalid.
         * @see SimpleDateFormat
         */
        fun formatDate(date: Date?, pattern: String?): String? {
            requireNotNull(date) { "date is null" }
            requireNotNull(pattern) { "pattern is null" }

            return SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = GMT
            }.format(date)
        }

    }
}
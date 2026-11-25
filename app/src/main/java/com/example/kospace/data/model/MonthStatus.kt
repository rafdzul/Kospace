package com.example.kospace.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class MonthStatus(
    val monthName: String,
    val monthNumber: Int,
    val isPaid: Boolean
)

data class MonthYearOption(
    val monthNumber: Int,
    val year: Int,
    val label: String
)

object MonthPeriodHelper {
    val fullMonthNames = listOf(
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    )
    val shortMonthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    )

    fun currentYear(): Int = LocalDate.now().year
    fun currentMonthNumber(): Int = LocalDate.now().monthValue

    fun fullMonthYear(monthNumber: Int, year: Int): String {
        val name = fullMonthNames.getOrElse(monthNumber - 1) { "Januari" }
        return "$name $year"
    }

    fun shortDateLabel(date: LocalDate = LocalDate.now()): String {
        val shortMonth = shortMonthNames.getOrElse(date.monthValue - 1) { "Jan" }
        return "${date.dayOfMonth} $shortMonth ${date.year}"
    }

    fun isMonthPaidInPayments(
        payments: List<PembayaranKos>,
        monthNumber: Int,
        year: Int
    ): Boolean {
        val fullName = fullMonthNames.getOrNull(monthNumber - 1) ?: return false
        val shortName = shortMonthNames.getOrNull(monthNumber - 1) ?: return false
        val yearStr = year.toString()
        return payments.any { p ->
            p.status.equals("Lunas", ignoreCase = true) &&
                p.monthPeriod.contains(yearStr) &&
                (p.monthPeriod.contains(fullName, ignoreCase = true) ||
                    p.monthPeriod.startsWith(shortName, ignoreCase = true))
        }
    }

    fun generateUpcomingMonths(monthsAhead: Int = 18): List<MonthYearOption> {
        val start = LocalDate.now().withDayOfMonth(1)
        return (0 until monthsAhead).map { offset ->
            val target = start.plusMonths(offset.toLong())
            MonthYearOption(
                monthNumber = target.monthValue,
                year = target.year,
                label = fullMonthYear(target.monthValue, target.year)
            )
        }
    }
}

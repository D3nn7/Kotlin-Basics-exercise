package driver

import extensions.getDaysBetween
import java.time.LocalDate

data class Vacation(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val duration: Long = endDate.getDaysBetween(startDate)
)

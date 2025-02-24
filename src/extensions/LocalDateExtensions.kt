package extensions

import java.time.Duration
import java.time.LocalDate

fun LocalDate.getDaysBetweenNow(): Long {
    return getDaysBetween(LocalDate.now())
}

fun LocalDate.getDaysBetween(other: LocalDate): Long {
    return Duration.between(
        other,
        this.atStartOfDay()
    ).toDays()
}
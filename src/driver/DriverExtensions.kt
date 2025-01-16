package driver

import java.time.LocalDate

fun IDriver.hasValidLicense(): Boolean {
    return LocalDate.now().isBefore(licenseExpiryDate)
}
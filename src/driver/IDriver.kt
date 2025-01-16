package driver

import vehicle.LicenseType
import java.time.LocalDate

interface IDriver {
    val id: String
    val name: String
    val licenseType: List<LicenseType>
    var status: DriverStatus
    val licenseExpiryDate: LocalDate
}
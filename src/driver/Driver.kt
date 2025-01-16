package driver

import vehicle.LicenseType
import java.time.LocalDate

data class Driver(
    override val id: String,
    override val name: String,
    override val licenseType: List<LicenseType>,
    override var status: DriverStatus,
    override val licenseExpiryDate: LocalDate,
    val phoneNumber: String,
    val email: String,
    val vacationDays: MutableList<LocalDate> = mutableListOf()
): IDriver
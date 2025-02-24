package driver

import java.time.LocalDate
import java.util.Date
import java.util.UUID

interface IDriver {

    val id: UUID
    var state: DriverState
    val driversLicenseCheckedDate: Date
    val name: String
    val birthdate: LocalDate
    val vacationDaysPerYear: Int

    fun changeState(state: DriverState)

    fun getAge(): Long

    fun isDriversLicenseValidationRequired(): Boolean

    fun addVacation(vacation: Vacation): Boolean

    companion object {

        const val DAYS_BETWEEN_DRIVERS_LICENSE_VALIDATIONS = 365
    }
}
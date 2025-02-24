package driver

import java.time.LocalDate
import java.util.*

class InternalDriver(
    override var state: DriverState,
    override val driversLicenseCheckedDate: Date,
    override val name: String,
    override val birthdate: LocalDate,
    override val vacationDaysPerYear: Int
) : AbstractDriver() {

    override fun addVacation(vacation: Vacation): Boolean {

        return vacations.add(vacation)
    }
}
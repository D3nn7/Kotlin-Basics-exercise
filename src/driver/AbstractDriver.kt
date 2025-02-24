package driver

import extensions.getDaysBetweenNow
import java.time.LocalDate
import java.util.*

abstract class AbstractDriver: IDriver {

    val vacations = mutableListOf<Vacation>()

    private val lastDriversLicenseValidation: LocalDate? = null

    override val id: UUID
        get() = UUID.randomUUID()

    override fun changeState(state: DriverState) {
        this.state = state
    }

    override fun getAge(): Long {

        return birthdate.getDaysBetweenNow()
    }

    override fun isDriversLicenseValidationRequired(): Boolean {

        if (lastDriversLicenseValidation == null) {
            return true
        }

        val daysAfterLastValidation = lastDriversLicenseValidation.getDaysBetweenNow()

        return daysAfterLastValidation >= IDriver.DAYS_BETWEEN_DRIVERS_LICENSE_VALIDATIONS
    }
}
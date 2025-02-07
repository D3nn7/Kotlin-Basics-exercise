package repository

import driver.Driver
import driver.LicenseType

interface IDriverRepository : ICRUDRepository<Driver, String> {
    fun findAvailableDrivers(): List<Driver>
    fun findByLicenseType(licenseType: LicenseType): List<Driver>
}
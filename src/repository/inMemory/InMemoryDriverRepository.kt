package repository.inMemory

import driver.Driver
import driver.DriverStatus
import repository.IDriverRepository
import vehicle.LicenseType

class InMemoryDriverRepository : IDriverRepository {
    private val drivers = mutableMapOf<String, Driver>()

    override fun findById(id: String): Driver? = drivers[id]

    override fun findAll(): List<Driver> = drivers.values.toList()

    override fun save(entity: Driver): Driver {
        drivers[entity.id] = entity
        return entity
    }

    override fun delete(id: String): Boolean = drivers.remove(id) != null

    override fun findAvailableDrivers(): List<Driver> {
        return drivers.values.filter { it.status == DriverStatus.AVAILABLE }
    }

    override fun findByLicenseType(licenseType: LicenseType): List<Driver> {
        return drivers.values.filter { it.licenseType.contains(licenseType) }
    }
}
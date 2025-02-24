package repository

import driver.IDriver
import vehicle.IVehicle
import java.util.UUID

interface IRepository {

    fun getAllDrivers(): List<IDriver>

    fun getDriverById(id: UUID): IDriver?

    fun addDriver(driver: IDriver): Boolean

    fun removeDriverById(id: UUID): Boolean

    fun getAllVehicles(): List<IVehicle>

    fun getVehicleById(id: UUID): IVehicle?

    fun addVehicle(vehicle: IVehicle): Boolean

    fun removeVehicleById(id: UUID): Boolean

    fun cleanRepository(): Boolean
}
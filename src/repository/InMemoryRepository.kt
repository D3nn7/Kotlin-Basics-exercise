package repository

import driver.IDriver
import vehicle.IVehicle
import java.util.*

class InMemoryRepository: IRepository {

    private val listOfDrivers = mutableSetOf<IDriver>()
    private val listOfVehicles = mutableSetOf<IVehicle>()

    override fun getAllDrivers(): List<IDriver> {

        return listOfDrivers.toList()
    }

    override fun getDriverById(id: UUID): IDriver? {

       return listOfDrivers.find { it.id == id }
    }

    override fun addDriver(driver: IDriver): Boolean {

        return listOfDrivers.add(driver)
    }

    override fun removeDriverById(id: UUID): Boolean {

        return listOfDrivers.removeIf { it.id == id }
    }

    override fun getAllVehicles(): List<IVehicle> {

        return listOfVehicles.toList()
    }

    override fun getVehicleById(id: UUID): IVehicle? {

        return listOfVehicles.find { it.id == id }
    }

    override fun addVehicle(vehicle: IVehicle): Boolean {

        return listOfVehicles.add(vehicle)
    }

    override fun removeVehicleById(id: UUID): Boolean {

        return listOfVehicles.removeIf { it.id == id }
    }

    override fun cleanRepository(): Boolean {

        listOfDrivers.clear()
        listOfVehicles.clear()
        return true
    }
}
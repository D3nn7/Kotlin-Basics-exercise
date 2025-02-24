package service

import console.ConsoleOutput
import main
import processor.DriverProcessor
import processor.VehicleProcessor
import repository.IRepository
import repository.InMemoryRepository
import vehicle.*
import java.sql.Driver
import kotlin.math.max

class FleetService(
    private val repository: IRepository
) {

    private val vehicleProcessor = VehicleProcessor()
    private val driverProcessor = DriverProcessor()

    fun createVehicle(
        type: VehicleType,
        name: String,
        numberplate: String,
        vehicleState: String,
        maxLoadInKilo: Int,
        mileageInKilometers: Double,
        fuelType: String,
        maintenanceScheduleInKilometers: Double,
    ) {

        val vehicle = vehicleProcessor.createVehicle(
            type,
            name,
            numberplate,
            vehicleState,
            maxLoadInKilo,
            mileageInKilometers,
            fuelType,
            maintenanceScheduleInKilometers
        )

        if (vehicle != null) {
            if (repository.addVehicle(vehicle))
                ConsoleOutput.text("Fahrzeug wurde angelegt")
            else
                ConsoleOutput.error("Fahrzeug konnte nicht angelegt werden")
        }
    }
}
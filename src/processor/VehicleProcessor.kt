package processor

import console.ConsoleOutput
import vehicle.*

class VehicleProcessor {

    fun createVehicle(
        type: VehicleType,
        name: String,
        numberplate: String,
        vehicleState: String,
        maxLoadInKilo: Int,
        mileageInKilometers: Double,
        fuelType: String,
        maintenanceScheduleInKilometers: Double,
    ): IVehicle? {

        try {
            return when (type) {
                VehicleType.CAR -> Car(
                    name = name,
                    numberplate = numberplate,
                    state = VehicleState.valueOf(vehicleState),
                    maxLoadInKilo = maxLoadInKilo,
                    mileageInKilometers = mileageInKilometers,
                    fuelType = FuelType.valueOf(fuelType),
                    maintenanceScheduleInKilometers = maintenanceScheduleInKilometers,
                    null
                )

                VehicleType.TRUCK -> Truck(
                    name = name,
                    numberplate = numberplate,
                    state = VehicleState.valueOf(vehicleState),
                    maxLoadInKilo = maxLoadInKilo,
                    mileageInKilometers = mileageInKilometers,
                    fuelType = FuelType.valueOf(fuelType),
                    maintenanceScheduleInKilometers = maintenanceScheduleInKilometers,
                    null
                )
            }
        } catch (ex: Exception) {
            if (ex is IllegalStateException) {
                ConsoleOutput.error("Vehicle state or fuel type is not supported")
            } else {
                ConsoleOutput.error(ex)
            }
        }

        return null
    }
}
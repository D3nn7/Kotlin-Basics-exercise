package vehicle

import java.util.Date

class VehicleMaintenance(
    private val vehicle: IVehicle,
    private val maintenanceIntervalInKilometers: Double
) {
    private var lastCheck: Date? = null
    private var lastMileageInKilometers: Double = 0.0

    fun check() {
        lastCheck = Date()
        lastMileageInKilometers = vehicle.mileageInKilometers

        println("Maintenance was successful. Next is needed after $maintenanceIntervalInKilometers kilometers.")
    }

    fun isMaintenanceNeeded(): Boolean {
        return (vehicle.mileageInKilometers - lastMileageInKilometers) < maintenanceIntervalInKilometers
    }

    override fun toString(): String {

        return if (isMaintenanceNeeded()) {
            "Maintenance needed!"
        } else {
            "Last maintenance was ${if(lastCheck != null) "at $lastCheck" else "never"}. Next is needed after ${maintenanceIntervalInKilometers - (vehicle.mileageInKilometers - lastMileageInKilometers)} kilometers."
        }
    }
}
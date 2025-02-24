package vehicle

import java.util.UUID

interface IVehicle {
    val id: UUID
    val name: String
    val numberplate: String
    var state: VehicleState
    val maxLoadInKilo: Int
    val mileageInKilometers: Double
    val fuelType: FuelType
    val maintenanceScheduleInKilometers: Double
    val assignedDriverId: UUID?

    fun checkIfNextMaintenanceIsRequired(): Boolean

    fun displayMaintenanceInformation()
}
package vehicle

import java.util.*

class Car(
    override val name: String,
    override val numberplate: String,
    override var state: VehicleState,
    override val maxLoadInKilo: Int,
    override val mileageInKilometers: Double,
    override val fuelType: FuelType,
    override val maintenanceScheduleInKilometers: Double,
    override val assignedDriverId: UUID?
) : AbstractVehicle()
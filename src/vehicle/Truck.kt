package vehicle

import java.util.*

class Truck(
    override val name: String,
    override val numberplate: String,
    override var state: VehicleState,
    override val maxLoadInKilo: Int,
    override val mileageInKilometers: Double = 0.0,
    override val fuelType: FuelType,
    override val maintenanceScheduleInKilometers: Double = 25000.0,
    override val assignedDriverId: UUID?
) : AbstractVehicle()
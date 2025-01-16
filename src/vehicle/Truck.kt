package vehicle

import trailer.TrailerType
import java.time.LocalDate

data class Truck(
    override val id: String,
    override val model: String,
    override val capacity: Double,
    override val fuelType: FuelType,
    override var status: VehicleStatus,
    val axles: Int,
    val trailerType: TrailerType,
) : AbstractVehicle() {
    override fun scheduleNextMaintenance(): LocalDate {
        return LocalDate.now().plusDays(30)
    }
}
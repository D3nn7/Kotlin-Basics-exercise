package vehicle

import java.time.LocalDate

interface IVehicle {
    val id: String
    val model: String
    val capacity: Double
    val fuelType: FuelType
    var status: VehicleStatus
    var currentFuelLevel: Double
    var lastMaintenanceDate: LocalDate

    fun needsMaintenance(): Boolean
    fun isAvailableForDelivery(): Boolean
}
package repository

import vehicle.IVehicle
import vehicle.VehicleStatus

interface IVehicleRepository : ICRUDRepository<IVehicle, String> {
    fun findAvailableVehicles(): List<IVehicle>
    fun findByStatus(status: VehicleStatus): List<IVehicle>
}
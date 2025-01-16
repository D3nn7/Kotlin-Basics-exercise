package repository.inMemory

import repository.IVehicleRepository
import vehicle.IVehicle
import vehicle.VehicleStatus

class InMemoryVehicleRepository: IVehicleRepository {
    private val vehicles = mutableMapOf<String, IVehicle>()

    override fun findById(id: String): IVehicle? = vehicles[id]

    override fun findAll(): List<IVehicle> = vehicles.values.toList()

    override fun save(entity: IVehicle): IVehicle {
        vehicles[entity.id] = entity
        return entity
    }

    override fun delete(id: String): Boolean = vehicles.remove(id) != null

    override fun findAvailableVehicles(): List<IVehicle> {
        return vehicles.values.filter { it.isAvailableForDelivery() }
    }

    override fun findByStatus(status: VehicleStatus): List<IVehicle> {
        return vehicles.values.filter { it.status == status }
    }
}
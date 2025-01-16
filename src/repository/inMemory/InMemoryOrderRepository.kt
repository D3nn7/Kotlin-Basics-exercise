package repository.inMemory

import order.DeliveryOrder
import order.OrderStatus
import repository.IOrderRepository

class InMemoryOrderRepository : IOrderRepository {
    private val orders = mutableMapOf<String, DeliveryOrder>()

    override fun findById(id: String): DeliveryOrder? = orders[id]

    override fun findAll(): List<DeliveryOrder> = orders.values.toList()

    override fun save(entity: DeliveryOrder): DeliveryOrder {
        orders[entity.id] = entity
        return entity
    }

    override fun delete(id: String): Boolean = orders.remove(id) != null

    override fun findByStatus(status: OrderStatus): List<DeliveryOrder> {
        return orders.values.filter { it.status == status }
    }

    override fun findByDriver(driverId: String): List<DeliveryOrder> {
        return orders.values.filter { it.assignedDriver?.id == driverId }
    }

    override fun findByVehicle(vehicleId: String): List<DeliveryOrder> {
        return orders.values.filter { it.assignedVehicle?.id == vehicleId }
    }

    override fun findActiveOrders(): List<DeliveryOrder> {
        return orders.values.filter {
            it.status == OrderStatus.CREATED ||
                    it.status == OrderStatus.ASSIGNED ||
                    it.status == OrderStatus.IN_TRANSIT
        }
    }
}

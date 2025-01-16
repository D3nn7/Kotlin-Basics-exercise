package repository

import order.DeliveryOrder
import order.OrderStatus

interface IOrderRepository : ICRUDRepository<DeliveryOrder, String> {
    fun findByStatus(status: OrderStatus): List<DeliveryOrder>

    fun findByDriver(driverId: String): List<DeliveryOrder>

    fun findByVehicle(vehicleId: String): List<DeliveryOrder>

    fun findActiveOrders(): List<DeliveryOrder>
}
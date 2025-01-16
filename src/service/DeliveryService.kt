package service

import cargo.ICargo
import driver.Driver
import driver.DriverStatus
import driver.hasValidLicense
import order.DeliveryOrder
import order.OrderStatus
import order.isReadyForDelivery
import repository.*
import vehicle.IVehicle
import vehicle.VehicleStatus
import vehicle.canHandleCargo
import java.time.LocalDateTime
import java.util.*
import kotlin.NoSuchElementException

class DeliveryService(
    val vehicleRepository: IVehicleRepository,
    val driverRepository: IDriverRepository,
    val orderRepository: IOrderRepository
) {
    fun createDeliveryOrder(cargo: List<ICargo>, deliveryDate: LocalDateTime): Result<DeliveryOrder> {
        return try {
            val order = DeliveryOrder(
                id = UUID.randomUUID().toString(),
                cargo = cargo,
                deliveryDate = deliveryDate,
                status = OrderStatus.CREATED
            )
            Result.success(orderRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun assignVehicleToOrder(orderId: String, vehicleId: String): Result<DeliveryOrder> {
        val order = orderRepository.findById(orderId) ?: return Result.failure(
            NoSuchElementException("Order not found")
        )
        val vehicle = vehicleRepository.findById(vehicleId) ?: return Result.failure(
            NoSuchElementException("Vehicle not found")
        )

        return try {
            validateVehicleAssignment(vehicle, order)
            order.assignedVehicle = vehicle
            vehicle.status = VehicleStatus.OCCUPIED
            Result.success(orderRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun assignDriverToOrder(orderId: String, driverId: String): Result<DeliveryOrder> {
        val order = orderRepository.findById(orderId) ?: return Result.failure(
            NoSuchElementException("Order not found")
        )
        val driver = driverRepository.findById(driverId) ?: return Result.failure(
            NoSuchElementException("Driver not found")
        )

        return try {
            validateDriverAssignment(driver)
            order.assignedDriver = driver
            driver.status = DriverStatus.ON_DUTY
            Result.success(orderRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun validateVehicleAssignment(vehicle: IVehicle, order: DeliveryOrder) {
        if (!vehicle.isAvailableForDelivery()) {
            throw IllegalStateException("Vehicle is not available")
        }

        if (!vehicle.canHandleCargo(order.cargo)) {
            throw IllegalStateException("Vehicle cannot handle cargo")
        }
    }

    private fun validateDriverAssignment(driver: Driver) {
        if (driver.status != DriverStatus.AVAILABLE) {
            throw IllegalStateException("Driver is not available")
        }

        if (!driver.hasValidLicense()) {
            throw IllegalStateException("Driver's license is expired")
        }
    }

    fun startDelivery(orderId: String): Result<DeliveryOrder> {
        val order = orderRepository.findById(orderId) ?: return Result.failure(
            NoSuchElementException("Order not found")
        )

        return try {
            if (!order.isReadyForDelivery()) {
                throw IllegalStateException("Order is not ready for delivery. Check vehicle and driver assignment.")
            }

            order.status = OrderStatus.IN_TRANSIT
            Result.success(orderRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<DeliveryOrder> {
        val order = orderRepository.findById(orderId) ?: return Result.failure(
            NoSuchElementException("Order not found")
        )

        return try {
            when (newStatus) {
                OrderStatus.IN_TRANSIT -> {
                    if (!order.isReadyForDelivery()) {
                        throw IllegalStateException("Order is not ready for delivery")
                    }
                }
                OrderStatus.DELIVERED -> {
                    // Fahrzeug und Fahrer wieder verfügbar machen
                    order.assignedVehicle?.status = VehicleStatus.AVAILABLE
                    order.assignedDriver?.status = DriverStatus.AVAILABLE
                }
                else -> { /* Andere Status erfordern keine speziellen Prüfungen */ }
            }

            order.status = newStatus
            Result.success(orderRepository.save(order))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

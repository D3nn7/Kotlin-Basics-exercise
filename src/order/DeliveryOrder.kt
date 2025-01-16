package order

import cargo.ICargo
import driver.IDriver
import vehicle.IVehicle
import java.time.LocalDateTime

data class DeliveryOrder(
    val id: String,
    val cargo: List<ICargo>,
    var assignedVehicle: IVehicle? = null,
    var assignedDriver: IDriver? = null,
    val deliveryDate: LocalDateTime,
    var status: OrderStatus
)
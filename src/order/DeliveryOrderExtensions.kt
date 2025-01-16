package order

fun DeliveryOrder.isReadyForDelivery(): Boolean {
    return assignedVehicle != null &&
            assignedDriver != null &&
            status == OrderStatus.ASSIGNED
}
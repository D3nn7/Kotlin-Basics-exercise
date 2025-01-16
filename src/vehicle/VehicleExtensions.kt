package vehicle

import cargo.Cargo
import cargo.CargoType
import cargo.ICargo
import trailer.TrailerType

fun IVehicle.canHandleCargo(cargo: List<ICargo>): Boolean {
    return cargo.sumOf { it.weight } <= capacity
}

fun Truck.canHandleCargo(cargo: List<Cargo>): Boolean {

    if (cargo.any { it.isHazardous } && trailerType != TrailerType.HAZARDOUS) {
        return false
    }

    if (cargo.any { it.type == CargoType.PERISHABLE } && trailerType != TrailerType.REFRIGERATED) {
        return false
    }

    return true
}
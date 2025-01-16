package vehicle

import maintenance.IMaintenanceSchedule
import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class AbstractVehicle : IVehicle, IMaintenanceSchedule {
    override var currentFuelLevel: Double = 100.0
    override var lastMaintenanceDate: LocalDate = LocalDate.now()

    override fun needsMaintenance(): Boolean {
        return isMaintenanceOverdue() || currentFuelLevel < 20.0
    }

    override fun isAvailableForDelivery(): Boolean {
        return status == VehicleStatus.AVAILABLE && !needsMaintenance()
    }

    override fun isMaintenanceOverdue(): Boolean {
        return ChronoUnit.DAYS.between(lastMaintenanceDate, LocalDate.now()) > 30
    }
}
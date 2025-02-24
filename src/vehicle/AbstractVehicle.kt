package vehicle

import java.util.*

abstract class AbstractVehicle: IVehicle {

    private val maintenance: VehicleMaintenance by lazy {

        VehicleMaintenance(this, maintenanceScheduleInKilometers)
    }

    override val id: UUID
        get() = UUID.randomUUID()

    override fun checkIfNextMaintenanceIsRequired(): Boolean {

        return maintenance.isMaintenanceNeeded()
    }

    override fun displayMaintenanceInformation() {

        println(maintenance.toString())
    }
}
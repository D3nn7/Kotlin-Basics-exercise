import driver.Driver
import driver.DriverStatus
import repository.IDriverRepository
import repository.IVehicleRepository
import repository.inMemory.InMemoryDriverRepository
import repository.inMemory.InMemoryOrderRepository
import repository.inMemory.InMemoryVehicleRepository
import service.DeliveryService
import trailer.TrailerType
import ui.ConsoleUI
import vehicle.FuelType
import vehicle.LicenseType
import vehicle.Truck
import vehicle.VehicleStatus
import java.time.LocalDate

fun main() {
    val vehicleRepository = InMemoryVehicleRepository()
    val driverRepository = InMemoryDriverRepository()
    val orderRepository = InMemoryOrderRepository()

    val deliveryService = DeliveryService(
        vehicleRepository = vehicleRepository,
        driverRepository = driverRepository,
        orderRepository = orderRepository
    )

    initializeSampleData(vehicleRepository, driverRepository)

    val consoleUI = ConsoleUI(deliveryService)
    consoleUI.start()
}

private fun initializeSampleData(
    vehicleRepository: IVehicleRepository,
    driverRepository: IDriverRepository
) {
    val truck1 = Truck(
        id = "V001",
        model = "Mercedes Actros",
        capacity = 20000.0,
        fuelType = FuelType.DIESEL,
        status = VehicleStatus.AVAILABLE,
        axles = 3,
        trailerType = TrailerType.BOX
    )

    val truck2 = Truck(
        id = "V002",
        model = "MAN TGX",
        capacity = 25000.0,
        fuelType = FuelType.DIESEL,
        status = VehicleStatus.MAINTENANCE,
        axles = 4,
        trailerType = TrailerType.FLATBED
    )

    vehicleRepository.save(truck1)
    vehicleRepository.save(truck2)

    val driver1 = Driver(
        id = "D001",
        name = "Max Mustermann",
        licenseType = listOf(LicenseType.CE, LicenseType.C),
        status = DriverStatus.AVAILABLE,
        licenseExpiryDate = LocalDate.now().plusYears(2),
        phoneNumber = "0123456789",
        email = "max.mustermann@logistik.de"
    )

    val driver2 = Driver(
        id = "D002",
        name = "Anna Schmidt",
        licenseType = listOf(LicenseType.CE, LicenseType.C, LicenseType.B),
        status = DriverStatus.AVAILABLE,
        licenseExpiryDate = LocalDate.now().plusYears(3),
        phoneNumber = "0987654321",
        email = "anna.schmidt@logistik.de"
    )

    driverRepository.save(driver1)
    driverRepository.save(driver2)
}

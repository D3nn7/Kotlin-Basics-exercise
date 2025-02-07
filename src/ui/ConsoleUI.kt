package ui

import cargo.Cargo
import cargo.CargoType
import cargo.Dimensions
import driver.Driver
import driver.DriverStatus
import order.OrderStatus
import service.DeliveryService
import vehicle.FuelType
import driver.LicenseType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

class ConsoleUI(private val deliveryService: DeliveryService) {
    fun start() {
        while (true) {
            println("""
                Logistik-Management-System
                -------------------------
                1. Fahrzeugverwaltung
                2. Fahrerverwaltung
                3. Auftragsverwaltung
                4. Beenden
                
                Bitte wählen Sie eine Option:
            """.trimIndent())

            when (readlnOrNull()) {
                "1" -> handleVehicleManagement()
                "2" -> handleDriverManagement()
                "3" -> handleOrderManagement()
                "4" -> break
                else -> println("Ungültige Eingabe")
            }
        }
    }

    private fun handleVehicleManagement() {
        println("""
            Fahrzeugverwaltung
            -----------------
            1. Neues Fahrzeug anlegen
            2. Fahrzeug suchen
            3. Alle Fahrzeuge anzeigen
            4. Fahrzeug löschen
            5. Zurück
        """.trimIndent())

        when (readlnOrNull()) {
            "1" -> createNewVehicle()
            "2" -> searchVehicle()
            "3" -> showAllVehicles()
            "4" -> deleteVehicle()
            "5" -> return
            else -> println("Ungültige Eingabe")
        }
    }

    private fun createNewVehicle() {
        println("Fahrzeugmodell eingeben:")
        val model = readlnOrNull() ?: return
        println("Kapazität eingeben (in kg):")
        val capacity = readlnOrNull()?.toDoubleOrNull() ?: return
        println("Kraftstoffart wählen (DIESEL, PETROL, ELECTRIC, HYDROGEN):")
        val fuelType = readlnOrNull()?.let { FuelType.valueOf(it) } ?: return

        // Weitere Implementierung...
    }

    private fun handleDriverManagement() {
        println("""
            Fahrerverwaltung
            ---------------
            1. Neuen Fahrer anlegen
            2. Fahrer suchen
            3. Alle Fahrer anzeigen
            4. Fahrer löschen
            5. Urlaubsplanung
            6. Zurück
        """.trimIndent())

        when (readlnOrNull()) {
            "1" -> createNewDriver()
            "2" -> searchDriver()
            "3" -> showAllDrivers()
            "4" -> deleteDriver()
            "5" -> handleVacationPlanning()
            "6" -> return
            else -> println("Ungültige Eingabe")
        }
    }

    private fun handleOrderManagement() {
        println("""
            Auftragsverwaltung
            ----------------
            1. Neuen Auftrag erstellen
            2. Auftrag suchen
            3. Alle Aufträge anzeigen
            4. Auftrag Fahrzeug/Fahrer zuweisen
            5. Auftragsstatus ändern
            6. Zurück
        """.trimIndent())

        when (readlnOrNull()) {
            "1" -> createNewOrder()
            "2" -> searchOrder()
            "3" -> showAllOrders()
            "4" -> assignVehicleAndDriverToOrder()
            "5" -> updateOrderStatus()
            "6" -> return
            else -> println("Ungültige Eingabe")
        }
    }

    private fun searchVehicle() {
        println("Fahrzeug-ID eingeben:")
        val id = readlnOrNull() ?: return

        try {
            val vehicle = deliveryService.vehicleRepository.findById(id)
            if (vehicle != null) {
                println("""
                    Fahrzeugdetails:
                    ID: ${vehicle.id}
                    Modell: ${vehicle.model}
                    Kapazität: ${vehicle.capacity} kg
                    Status: ${vehicle.status}
                    Kraftstoff: ${vehicle.fuelType}
                    Kraftstoffstand: ${vehicle.currentFuelLevel}%
                    Letzte Wartung: ${vehicle.lastMaintenanceDate}
                """.trimIndent())
            } else {
                println("Fahrzeug nicht gefunden.")
            }
        } catch (e: Exception) {
            println("Fehler bei der Suche: ${e.message}")
        }
    }

    private fun showAllVehicles() {
        try {
            val vehicles = deliveryService.vehicleRepository.findAll()
            if (vehicles.isEmpty()) {
                println("Keine Fahrzeuge verfügbar.")
                return
            }

            println("Alle Fahrzeuge:")
            println("ID | Modell | Status | Kapazität | Kraftstoff")
            println("-".repeat(50))

            vehicles.forEach { vehicle ->
                println("${vehicle.id} | ${vehicle.model} | ${vehicle.status} | ${vehicle.capacity}kg | ${vehicle.fuelType}")
            }
        } catch (e: Exception) {
            println("Fehler beim Anzeigen der Fahrzeuge: ${e.message}")
        }
    }

    private fun deleteVehicle() {
        println("ID des zu löschenden Fahrzeugs eingeben:")
        val id = readlnOrNull() ?: return

        try {
            val success = deliveryService.vehicleRepository.delete(id)
            if (success) {
                println("Fahrzeug erfolgreich gelöscht.")
            } else {
                println("Fahrzeug konnte nicht gelöscht werden.")
            }
        } catch (e: Exception) {
            println("Fehler beim Löschen: ${e.message}")
        }
    }

    private fun createNewDriver() {
        println("Name des Fahrers:")
        val name = readlnOrNull() ?: return

        println("Führerscheinklassen (kommagetrennt: A,B,C,CE,D):")
        val licenseTypes = readlnOrNull()?.split(",")?.map {
            try {
                LicenseType.valueOf(it.trim())
            } catch (e: IllegalArgumentException) {
                null
            }
        }?.filterNotNull() ?: return

        println("Führerschein gültig bis (YYYY-MM-DD):")
        val expiryDate = readlnOrNull()?.let {
            try {
                LocalDate.parse(it)
            } catch (e: Exception) {
                println("Ungültiges Datum")
                return
            }
        } ?: return

        println("Telefonnummer:")
        val phone = readlnOrNull() ?: return

        println("E-Mail:")
        val email = readlnOrNull() ?: return

        try {
            val driver = Driver(
                id = UUID.randomUUID().toString(),
                name = name,
                licenseType = licenseTypes,
                status = DriverStatus.AVAILABLE,
                licenseExpiryDate = expiryDate,
                phoneNumber = phone,
                email = email
            )
            deliveryService.driverRepository.save(driver)
            println("Fahrer erfolgreich angelegt.")
        } catch (e: Exception) {
            println("Fehler beim Anlegen des Fahrers: ${e.message}")
        }
    }

    private fun createNewOrder() {
        println("Waren für den Auftrag eingeben:")
        val cargoList = mutableListOf<Cargo>()

        do {
            println("Beschreibung der Ware:")
            val description = readlnOrNull() ?: break

            println("Gewicht in kg:")
            val weight = readlnOrNull()?.toDoubleOrNull() ?: break

            println("Gefahrgut? (ja/nein):")
            val isHazardous = readlnOrNull()?.lowercase() == "ja"

            val cargo = Cargo(
                id = UUID.randomUUID().toString(),
                weight = weight,
                description = description,
                type = if (isHazardous) CargoType.HAZARDOUS else CargoType.GENERAL,
                dimensions = Dimensions(1.0, 1.0, 1.0), // Vereinfachte Dimensionen
                isHazardous = isHazardous
            )
            cargoList.add(cargo)

            println("Weitere Ware hinzufügen? (ja/nein):")
        } while (readlnOrNull()?.lowercase() == "ja")

        println("Lieferdatum (YYYY-MM-DD HH:mm):")
        val deliveryDate = readlnOrNull()?.let {
            try {
                LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            } catch (e: Exception) {
                println("Ungültiges Datum")
                return
            }
        } ?: return

        try {
            val result = deliveryService.createDeliveryOrder(cargoList, deliveryDate)
            if (result.isSuccess) {
                println("Auftrag erfolgreich erstellt. Auftrags-ID: ${result.getOrNull()?.id}")
            } else {
                println("Fehler beim Erstellen des Auftrags: ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            println("Fehler beim Erstellen des Auftrags: ${e.message}")
        }
    }

    private fun assignVehicleAndDriverToOrder() {
        println("Auftrags-ID eingeben:")
        val orderId = readlnOrNull() ?: return

        println("Fahrzeug-ID eingeben:")
        val vehicleId = readlnOrNull() ?: return

        println("Fahrer-ID eingeben:")
        val driverId = readlnOrNull() ?: return

        try {
            val vehicleResult = deliveryService.assignVehicleToOrder(orderId, vehicleId)
            if (vehicleResult.isFailure) {
                println("Fehler bei der Fahrzeugzuweisung: ${vehicleResult.exceptionOrNull()?.message}")
                return
            }

            val driverResult = deliveryService.assignDriverToOrder(orderId, driverId)
            if (driverResult.isFailure) {
                println("Fehler bei der Fahrerzuweisung: ${driverResult.exceptionOrNull()?.message}")
                return
            }

            println("Fahrzeug und Fahrer erfolgreich zugewiesen.")
        } catch (e: Exception) {
            println("Fehler bei der Zuweisung: ${e.message}")
        }
    }

    private fun searchDriver() {
        println("Fahrer-ID eingeben:")
        val id = readlnOrNull() ?: return

        try {
            val driver = deliveryService.driverRepository.findById(id)
            if (driver != null) {
                println("""
                    Fahrerdetails:
                    ID: ${driver.id}
                    Name: ${driver.name}
                    Status: ${driver.status}
                    Führerscheinklassen: ${driver.licenseType.joinToString(", ")}
                    Führerschein gültig bis: ${driver.licenseExpiryDate}
                    Telefon: ${driver.phoneNumber}
                    E-Mail: ${driver.email}
                    Urlaubstage: ${driver.vacationDays.joinToString(", ")}
                """.trimIndent())
            } else {
                println("Fahrer nicht gefunden.")
            }
        } catch (e: Exception) {
            println("Fehler bei der Suche: ${e.message}")
        }
    }

    private fun showAllDrivers() {
        try {
            val drivers = deliveryService.driverRepository.findAll()
            if (drivers.isEmpty()) {
                println("Keine Fahrer verfügbar.")
                return
            }

            println("Alle Fahrer:")
            println("ID | Name | Status | Führerscheinklassen | Gültig bis")
            println("-".repeat(70))

            drivers.forEach { driver ->
                println("${driver.id} | ${driver.name} | ${driver.status} | " +
                        "${driver.licenseType.joinToString(",")} | ${driver.licenseExpiryDate}")
            }
        } catch (e: Exception) {
            println("Fehler beim Anzeigen der Fahrer: ${e.message}")
        }
    }

    private fun deleteDriver() {
        println("ID des zu löschenden Fahrers eingeben:")
        val id = readlnOrNull() ?: return

        try {
            val success = deliveryService.driverRepository.delete(id)
            if (success) {
                println("Fahrer erfolgreich gelöscht.")
            } else {
                println("Fahrer konnte nicht gelöscht werden.")
            }
        } catch (e: Exception) {
            println("Fehler beim Löschen: ${e.message}")
        }
    }

    private fun handleVacationPlanning() {
        println("""
            Urlaubsplanung
            -------------
            1. Urlaub eintragen
            2. Urlaub stornieren
            3. Urlaubsübersicht
            4. Zurück
        """.trimIndent())

        when (readlnOrNull()) {
            "1" -> addVacation()
            "2" -> removeVacation()
            "3" -> showVacationOverview()
            "4" -> return
            else -> println("Ungültige Eingabe")
        }
    }

    private fun addVacation() {
        println("Fahrer-ID eingeben:")
        val driverId = readlnOrNull() ?: return

        println("Urlaubsbeginn (YYYY-MM-DD):")
        val startDate = readlnOrNull()?.let { LocalDate.parse(it) } ?: return

        println("Urlaubsende (YYYY-MM-DD):")
        val endDate = readlnOrNull()?.let { LocalDate.parse(it) } ?: return

        try {
            val driver = deliveryService.driverRepository.findById(driverId) ?: throw Exception("Fahrer nicht gefunden")
            val vacationDays = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList())
            driver.vacationDays.addAll(vacationDays)
            deliveryService.driverRepository.save(driver)
            println("Urlaub erfolgreich eingetragen.")
        } catch (e: Exception) {
            println("Fehler beim Eintragen des Urlaubs: ${e.message}")
        }
    }

    private fun removeVacation() {
        println("Fahrer-ID eingeben:")
        val driverId = readlnOrNull() ?: return

        println("Datum des zu stornierenden Urlaubs (YYYY-MM-DD):")
        val date = readlnOrNull()?.let { LocalDate.parse(it) } ?: return

        try {
            val driver = deliveryService.driverRepository.findById(driverId) ?: throw Exception("Fahrer nicht gefunden")
            driver.vacationDays.remove(date)
            deliveryService.driverRepository.save(driver)
            println("Urlaub erfolgreich storniert.")
        } catch (e: Exception) {
            println("Fehler beim Stornieren des Urlaubs: ${e.message}")
        }
    }

    private fun showVacationOverview() {
        println("Fahrer-ID eingeben (leer lassen für alle Fahrer):")
        val driverId = readlnOrNull()

        try {
            if (driverId.isNullOrBlank()) {
                val drivers = deliveryService.driverRepository.findAll()
                drivers.forEach { driver ->
                    printDriverVacations(driver)
                }
            } else {
                val driver = deliveryService.driverRepository.findById(driverId)
                if (driver != null) {
                    printDriverVacations(driver)
                } else {
                    println("Fahrer nicht gefunden.")
                }
            }
        } catch (e: Exception) {
            println("Fehler beim Anzeigen der Urlaubsübersicht: ${e.message}")
        }
    }

    private fun printDriverVacations(driver: Driver) {
        println("\nUrlaubsübersicht für ${driver.name}:")
        if (driver.vacationDays.isEmpty()) {
            println("Keine Urlaubstage eingetragen.")
        } else {
            driver.vacationDays.sorted().forEach { date ->
                println("- $date")
            }
        }
    }

    private fun searchOrder() {
        println("Auftrags-ID eingeben:")
        val id = readlnOrNull() ?: return

        try {
            val order = deliveryService.orderRepository.findById(id)
            if (order != null) {
                println("""
                    Auftragsdetails:
                    ID: ${order.id}
                    Status: ${order.status}
                    Lieferdatum: ${order.deliveryDate}
                    Fahrzeug: ${order.assignedVehicle?.model ?: "Nicht zugewiesen"}
                    Fahrer: ${order.assignedDriver?.name ?: "Nicht zugewiesen"}
                    
                    Waren:
                    ${order.cargo.joinToString("\n") {
                    "- ${it.description} (${it.weight}kg)"
                }}
                """.trimIndent())
            } else {
                println("Auftrag nicht gefunden.")
            }
        } catch (e: Exception) {
            println("Fehler bei der Suche: ${e.message}")
        }
    }

    private fun showAllOrders() {
        try {
            val orders = deliveryService.orderRepository.findAll()
            if (orders.isEmpty()) {
                println("Keine Aufträge vorhanden.")
                return
            }

            println("Alle Aufträge:")
            println("ID | Status | Lieferdatum | Fahrzeug | Fahrer")
            println("-".repeat(80))

            orders.forEach { order ->
                println("${order.id} | ${order.status} | ${order.deliveryDate} | " +
                        "${order.assignedVehicle?.model ?: "N/A"} | " +
                        "${order.assignedDriver?.name ?: "N/A"}")
            }
        } catch (e: Exception) {
            println("Fehler beim Anzeigen der Aufträge: ${e.message}")
        }
    }

    private fun updateOrderStatus() {
        println("Auftrags-ID eingeben:")
        val id = readlnOrNull() ?: return

        println("""
        Neuen Status wählen:
        1. CREATED
        2. ASSIGNED
        3. IN_TRANSIT
        4. DELIVERED
        5. CANCELLED
    """.trimIndent())

        val status = when (readlnOrNull()) {
            "1" -> OrderStatus.CREATED
            "2" -> OrderStatus.ASSIGNED
            "3" -> OrderStatus.IN_TRANSIT
            "4" -> OrderStatus.DELIVERED
            "5" -> OrderStatus.CANCELLED
            else -> {
                println("Ungültige Eingabe")
                return
            }
        }

        try {
            val result = deliveryService.updateOrderStatus(id, status)
            if (result.isSuccess) {
                println("Auftragsstatus erfolgreich aktualisiert.")
            } else {
                println("Fehler beim Aktualisieren des Status: ${result.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            println("Fehler beim Aktualisieren des Status: ${e.message}")
        }
    }
}
import repository.InMemoryRepository
import service.FleetService

fun main() {

    val shouldInitDemoData = true

    val repository = InMemoryRepository()
    val fleetService = FleetService(repository)
    val ui = ConsoleUI(fleetService)

    if (shouldInitDemoData) initDemoData(fleetService)

    ui.start()
}

private fun initDemoData(fleetService: FleetService) {

    //TODO: Init demo data

}


package maintenance

import java.time.LocalDate

interface IMaintenanceSchedule {
    fun scheduleNextMaintenance(): LocalDate
    fun isMaintenanceOverdue(): Boolean
}
package cargo

data class Cargo(
    override val id: String,
    override val weight: Double,
    override val description: String,
    val type: CargoType,
    val dimensions: Dimensions,
    val isHazardous: Boolean
): ICargo
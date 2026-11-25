package ie.setu.models

data class Potion(
    var id: Int = 0,
    var name: String,
    var category: String,
    var rarity: String,
    var effect: String,
    var quantity: Int,
    var price: Double,
    var description: String
)
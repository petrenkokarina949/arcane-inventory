package ie.setu.utils

import ie.setu.models.Ingredient
import java.util.Locale
import ie.setu.models.Potion
val categories = arrayOf(
    "Herb",
    "Mineral",
    "Monster Part",
    "Magical Essence",
    "Fungi"
)

val rarities = arrayOf(
    "Common",
    "Uncommon",
    "Rare",
    "Epic",
    "Legendary"
)

val potionCategories = arrayOf(
    "Healing",
    "Mana",
    "Buff",
    "Resistance",
    "Poison"
)

fun isValidName(name: String): Boolean {
    return name.isNotBlank() && name.any { it.isLetter() }
}

fun isValidCategory(category: String): Boolean {
    return category in categories
}

fun isValidRarity(rarity: String): Boolean {
    return rarity in rarities
}

fun isValidQuantity(quantity: Int): Boolean {
    return quantity >= 0
}

fun isValidPrice(price: Double): Boolean {
    return price.isFinite() && price >= 0
}
fun isValidIngredient(ingredient: Ingredient): Boolean {
    return isValidName(ingredient.name) &&
            isValidCategory(ingredient.category) &&
            isValidRarity(ingredient.rarity) &&
            isValidQuantity(ingredient.quantity) &&
            isValidPrice(ingredient.price)
}
fun formatPrice(price: Double): String {
    return String.format(Locale.US, "%.2f", price)
}
fun isValidPotionCategory(category: String): Boolean {
    return category in potionCategories
}

fun isValidEffect(effect: String): Boolean {
    return effect.isNotBlank()
}

fun isValidPotion(potion: Potion): Boolean {
    return isValidName(potion.name) &&
            isValidPotionCategory(potion.category) &&
            isValidRarity(potion.rarity) &&
            isValidEffect(potion.effect) &&
            isValidQuantity(potion.quantity) &&
            isValidPrice(potion.price)
}
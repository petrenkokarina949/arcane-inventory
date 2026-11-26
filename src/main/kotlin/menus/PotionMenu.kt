package ie.setu.menus

import ie.setu.controllers.PotionAPI
import ie.setu.models.Potion
import ie.setu.utils.*

internal val potionAPI = PotionAPI()
fun runPotionMenu() {
    var option: Int

    do {
        println("==============================")
        println("      POTION MANAGEMENT")
        println("==============================")
        println("1. Add Potion")
        println("2. List All Potions")
        println("3. Search Potions")
        println("4. Update Potion")
        println("5. Delete Potion")
        println("6. View Potion Details")
        println()
        println("0. Back to Main Menu")
        println("==============================")

        print("Enter your choice: ")
        option = readln().toIntOrNull() ?: -1

        when (option) {
            1 -> addPotion()
            2 -> listPotions()
            3 -> searchPotions()
            4 -> updatePotion()
            5 -> deletePotion()
            6 -> viewPotionDetails()
            0 -> println("Returning to main menu...")
            else -> println("Invalid option. Please try again.")
        }

    } while (option != 0)}
private fun readPotionDetails(): Potion {

    print("Enter potion name: ")
    var name = readln().trim()

    while (!isValidName(name)) {
        print("Invalid name. Try again: ")
        name = readln().trim()
    }

    val category = selectPotionCategory()
    val rarity = selectPotionRarity()

    print("Enter effect: ")
    var effect = readln().trim()

    while (!isValidEffect(effect)) {
        print("Invalid effect. Try again: ")
        effect = readln().trim()
    }

    print("Enter quantity: ")
    var quantity = readln().toIntOrNull()

    while (quantity == null || !isValidQuantity(quantity)) {
        print("Invalid quantity. Try again: ")
        quantity = readln().toIntOrNull()
    }

    print("Enter price: ")
    var price = readln().toDoubleOrNull()

    while (price == null || !isValidPrice(price)) {
        print("Invalid price. Try again: ")
        price = readln().toDoubleOrNull()
    }

    print("Enter description: ")
    val description = readln().trim()

    return Potion(
        name = name,
        category = category,
        rarity = rarity,
        effect = effect,
        quantity = quantity,
        price = price,
        description = description
    )
}
private fun addPotion() {
    println("========== ADD POTION ==========")

    val potion = readPotionDetails()

    if (potionAPI.add(potion)) {
        println("Potion added successfully.")
    } else {
        println("Failed to add potion.")
    }
}
private fun listPotions() {
    println("========== POTIONS ==========")
    println(potionAPI.listAllPotions())
    println("Total potions: ${potionAPI.numberOfPotions()}")
}
private fun updatePotion() {
    println("========== UPDATE POTION ==========")

    if (potionAPI.numberOfPotions() == 0) {
        println("No potions stored.")
        return
    }

    listPotions()

    print("Enter potion ID: ")
    val id = readln().toIntOrNull()

    if (id == null || !potionAPI.isValidId(id)) {
        println("Invalid potion ID.")
        return
    }

    val potion = readPotionDetails()

    if (potionAPI.update(id, potion)) {
        println("Potion updated successfully.")
    } else {
        println("Failed to update potion.")
    }
}
private fun deletePotion() {
    println("========== DELETE POTION ==========")

    if (potionAPI.numberOfPotions() == 0) {
        println("No potions stored.")
        return
    }

    listPotions()

    print("Enter potion ID: ")
    val id = readln().toIntOrNull()

    if (id == null || !potionAPI.isValidId(id)) {
        println("Invalid potion ID.")
        return
    }
    if (recipeAPI.isPotionUsed(id)) {
        println("Cannot delete potion. It is used in a recipe.")
        return
    }
    print("Are you sure you want to delete this potion? (y/n): ")
    val confirmation = readln().trim()

    if (confirmation.equals("y", ignoreCase = true)) {
        if (potionAPI.delete(id)) {
            println("Potion deleted successfully.")
        } else {
            println("Failed to delete potion.")
        }
    } else {
        println("Deletion cancelled.")
    }
}
private fun searchPotions() {
    println("========== SEARCH POTIONS ==========")

    print("Enter potion name: ")
    val searchString = readln().trim()

    if (searchString.isBlank()) {
        println("Search cannot be empty.")
    } else {
        println(potionAPI.searchByName(searchString))
    }
}
private fun viewPotionDetails() {
    println("========== POTION DETAILS ==========")

    if (potionAPI.numberOfPotions() == 0) {
        println("No potions stored.")
        return
    }

    listPotions()

    print("Enter potion ID: ")
    val id = readln().toIntOrNull()

    if (id == null) {
        println("Invalid potion ID.")
        return
    }

    val potion = potionAPI.findPotion(id)

    if (potion != null) {
        println("""
            ID: ${potion.id}
            Name: ${potion.name}
            Category: ${potion.category}
            Rarity: ${potion.rarity}
            Effect: ${potion.effect}
            Quantity: ${potion.quantity}
            Price: ${formatPrice(potion.price)}
            Description: ${potion.description}
        """.trimIndent())
    } else {
        println("Potion not found.")
    }
}
private fun selectPotionCategory(): String {
    println("Select potion category:")

    for (i in potionCategories.indices) {
        println("${i + 1}. ${potionCategories[i]}")
    }

    print("Enter category number: ")
    var choice = readln().toIntOrNull()

    while (choice == null || choice !in 1..potionCategories.size) {
        print("Invalid category. Try again: ")
        choice = readln().toIntOrNull()
    }

    return potionCategories[choice - 1]
}
private fun selectPotionRarity(): String {
    println("Select rarity:")

    for (i in rarities.indices) {
        println("${i + 1}. ${rarities[i]}")
    }

    print("Enter rarity number: ")
    var choice = readln().toIntOrNull()

    while (choice == null || choice !in 1..rarities.size) {
        print("Invalid rarity. Try again: ")
        choice = readln().toIntOrNull()
    }

    return rarities[choice - 1]
}
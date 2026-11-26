package ie.setu.menus

import ie.setu.controllers.PotionAPI
import ie.setu.models.Potion
import ie.setu.utils.*

private val potionAPI = PotionAPI()
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
            3 -> println("Search Potions")
            4 -> updatePotion()
            5 -> deletePotion()
            6 -> println("View Potion Details")
            0 -> println("Returning to main menu...")
            else -> println("Invalid option. Please try again.")
        }

    } while (option != 0)}
    private fun readPotionDetails(): Potion {
        print("Enter potion name: ")
        val name = readln().trim()

        print("Enter category: ")
        val category = readln().trim()

        print("Enter rarity: ")
        val rarity = readln().trim()

        print("Enter effect: ")
        val effect = readln().trim()

        print("Enter quantity: ")
        val quantity = readln().toIntOrNull() ?: 0

        print("Enter price: ")
        val price = readln().toDoubleOrNull() ?: 0.0

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
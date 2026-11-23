package ie.setu.menus

import ie.setu.controllers.IngredientAPI
import ie.setu.models.Ingredient
import ie.setu.utils.*

private val ingredientAPI = IngredientAPI()

fun runIngredientMenu() {
    var option: Int

    do {
        println("""
            ==============================
                INGREDIENT MANAGEMENT
            ==============================
            1. Add Ingredient
            2. List All Ingredients
            3. Search Ingredients
            4. Update Ingredient
            5. Delete Ingredient
            6. View Ingredient Details

            0. Back to Main Menu
            ==============================
        """.trimIndent())

        print("Enter your choice: ")
        option = readln().toIntOrNull() ?: -1

        when (option) {
            1 -> addIngredient()
            2 -> listIngredients()
            3 -> searchIngredients()
            4 -> updateIngredient()
            5 -> deleteIngredient()
            6 -> viewIngredientDetails()
            0 -> println("Returning to main menu...")
            else -> println("Invalid option. Please try again.")
        }
    } while (option != 0)
}
private fun selectCategory(): String {
    println("Choose ingredient category:")

    for (i in categories.indices) {
        println("${i + 1}. ${categories[i]}")
    }

    print("Enter category number: ")
    var choice = readln().toIntOrNull()

    while (choice == null || choice !in 1..categories.size) {
        println("Invalid category. Please try again.")
        print("Enter category number: ")
        choice = readln().toIntOrNull()
    }

    return categories[choice - 1]
}

private fun selectRarity(): String {
    println("Choose ingredient rarity:")

    for (i in rarities.indices) {
        println("${i + 1}. ${rarities[i]}")
    }

    print("Enter rarity number: ")
    var choice = readln().toIntOrNull()

    while (choice == null || choice !in 1..rarities.size) {
        println("Invalid rarity. Please try again.")
        print("Enter rarity number: ")
        choice = readln().toIntOrNull()
    }

    return rarities[choice - 1]
}

private fun readIngredientDetails(): Ingredient {

    print("Enter ingredient name: ")
    var name = readln().trim()

    while (!isValidName(name)) {
        println("Invalid name. Name must contain at least one letter.")
        print("Enter ingredient name: ")
        name = readln().trim()
    }

    val category = selectCategory()
    val rarity = selectRarity()

    print("Enter quantity: ")
    var quantity = readln().toIntOrNull()

    while (quantity == null || !isValidQuantity(quantity)) {
        println("Invalid quantity. Enter a non-negative whole number.")
        print("Enter quantity: ")
        quantity = readln().toIntOrNull()
    }

    print("Enter price: ")
    var price = readln().toDoubleOrNull()

    while (price == null || !isValidPrice(price)) {
        println("Invalid price. Enter a non-negative number.")
        print("Enter price: ")
        price = readln().toDoubleOrNull()
    }

    print("Enter description: ")
    val description = readln().trim()

    return Ingredient(
        name = name,
        category = category,
        rarity = rarity,
        quantity = quantity,
        price = price,
        description = description
    )
}
private fun addIngredient() {
    println("========== ADD INGREDIENT ==========")

    val ingredient = readIngredientDetails()

    if (ingredientAPI.add(ingredient)) {
        println("Ingredient added successfully.")
    } else {
        println("Invalid ingredient details.")
    }
}
private fun listIngredients() {
    println("========== INGREDIENTS ==========")

    println(ingredientAPI.listAllIngredients())

    println("Total ingredients: ${ingredientAPI.numberOfIngredients()}")
}
private fun searchIngredients() {
    println("========== SEARCH INGREDIENTS ==========")

    print("Enter ingredient name: ")
    val searchString = readln().trim()

    if (searchString.isBlank()) {
        println("Search text cannot be empty.")
    } else {
        println(ingredientAPI.searchByName(searchString))
    }
}
private fun updateIngredient() {
    println("========== UPDATE INGREDIENT ==========")

    if (ingredientAPI.numberOfIngredients() == 0) {
        println("No ingredients available to update.")
        return
    }

    listIngredients()

    print("Enter ingredient ID: ")
    val id = readln().toIntOrNull()

    if (id == null || !ingredientAPI.isValidId(id)) {
        println("Invalid ingredient ID.")
        return
    }

    val ingredient = readIngredientDetails()

    if (ingredientAPI.update(id, ingredient)) {
        println("Ingredient updated successfully.")
    } else {
        println("Invalid ingredient details.")
    }
}
private fun deleteIngredient() {
    println("========== DELETE INGREDIENT ==========")

    if (ingredientAPI.numberOfIngredients() == 0) {
        println("No ingredients available to delete.")
        return
    }

    listIngredients()

    print("Enter ingredient ID: ")
    val id = readln().toIntOrNull()

    if (id == null || !ingredientAPI.isValidId(id)) {
        println("Invalid ingredient ID.")
        return
    }

    print("Are you sure you want to delete this ingredient? (y/n): ")
    val confirmation = readln().trim()

    if (confirmation.equals("y", ignoreCase = true)) {
        if (ingredientAPI.delete(id)) {
            println("Ingredient deleted successfully.")
        } else {
            println("Unable to delete ingredient.")
        }
    } else {
        println("Deletion cancelled.")
    }
}
private fun viewIngredientDetails() {
    println("========== INGREDIENT DETAILS ==========")

    if (ingredientAPI.numberOfIngredients() == 0) {
        println("No ingredients available.")
        return
    }

    listIngredients()

    print("Enter ingredient ID: ")
    val id = readln().toIntOrNull()

    if (id == null) {
        println("Invalid ingredient ID.")
        return
    }

    val ingredient = ingredientAPI.findIngredient(id)

    if (ingredient != null) {
        println("""
            ==============================
            ID: ${ingredient.id}
            Name: ${ingredient.name}
            Category: ${ingredient.category}
            Rarity: ${ingredient.rarity}
            Quantity: ${ingredient.quantity}
            Price: ${formatPrice(ingredient.price)}
            Description: ${ingredient.description}
            ==============================
        """.trimIndent())
    } else {
        println("Ingredient not found.")
    }
}
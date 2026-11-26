package ie.setu.menus

import ie.setu.services.CraftingService

private val craftingService = CraftingService(
    ingredientAPI,
    potionAPI,
    recipeAPI
)

fun runInventoryMenu() {
    var option: Int?

    do {
        println("==============================")
        println("    INVENTORY AND CRAFTING")
        println("==============================")
        println("1. View Ingredient Inventory")
        println("2. View Potion Inventory")
        println("3. View Available Recipes")
        println("4. Craft Potion")
        println()
        println("0. Back to Main Menu")
        println("==============================")

        print("Enter your choice: ")
        option = readln().toIntOrNull()

        when (option) {
            1 -> viewIngredientInventory()
            2 -> viewPotionInventory()
            3 -> viewAvailableRecipes()
            4 -> craftPotion()
            0 -> println("Returning to main menu...")
            else -> println("Invalid option.")
        }

    } while (option != 0)
}

private fun viewIngredientInventory() {
    println("========== INGREDIENT INVENTORY ==========")
    println(craftingService.listAvailableIngredients())
}

private fun viewPotionInventory() {
    println("========== POTION INVENTORY ==========")
    println(craftingService.listAvailablePotions())
}

private fun viewAvailableRecipes() {
    println("========== AVAILABLE RECIPES ==========")
    println(craftingService.listAvailableRecipes())
}

private fun craftPotion() {
    println("========== CRAFT POTION ==========")

    if (recipeAPI.numberOfRecipes() == 0) {
        println("No recipes available.")
        return
    }

    viewAvailableRecipes()

    print("Enter recipe ID: ")
    val recipeId = readln().toIntOrNull()

    if (recipeId == null ||
        craftingService.findRecipe(recipeId) == null) {
        println("Invalid recipe ID.")
        return
    }

    if (craftingService.craftPotion(recipeId)) {
        println("Potion crafted successfully.")
    } else {
        println("Unable to craft potion.")
    }
}
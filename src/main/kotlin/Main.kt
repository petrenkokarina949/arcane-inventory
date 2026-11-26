package ie.setu
import ie.setu.menus.runIngredientMenu
import ie.setu.menus.runPotionMenu
import ie.setu.menus.runRecipeMenu
import ie.setu.menus.runInventoryMenu

fun main() {
    var option: Int

    do {
        mainMenu()

        print("Enter your choice: ")
        option = readln().toIntOrNull() ?: -1

        when (option) {
            1 -> runIngredientMenu()
            2 -> runPotionMenu()
            3 -> runRecipeMenu()
            4 -> runInventoryMenu()
            0 -> println("Goodbye!")
       else -> println("Invalid option. Please try again.")
        }
    } while (option != 0)
}

fun mainMenu() {
    println("""
        ==============================
              ARCANE INVENTORY
        ==============================
        1. Manage Ingredients
        2. Manage Potions
        3. Manage Recipes
        4. Inventory and Crafting

        0. Exit
        ==============================
    """.trimIndent())
}
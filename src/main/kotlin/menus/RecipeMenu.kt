package ie.setu.menus

import ie.setu.controllers.RecipeAPI
import ie.setu.models.Recipe

internal val recipeAPI = RecipeAPI()

fun runRecipeMenu() {
    var option: Int

    do {
        println("""
            ==============================
                 RECIPE MANAGEMENT
            ==============================
            1. Add Recipe
            2. List All Recipes
            3. Search Recipes
            4. Update Recipe
            5. Delete Recipe
            6. View Recipe Details

            7. Add Ingredient to Recipe
            8. Update Ingredient Quantity
            9. Remove Ingredient from Recipe

            0. Back to Main Menu
            ==============================
        """.trimIndent())

        print("Enter your choice: ")
        option = readln().toIntOrNull() ?: -1

        when (option) {
            1 -> addRecipe()
            2 -> listRecipes()
            3 -> searchRecipes()
            4 -> updateRecipe()
            5 -> deleteRecipe()
            6 -> viewRecipeDetails()
            7 -> addIngredientToRecipe()
            8 -> updateRecipeIngredient()
            9 -> removeIngredientFromRecipe()
            0 -> println("Returning to main menu...")
            else -> println("Invalid option. Please try again.")
        }

    } while (option != 0)
}

private fun readRecipeDetails(): Recipe? {

    if (potionAPI.numberOfPotions() == 0) {
        println("Add a potion before creating a recipe.")
        return null
    }

    print("Enter recipe name: ")
    val name = readln().trim()

    println("Available potions:")
    println(potionAPI.listAllPotions())

    print("Enter potion ID: ")
    val potionId = readln().toIntOrNull()

    if (potionId == null || !potionAPI.isValidId(potionId)) {
        println("Invalid potion ID.")
        return null
    }

    print("Enter recipe description: ")
    val description = readln().trim()

    return Recipe(
        name = name,
        potionId = potionId,
        description = description
    )
}

private fun addRecipe() {
    println("========== ADD RECIPE ==========")

    val recipe = readRecipeDetails() ?: return

    if (recipeAPI.add(recipe)) {
        println("Recipe added successfully.")
    } else {
        println("Failed to add recipe.")
    }
}

private fun listRecipes() {
    println("========== RECIPES ==========")

    println(recipeAPI.listAllRecipes())
    println("Total recipes: ${recipeAPI.numberOfRecipes()}")
}

private fun searchRecipes() {
    println("========== SEARCH RECIPES ==========")

    print("Enter recipe name: ")
    val searchString = readln().trim()

    if (searchString.isBlank()) {
        println("Search cannot be empty.")
    } else {
        println(recipeAPI.searchByName(searchString))
    }
}

private fun selectRecipe(): Recipe? {

    if (recipeAPI.numberOfRecipes() == 0) {
        println("No recipes stored.")
        return null
    }

    listRecipes()

    print("Enter recipe ID: ")
    val id = readln().toIntOrNull()

    if (id == null || !recipeAPI.isValidId(id)) {
        println("Invalid recipe ID.")
        return null
    }

    return recipeAPI.findRecipe(id)
}

private fun updateRecipe() {
    println("========== UPDATE RECIPE ==========")

    val recipe = selectRecipe() ?: return
    val updatedRecipe = readRecipeDetails() ?: return

    if (recipeAPI.update(recipe.id, updatedRecipe)) {
        println("Recipe updated successfully.")
    } else {
        println("Failed to update recipe.")
    }
}

private fun deleteRecipe() {
    println("========== DELETE RECIPE ==========")

    val recipe = selectRecipe() ?: return

    print("Are you sure you want to delete this recipe? (y/n): ")
    val confirmation = readln().trim()

    if (confirmation.equals("y", ignoreCase = true)) {
        if (recipeAPI.delete(recipe.id)) {
            println("Recipe deleted successfully.")
        } else {
            println("Failed to delete recipe.")
        }
    } else {
        println("Deletion cancelled.")
    }
}

private fun viewRecipeDetails() {
    println("========== RECIPE DETAILS ==========")

    val recipe = selectRecipe() ?: return

    val potion = potionAPI.findPotion(recipe.potionId)

    println("""
        ==============================
        ID: ${recipe.id}
        Name: ${recipe.name}
        Potion: ${potion?.name ?: "Potion not found"}
        Description: ${recipe.description}
        ==============================
    """.trimIndent())

    printRecipeIngredients(recipe)
}

private fun printRecipeIngredients(recipe: Recipe) {

    println("Required ingredients:")

    if (recipe.ingredients.isEmpty()) {
        println("No ingredients added.")
    } else {
        for (recipeIngredient in recipe.ingredients) {

            val ingredient = ingredientAPI.findIngredient(
                recipeIngredient.ingredientId
            )

            println(
                "ID: ${recipeIngredient.ingredientId} | " +
                        "${ingredient?.name ?: "Ingredient not found"} | " +
                        "Required quantity: ${recipeIngredient.quantity}"
            )
        }
    }
}

private fun addIngredientToRecipe() {
    println("========== ADD RECIPE INGREDIENT ==========")

    val recipe = selectRecipe() ?: return

    if (ingredientAPI.numberOfIngredients() == 0) {
        println("No ingredients available.")
        return
    }

    println(ingredientAPI.listAllIngredients())

    print("Enter ingredient ID: ")
    val ingredientId = readln().toIntOrNull()

    if (ingredientId == null || !ingredientAPI.isValidId(ingredientId)) {
        println("Invalid ingredient ID.")
        return
    }

    print("Enter required quantity: ")
    val quantity = readln().toIntOrNull()

    if (quantity == null || quantity <= 0) {
        println("Invalid quantity.")
        return
    }

    if (recipeAPI.addIngredient(recipe.id, ingredientId, quantity)) {
        println("Ingredient added to recipe successfully.")
    } else {
        println("Ingredient could not be added. It may already exist.")
    }
}

private fun updateRecipeIngredient() {
    println("========== UPDATE RECIPE INGREDIENT ==========")

    val recipe = selectRecipe() ?: return

    if (recipe.ingredients.isEmpty()) {
        println("No ingredients in this recipe.")
        return
    }

    printRecipeIngredients(recipe)

    print("Enter ingredient ID: ")
    val ingredientId = readln().toIntOrNull()

    if (ingredientId == null) {
        println("Invalid ingredient ID.")
        return
    }

    print("Enter new required quantity: ")
    val quantity = readln().toIntOrNull()

    if (quantity == null || quantity <= 0) {
        println("Invalid quantity.")
        return
    }

    if (recipeAPI.updateIngredientQuantity(
            recipe.id, ingredientId, quantity
        )) {
        println("Ingredient quantity updated successfully.")
    } else {
        println("Ingredient not found in recipe.")
    }
}

private fun removeIngredientFromRecipe() {
    println("========== REMOVE RECIPE INGREDIENT ==========")

    val recipe = selectRecipe() ?: return

    if (recipe.ingredients.isEmpty()) {
        println("No ingredients in this recipe.")
        return
    }

    printRecipeIngredients(recipe)

    print("Enter ingredient ID: ")
    val ingredientId = readln().toIntOrNull()

    if (ingredientId == null) {
        println("Invalid ingredient ID.")
        return
    }

    print("Remove this ingredient from the recipe? (y/n): ")
    val confirmation = readln().trim()

    if (confirmation.equals("y", ignoreCase = true)) {
        if (recipeAPI.removeIngredient(recipe.id, ingredientId)) {
            println("Ingredient removed from recipe successfully.")
        } else {
            println("Ingredient not found in recipe.")
        }
    } else {
        println("Removal cancelled.")
    }
}
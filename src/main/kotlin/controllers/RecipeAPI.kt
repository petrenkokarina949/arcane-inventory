package ie.setu.controllers

import ie.setu.models.Recipe

class RecipeAPI {

    private var recipes = ArrayList<Recipe>()
    private var lastId = 0

    private fun getId(): Int {
        return lastId++
    }

    fun add(recipe: Recipe): Boolean {
        recipe.id = getId()
        return recipes.add(recipe)
    }

    fun numberOfRecipes(): Int {
        return recipes.size
    }

    fun listAllRecipes(): String {
        return if (recipes.isEmpty()) {
            "No recipes stored"
        } else {
            recipes.joinToString("\n") { recipe ->
                "${recipe.id}: ${recipe.name} | " +
                        "Potion ID: ${recipe.potionId} | " +
                        "Ingredients: ${recipe.ingredients.size}"
            }
        }
    }

    fun findRecipe(id: Int): Recipe? {
        return recipes.find { recipe -> recipe.id == id }
    }

    fun isValidId(id: Int): Boolean {
        return recipes.any { recipe -> recipe.id == id }
    }

    fun searchByName(searchString: String): String {
        return recipes.filter { recipe ->
            recipe.name.contains(searchString, ignoreCase = true)
        }.joinToString("\n") { recipe ->
            "${recipe.id}: ${recipe.name} | " +
                    "Potion ID: ${recipe.potionId} | " +
                    "Ingredients: ${recipe.ingredients.size}"
        }.ifEmpty { "No matching recipes found" }
    }
}
package ie.setu.controllers

import ie.setu.models.Recipe
import ie.setu.models.RecipeIngredient

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
    fun addIngredient(recipeId: Int, ingredientId: Int, quantity: Int): Boolean {
        val recipe = findRecipe(recipeId)

        if (recipe == null || quantity <= 0) {
            return false
        }

        if (recipe.ingredients.any { ingredient ->
                ingredient.ingredientId == ingredientId
            }) {
            return false
        }

        return recipe.ingredients.add(
            RecipeIngredient(ingredientId, quantity)
        )
    }
    fun updateIngredientQuantity(
        recipeId: Int,
        ingredientId: Int,
        quantity: Int
    ): Boolean {
        val recipe = findRecipe(recipeId)

        if (recipe == null || quantity <= 0) {
            return false
        }

        val foundIngredient = recipe.ingredients.find { ingredient ->
            ingredient.ingredientId == ingredientId
        }

        if (foundIngredient != null) {
            foundIngredient.quantity = quantity
            return true
        }

        return false
    }
    fun removeIngredient(recipeId: Int, ingredientId: Int): Boolean {
        val recipe = findRecipe(recipeId)

        if (recipe == null) {
            return false
        }

        return recipe.ingredients.removeIf { ingredient ->
            ingredient.ingredientId == ingredientId
        }
    }
}
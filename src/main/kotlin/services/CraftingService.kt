package ie.setu.services

import ie.setu.controllers.IngredientAPI
import ie.setu.controllers.PotionAPI
import ie.setu.controllers.RecipeAPI
import ie.setu.models.Recipe

class CraftingService(
    private val ingredientAPI: IngredientAPI,
    private val potionAPI: PotionAPI,
    private val recipeAPI: RecipeAPI
) {

    fun findRecipe(recipeId: Int): Recipe? {
        return recipeAPI.findRecipe(recipeId)
    }

    fun listAvailableIngredients(): String {
        return ingredientAPI.listAllIngredients()
    }

    fun listAvailablePotions(): String {
        return potionAPI.listAllPotions()
    }

    fun listAvailableRecipes(): String {
        return recipeAPI.listAllRecipes()
    }
    fun canCraft(recipeId: Int): Boolean {
        return getCraftingError(recipeId).isEmpty()
    }
    fun getCraftingError(recipeId: Int): String {
        val recipe = recipeAPI.findRecipe(recipeId)

        if (recipe == null) {
            return "Recipe not found."
        }

        if (recipe.ingredients.isEmpty()) {
            return "Recipe has no ingredients."
        }

        val potion = potionAPI.findPotion(recipe.potionId)

        if (potion == null) {
            return "Potion not found."
        }

        for (requiredIngredient in recipe.ingredients) {
            val ingredient = ingredientAPI.findIngredient(
                requiredIngredient.ingredientId
            )

            if (ingredient == null) {
                return "Required ingredient not found."
            }

            if (requiredIngredient.quantity <= 0) {
                return "Invalid required quantity."
            }

            if (ingredient.quantity < requiredIngredient.quantity) {
                return "Not enough ${ingredient.name}. " +
                        "Required: ${requiredIngredient.quantity}, " +
                        "Available: ${ingredient.quantity}."
            }
        }

        return ""
    }
    fun craftPotion(recipeId: Int): Boolean {
        if (!canCraft(recipeId)) {
            return false
        }

        val recipe = recipeAPI.findRecipe(recipeId)
            ?: return false

        val potion = potionAPI.findPotion(recipe.potionId)
            ?: return false

        for (requiredIngredient in recipe.ingredients) {
            val ingredient = ingredientAPI.findIngredient(
                requiredIngredient.ingredientId
            ) ?: return false

            ingredient.quantity -= requiredIngredient.quantity
        }

        potion.quantity++

        return true
    }
}
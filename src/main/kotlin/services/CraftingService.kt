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
        val recipe = recipeAPI.findRecipe(recipeId)

        if (recipe == null || recipe.ingredients.isEmpty()) {
            return false
        }

        if (!potionAPI.isValidId(recipe.potionId)) {
            return false
        }

        return recipe.ingredients.all { requiredIngredient ->
            val ingredient = ingredientAPI.findIngredient(
                requiredIngredient.ingredientId
            )

            ingredient != null &&
                    requiredIngredient.quantity > 0 &&
                    ingredient.quantity >= requiredIngredient.quantity
        }
    }
}
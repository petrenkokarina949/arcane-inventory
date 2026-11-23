package ie.setu.controllers

import ie.setu.models.Ingredient
import ie.setu.utils.isValidIngredient
import ie.setu.utils.formatPrice
class IngredientAPI {

    private var ingredients = ArrayList<Ingredient>()
    private var lastId = 0

    private fun getId(): Int {
        return lastId++
    }

    fun add(ingredient: Ingredient): Boolean {
        if (!isValidIngredient(ingredient)) {
            return false
        }

        ingredient.id = getId()
        return ingredients.add(ingredient)
    }
    fun numberOfIngredients(): Int {
        return ingredients.size
    }
    fun listAllIngredients(): String {
        return if (ingredients.isEmpty()) {
            "No ingredients stored"
        } else {
            ingredients.joinToString("\n") { ingredient ->
                "${ingredient.id}: ${ingredient.name} | " +
                        "${ingredient.category} | ${ingredient.rarity} | " +
                        "Quantity: ${ingredient.quantity} | " +
                        "Price: ${formatPrice(ingredient.price)}"
            }
        }
    }
    fun findIngredient(id: Int): Ingredient? {
        return ingredients.find { ingredient -> ingredient.id == id }
    }
    fun isValidId(id: Int): Boolean {
        return ingredients.any { ingredient -> ingredient.id == id }
    }
    fun searchByName(searchString: String): String {
        return ingredients.filter { ingredient ->
            ingredient.name.contains(searchString, ignoreCase = true)
        }.joinToString("\n") { ingredient ->
            "${ingredient.id}: ${ingredient.name} | " +
                    "${ingredient.category} | ${ingredient.rarity} | " +
                    "Quantity: ${ingredient.quantity} | " +
                    "Price: ${formatPrice(ingredient.price)}"
        }.ifEmpty { "No matching ingredients found" }
    }
    fun update(id: Int, ingredient: Ingredient): Boolean {
        if (!isValidIngredient(ingredient)) {
            return false
        }

        val foundIngredient = findIngredient(id)

        if (foundIngredient != null) {
            foundIngredient.name = ingredient.name
            foundIngredient.category = ingredient.category
            foundIngredient.rarity = ingredient.rarity
            foundIngredient.quantity = ingredient.quantity
            foundIngredient.price = ingredient.price
            foundIngredient.description = ingredient.description

            return true
        }
        return false
    }
    fun delete(id: Int): Boolean {
        return ingredients.removeIf { ingredient ->
            ingredient.id == id
        }
    }
}
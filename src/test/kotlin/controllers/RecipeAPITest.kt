package controllers

import ie.setu.controllers.IngredientAPI
import ie.setu.controllers.PotionAPI
import ie.setu.controllers.RecipeAPI
import ie.setu.models.Ingredient
import ie.setu.models.Potion
import ie.setu.models.Recipe
import ie.setu.models.RecipeIngredient
import kotlin.test.*

class RecipeAPITest {

    private lateinit var ingredientAPI: IngredientAPI
    private lateinit var potionAPI: PotionAPI
    private lateinit var recipeAPI: RecipeAPI

    private lateinit var healingRecipe: Recipe
    private lateinit var manaRecipe: Recipe

    @BeforeTest
    fun setup() {
        ingredientAPI = IngredientAPI()
        potionAPI = PotionAPI()
        recipeAPI = RecipeAPI(ingredientAPI, potionAPI)

        ingredientAPI.add(
            Ingredient(
                name = "Moon Herb",
                category = "Herb",
                rarity = "Common",
                quantity = 10,
                price = 5.50,
                description = "Magical herb"
            )
        )

        ingredientAPI.add(
            Ingredient(
                name = "Magic Crystal",
                category = "Mineral",
                rarity = "Rare",
                quantity = 5,
                price = 25.00,
                description = "Magical crystal"
            )
        )

        potionAPI.add(
            Potion(
                name = "Healing Potion",
                category = "Healing",
                rarity = "Common",
                effect = "Restores health",
                quantity = 0,
                price = 30.00,
                description = "Healing potion"
            )
        )

        potionAPI.add(
            Potion(
                name = "Mana Potion",
                category = "Mana",
                rarity = "Rare",
                effect = "Restores mana",
                quantity = 0,
                price = 40.00,
                description = "Mana potion"
            )
        )

        healingRecipe = Recipe(
            name = "Healing Recipe",
            potionId = 0,
            description = "Restores health"
        )

        manaRecipe = Recipe(
            name = "Mana Recipe",
            potionId = 1,
            description = "Restores mana"
        )
    }

    @Test
    fun addValidRecipe() {
        assertTrue(recipeAPI.add(healingRecipe))
        assertEquals(1, recipeAPI.numberOfRecipes())
    }

    @Test
    fun addRecipeWithInvalidName() {
        healingRecipe.name = ""

        assertFalse(recipeAPI.add(healingRecipe))
        assertEquals(0, recipeAPI.numberOfRecipes())
    }

    @Test
    fun addRecipeWithInvalidPotionId() {
        healingRecipe.potionId = 999

        assertFalse(recipeAPI.add(healingRecipe))
        assertEquals(0, recipeAPI.numberOfRecipes())
    }

    @Test
    fun addRecipeWithInvalidIngredientId() {
        healingRecipe.ingredients.add(
            RecipeIngredient(999, 2)
        )

        assertFalse(recipeAPI.add(healingRecipe))
    }

    @Test
    fun addRecipeWithInvalidIngredientQuantity() {
        healingRecipe.ingredients.add(
            RecipeIngredient(0, 0)
        )

        assertFalse(recipeAPI.add(healingRecipe))
    }

    @Test
    fun addRecipeWithDuplicateIngredients() {
        healingRecipe.ingredients.add(
            RecipeIngredient(0, 2)
        )
        healingRecipe.ingredients.add(
            RecipeIngredient(0, 3)
        )

        assertFalse(recipeAPI.add(healingRecipe))
    }

    @Test
    fun recipesReceiveUniqueIds() {
        recipeAPI.add(healingRecipe)
        recipeAPI.add(manaRecipe)

        assertEquals(0, healingRecipe.id)
        assertEquals(1, manaRecipe.id)
    }

    @Test
    fun listRecipesWhenEmpty() {
        assertEquals(
            "No recipes stored",
            recipeAPI.listAllRecipes()
        )
    }

    @Test
    fun listAllRecipes() {
        recipeAPI.add(healingRecipe)
        recipeAPI.add(manaRecipe)

        val result = recipeAPI.listAllRecipes()

        assertTrue(result.contains("Healing Recipe"))
        assertTrue(result.contains("Mana Recipe"))
    }

    @Test
    fun numberOfRecipes() {
        assertEquals(0, recipeAPI.numberOfRecipes())

        recipeAPI.add(healingRecipe)
        recipeAPI.add(manaRecipe)

        assertEquals(2, recipeAPI.numberOfRecipes())
    }

    @Test
    fun findExistingRecipe() {
        recipeAPI.add(healingRecipe)

        assertEquals(
            healingRecipe,
            recipeAPI.findRecipe(0)
        )
        assertTrue(recipeAPI.isValidId(0))
    }

    @Test
    fun findNonExistingRecipe() {
        assertNull(recipeAPI.findRecipe(999))
        assertFalse(recipeAPI.isValidId(999))
    }

    @Test
    fun searchRecipeByPartialName() {
        recipeAPI.add(healingRecipe)
        recipeAPI.add(manaRecipe)

        val result = recipeAPI.searchByName("Heal")

        assertTrue(result.contains("Healing Recipe"))
        assertFalse(result.contains("Mana Recipe"))
    }

    @Test
    fun searchRecipeIgnoringCase() {
        recipeAPI.add(healingRecipe)

        val result = recipeAPI.searchByName("HEALING")

        assertTrue(result.contains("Healing Recipe"))
    }

    @Test
    fun searchRecipeWithNoResults() {
        recipeAPI.add(healingRecipe)

        assertEquals(
            "No matching recipes found",
            recipeAPI.searchByName("Poison")
        )
    }

    @Test
    fun updateExistingRecipe() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        val updatedRecipe = Recipe(
            name = "Advanced Healing Recipe",
            potionId = 1,
            description = "Updated description"
        )

        assertTrue(recipeAPI.update(0, updatedRecipe))

        val recipe = recipeAPI.findRecipe(0)!!

        assertEquals("Advanced Healing Recipe", recipe.name)
        assertEquals(1, recipe.potionId)
        assertEquals("Updated description", recipe.description)
        assertEquals(1, recipe.ingredients.size)
        assertEquals(3, recipe.ingredients[0].quantity)
    }

    @Test
    fun updateNonExistingRecipe() {
        assertFalse(recipeAPI.update(999, healingRecipe))
    }

    @Test
    fun updateRecipeWithInvalidData() {
        recipeAPI.add(healingRecipe)

        val invalidRecipe = Recipe(
            name = "",
            potionId = 0,
            description = "Invalid recipe"
        )

        assertFalse(recipeAPI.update(0, invalidRecipe))
        assertEquals(
            "Healing Recipe",
            recipeAPI.findRecipe(0)?.name
        )
    }

    @Test
    fun deleteExistingRecipe() {
        recipeAPI.add(healingRecipe)

        assertTrue(recipeAPI.delete(0))
        assertEquals(0, recipeAPI.numberOfRecipes())
        assertNull(recipeAPI.findRecipe(0))
    }

    @Test
    fun deleteNonExistingRecipe() {
        assertFalse(recipeAPI.delete(999))
    }

    @Test
    fun addIngredientToRecipe() {
        recipeAPI.add(healingRecipe)

        assertTrue(recipeAPI.addIngredient(0, 0, 3))
        assertEquals(1, healingRecipe.ingredients.size)
        assertEquals(0, healingRecipe.ingredients[0].ingredientId)
        assertEquals(3, healingRecipe.ingredients[0].quantity)
    }

    @Test
    fun addDuplicateIngredientToRecipe() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        assertFalse(recipeAPI.addIngredient(0, 0, 5))
        assertEquals(1, healingRecipe.ingredients.size)
        assertEquals(3, healingRecipe.ingredients[0].quantity)
    }

    @Test
    fun addNonExistingIngredientToRecipe() {
        recipeAPI.add(healingRecipe)

        assertFalse(recipeAPI.addIngredient(0, 999, 3))
        assertTrue(healingRecipe.ingredients.isEmpty())
    }

    @Test
    fun addIngredientWithInvalidQuantity() {
        recipeAPI.add(healingRecipe)

        assertFalse(recipeAPI.addIngredient(0, 0, 0))
        assertFalse(recipeAPI.addIngredient(0, 0, -5))
        assertTrue(healingRecipe.ingredients.isEmpty())
    }

    @Test
    fun addIngredientToNonExistingRecipe() {
        assertFalse(recipeAPI.addIngredient(999, 0, 3))
    }

    @Test
    fun updateIngredientQuantity() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        assertTrue(
            recipeAPI.updateIngredientQuantity(0, 0, 5)
        )

        assertEquals(5, healingRecipe.ingredients[0].quantity)
    }

    @Test
    fun updateIngredientWithInvalidQuantity() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        assertFalse(
            recipeAPI.updateIngredientQuantity(0, 0, -5)
        )

        assertEquals(3, healingRecipe.ingredients[0].quantity)
    }

    @Test
    fun updateNonExistingRecipeIngredient() {
        recipeAPI.add(healingRecipe)

        assertFalse(
            recipeAPI.updateIngredientQuantity(0, 999, 5)
        )
    }

    @Test
    fun removeIngredientFromRecipe() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        assertTrue(recipeAPI.removeIngredient(0, 0))
        assertTrue(healingRecipe.ingredients.isEmpty())
    }

    @Test
    fun removeNonExistingRecipeIngredient() {
        recipeAPI.add(healingRecipe)

        assertFalse(recipeAPI.removeIngredient(0, 999))
    }

    @Test
    fun checkIfIngredientIsUsed() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        assertTrue(recipeAPI.isIngredientUsed(0))
        assertFalse(recipeAPI.isIngredientUsed(1))
    }

    @Test
    fun checkIfPotionIsUsed() {
        recipeAPI.add(healingRecipe)

        assertTrue(recipeAPI.isPotionUsed(0))
        assertFalse(recipeAPI.isPotionUsed(1))
    }

    @Test
    fun referencesAreReleasedAfterRecipeDeletion() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)

        recipeAPI.delete(0)

        assertFalse(recipeAPI.isIngredientUsed(0))
        assertFalse(recipeAPI.isPotionUsed(0))
    }

    @Test
    fun recipeDoesNotChangeInventoryQuantity() {
        recipeAPI.add(healingRecipe)
        recipeAPI.addIngredient(0, 0, 3)
        recipeAPI.updateIngredientQuantity(0, 0, 5)
        recipeAPI.removeIngredient(0, 0)

        assertEquals(
            10,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            0,
            potionAPI.findPotion(0)?.quantity
        )
    }
}
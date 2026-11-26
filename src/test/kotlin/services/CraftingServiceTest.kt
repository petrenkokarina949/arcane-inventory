package services

import ie.setu.controllers.IngredientAPI
import ie.setu.controllers.PotionAPI
import ie.setu.controllers.RecipeAPI
import ie.setu.models.Ingredient
import ie.setu.models.Potion
import ie.setu.models.Recipe
import ie.setu.services.CraftingService
import kotlin.test.*

class CraftingServiceTest {

    private lateinit var ingredientAPI: IngredientAPI
    private lateinit var potionAPI: PotionAPI
    private lateinit var recipeAPI: RecipeAPI
    private lateinit var craftingService: CraftingService

    @BeforeTest
    fun setup() {
        ingredientAPI = IngredientAPI()
        potionAPI = PotionAPI()
        recipeAPI = RecipeAPI(ingredientAPI, potionAPI)

        craftingService = CraftingService(
            ingredientAPI,
            potionAPI,
            recipeAPI
        )

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

        recipeAPI.add(
            Recipe(
                name = "Healing Recipe",
                potionId = 0,
                description = "Restores health"
            )
        )

        recipeAPI.addIngredient(0, 0, 3)
        recipeAPI.addIngredient(0, 1, 2)
    }

    @Test
    fun findExistingRecipe() {
        assertNotNull(craftingService.findRecipe(0))
    }

    @Test
    fun findNonExistingRecipe() {
        assertNull(craftingService.findRecipe(999))
    }

    @Test
    fun listAvailableIngredients() {
        val result = craftingService.listAvailableIngredients()

        assertTrue(result.contains("Moon Herb"))
        assertTrue(result.contains("Magic Crystal"))
    }

    @Test
    fun listAvailablePotions() {
        val result = craftingService.listAvailablePotions()

        assertTrue(result.contains("Healing Potion"))
    }

    @Test
    fun listAvailableRecipes() {
        val result = craftingService.listAvailableRecipes()

        assertTrue(result.contains("Healing Recipe"))
    }

    @Test
    fun canCraftWithEnoughIngredients() {
        assertTrue(craftingService.canCraft(0))
    }

    @Test
    fun cannotCraftNonExistingRecipe() {
        assertFalse(craftingService.canCraft(999))
    }

    @Test
    fun cannotCraftEmptyRecipe() {
        recipeAPI.removeIngredient(0, 0)
        recipeAPI.removeIngredient(0, 1)

        assertFalse(craftingService.canCraft(0))
    }

    @Test
    fun cannotCraftWithInsufficientIngredients() {
        ingredientAPI.findIngredient(0)!!.quantity = 1

        assertFalse(craftingService.canCraft(0))
    }

    @Test
    fun canCraftWithExactRequiredQuantity() {
        ingredientAPI.findIngredient(0)!!.quantity = 3
        ingredientAPI.findIngredient(1)!!.quantity = 2

        assertTrue(craftingService.canCraft(0))
    }

    @Test
    fun craftPotionSuccessfully() {
        assertTrue(craftingService.craftPotion(0))

        assertEquals(
            7,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            3,
            ingredientAPI.findIngredient(1)?.quantity
        )

        assertEquals(
            1,
            potionAPI.findPotion(0)?.quantity
        )
    }

    @Test
    fun craftMultiplePotions() {
        assertTrue(craftingService.craftPotion(0))
        assertTrue(craftingService.craftPotion(0))

        assertEquals(
            4,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            1,
            ingredientAPI.findIngredient(1)?.quantity
        )

        assertEquals(
            2,
            potionAPI.findPotion(0)?.quantity
        )
    }

    @Test
    fun failedCraftingDoesNotChangeInventory() {
        ingredientAPI.findIngredient(1)!!.quantity = 1

        assertFalse(craftingService.craftPotion(0))

        assertEquals(
            10,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            1,
            ingredientAPI.findIngredient(1)?.quantity
        )

        assertEquals(
            0,
            potionAPI.findPotion(0)?.quantity
        )
    }

    @Test
    fun craftingStopsWhenIngredientsRunOut() {
        assertTrue(craftingService.craftPotion(0))
        assertTrue(craftingService.craftPotion(0))
        assertFalse(craftingService.craftPotion(0))

        assertEquals(
            4,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            1,
            ingredientAPI.findIngredient(1)?.quantity
        )

        assertEquals(
            2,
            potionAPI.findPotion(0)?.quantity
        )
    }

    @Test
    fun craftingWithExactQuantityLeavesZeroIngredients() {
        ingredientAPI.findIngredient(0)!!.quantity = 3
        ingredientAPI.findIngredient(1)!!.quantity = 2

        assertTrue(craftingService.craftPotion(0))

        assertEquals(
            0,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            0,
            ingredientAPI.findIngredient(1)?.quantity
        )
    }

    @Test
    fun cannotCraftWhenPotionIsMissing() {
        potionAPI.delete(0)

        assertFalse(craftingService.canCraft(0))
        assertFalse(craftingService.craftPotion(0))

        assertEquals(
            10,
            ingredientAPI.findIngredient(0)?.quantity
        )
    }

    @Test
    fun cannotCraftWhenIngredientIsMissing() {
        ingredientAPI.delete(1)

        assertFalse(craftingService.canCraft(0))
        assertFalse(craftingService.craftPotion(0))

        assertEquals(
            10,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            0,
            potionAPI.findPotion(0)?.quantity
        )
    }

    @Test
    fun noErrorWhenCraftingIsPossible() {
        assertEquals(
            "",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun errorForNonExistingRecipe() {
        assertEquals(
            "Recipe not found.",
            craftingService.getCraftingError(999)
        )
    }

    @Test
    fun errorForEmptyRecipe() {
        recipeAPI.removeIngredient(0, 0)
        recipeAPI.removeIngredient(0, 1)

        assertEquals(
            "Recipe has no ingredients.",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun errorForMissingPotion() {
        potionAPI.delete(0)

        assertEquals(
            "Potion not found.",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun errorForMissingIngredient() {
        ingredientAPI.delete(1)

        assertEquals(
            "Required ingredient not found.",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun errorForInsufficientIngredients() {
        ingredientAPI.findIngredient(0)!!.quantity = 1

        assertEquals(
            "Not enough Moon Herb. Required: 3, Available: 1.",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun cannotCraftWithInvalidRequiredQuantity() {
        recipeAPI.findRecipe(0)!!.ingredients[0].quantity = 0

        assertFalse(craftingService.canCraft(0))
        assertFalse(craftingService.craftPotion(0))

        assertEquals(
            "Invalid required quantity.",
            craftingService.getCraftingError(0)
        )
    }

    @Test
    fun availabilityCheckDoesNotChangeInventory() {
        assertTrue(craftingService.canCraft(0))

        assertEquals(
            10,
            ingredientAPI.findIngredient(0)?.quantity
        )

        assertEquals(
            5,
            ingredientAPI.findIngredient(1)?.quantity
        )

        assertEquals(
            0,
            potionAPI.findPotion(0)?.quantity
        )
    }
}
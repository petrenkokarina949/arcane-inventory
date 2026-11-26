package controllers

import ie.setu.controllers.IngredientAPI
import ie.setu.models.Ingredient
import kotlin.test.*

class IngredientAPITest {

    private lateinit var ingredientAPI: IngredientAPI
    private lateinit var moonHerb: Ingredient
    private lateinit var magicCrystal: Ingredient

    @BeforeTest
    fun setup() {
        ingredientAPI = IngredientAPI()

        moonHerb = Ingredient(
            name = "Moon Herb",
            category = "Herb",
            rarity = "Common",
            quantity = 10,
            price = 5.50,
            description = "A magical healing herb"
        )

        magicCrystal = Ingredient(
            name = "Magic Crystal",
            category = "Mineral",
            rarity = "Rare",
            quantity = 5,
            price = 25.00,
            description = "Contains magical energy"
        )
    }

    // ADD TESTS

    @Test
    fun addValidIngredient() {
        assertTrue(ingredientAPI.add(moonHerb))
        assertEquals(1, ingredientAPI.numberOfIngredients())
    }

    @Test
    fun addInvalidIngredient() {
        moonHerb.name = ""

        assertFalse(ingredientAPI.add(moonHerb))
        assertEquals(0, ingredientAPI.numberOfIngredients())
    }

    @Test
    fun assignUniqueIds() {
        ingredientAPI.add(moonHerb)
        ingredientAPI.add(magicCrystal)

        assertEquals(0, moonHerb.id)
        assertEquals(1, magicCrystal.id)
    }

    // LIST AND COUNT TESTS

    @Test
    fun listEmptyIngredients() {
        assertEquals(
            "No ingredients stored",
            ingredientAPI.listAllIngredients()
        )
    }

    @Test
    fun listAllIngredients() {
        ingredientAPI.add(moonHerb)
        ingredientAPI.add(magicCrystal)

        val result = ingredientAPI.listAllIngredients()

        assertTrue(result.contains("Moon Herb"))
        assertTrue(result.contains("Magic Crystal"))
    }

    @Test
    fun countIngredients() {
        assertEquals(0, ingredientAPI.numberOfIngredients())

        ingredientAPI.add(moonHerb)
        ingredientAPI.add(magicCrystal)

        assertEquals(2, ingredientAPI.numberOfIngredients())
    }

    // SEARCH AND FIND TESTS

    @Test
    fun findExistingIngredient() {
        ingredientAPI.add(moonHerb)

        assertEquals(
            moonHerb,
            ingredientAPI.findIngredient(moonHerb.id)
        )
        assertTrue(ingredientAPI.isValidId(moonHerb.id))
    }

    @Test
    fun findNonExistingIngredient() {
        assertNull(ingredientAPI.findIngredient(99))
        assertFalse(ingredientAPI.isValidId(99))
    }

    @Test
    fun searchIngredientByName() {
        ingredientAPI.add(moonHerb)
        ingredientAPI.add(magicCrystal)

        val result = ingredientAPI.searchByName("Moon")

        assertTrue(result.contains("Moon Herb"))
        assertFalse(result.contains("Magic Crystal"))
    }

    @Test
    fun searchIngredientIgnoringCase() {
        ingredientAPI.add(moonHerb)

        val result = ingredientAPI.searchByName("mOoN")

        assertTrue(result.contains("Moon Herb"))
    }

    @Test
    fun searchNonExistingIngredient() {
        ingredientAPI.add(moonHerb)

        assertEquals(
            "No matching ingredients found",
            ingredientAPI.searchByName("Dragon")
        )
    }

    // UPDATE TESTS

    @Test
    fun updateExistingIngredient() {
        ingredientAPI.add(moonHerb)

        val updatedIngredient = Ingredient(
            name = "Moon Essence",
            category = "Magical Essence",
            rarity = "Epic",
            quantity = 20,
            price = 15.00,
            description = "A powerful magical essence"
        )

        val id = moonHerb.id

        assertTrue(
            ingredientAPI.update(id, updatedIngredient)
        )

        val result = ingredientAPI.findIngredient(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals(updatedIngredient.name, result.name)
        assertEquals(updatedIngredient.category, result.category)
        assertEquals(updatedIngredient.rarity, result.rarity)
        assertEquals(updatedIngredient.quantity, result.quantity)
        assertEquals(updatedIngredient.price, result.price)
        assertEquals(updatedIngredient.description, result.description)
    }

    @Test
    fun updateNonExistingIngredient() {
        assertFalse(
            ingredientAPI.update(99, magicCrystal)
        )
    }

    @Test
    fun updateIngredientWithInvalidData() {
        ingredientAPI.add(moonHerb)

        magicCrystal.price = -10.0

        assertFalse(
            ingredientAPI.update(moonHerb.id, magicCrystal)
        )

        val result = ingredientAPI.findIngredient(moonHerb.id)

        assertNotNull(result)
        assertEquals("Moon Herb", result.name)
        assertEquals(5.50, result.price)
    }

    // DELETE TESTS

    @Test
    fun deleteExistingIngredient() {
        ingredientAPI.add(moonHerb)
        ingredientAPI.add(magicCrystal)

        assertTrue(ingredientAPI.delete(moonHerb.id))
        assertEquals(1, ingredientAPI.numberOfIngredients())
        assertNull(ingredientAPI.findIngredient(moonHerb.id))
        assertNotNull(ingredientAPI.findIngredient(magicCrystal.id))
    }

    @Test
    fun deleteNonExistingIngredient() {
        ingredientAPI.add(moonHerb)

        assertFalse(ingredientAPI.delete(99))
        assertEquals(1, ingredientAPI.numberOfIngredients())
    }
}
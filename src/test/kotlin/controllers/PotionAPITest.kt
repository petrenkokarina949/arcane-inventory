package controllers

import ie.setu.controllers.PotionAPI
import ie.setu.models.Potion
import kotlin.test.*

class PotionAPITest {

    private lateinit var potionAPI: PotionAPI
    private lateinit var healingPotion: Potion
    private lateinit var manaPotion: Potion

    @BeforeTest
    fun setup() {
        potionAPI = PotionAPI()

        healingPotion = Potion(
            name = "Healing Potion",
            category = "Healing",
            rarity = "Common",
            effect = "Restores 50 HP",
            quantity = 10,
            price = 25.50,
            description = "Restores health"
        )

        manaPotion = Potion(
            name = "Mana Potion",
            category = "Mana",
            rarity = "Rare",
            effect = "Restores 100 MP",
            quantity = 5,
            price = 40.00,
            description = "Restores mana"
        )
    }

    // ADD TESTS

    @Test
    fun addValidPotion() {
        assertTrue(potionAPI.add(healingPotion))
        assertEquals(1, potionAPI.numberOfPotions())
    }

    @Test
    fun addInvalidPotion() {
        healingPotion.name = ""

        assertFalse(potionAPI.add(healingPotion))
        assertEquals(0, potionAPI.numberOfPotions())
    }

    @Test
    fun assignUniqueIds() {
        potionAPI.add(healingPotion)
        potionAPI.add(manaPotion)

        assertEquals(0, healingPotion.id)
        assertEquals(1, manaPotion.id)
    }

    // LIST AND COUNT TESTS

    @Test
    fun listEmptyPotions() {
        assertEquals(
            "No potions stored",
            potionAPI.listAllPotions()
        )
    }

    @Test
    fun listAllPotions() {
        potionAPI.add(healingPotion)
        potionAPI.add(manaPotion)

        val result = potionAPI.listAllPotions()

        assertTrue(result.contains("Healing Potion"))
        assertTrue(result.contains("Mana Potion"))
    }

    @Test
    fun countPotions() {
        assertEquals(0, potionAPI.numberOfPotions())

        potionAPI.add(healingPotion)
        potionAPI.add(manaPotion)

        assertEquals(2, potionAPI.numberOfPotions())
    }

    // SEARCH AND FIND TESTS

    @Test
    fun findExistingPotion() {
        potionAPI.add(healingPotion)

        assertEquals(
            healingPotion,
            potionAPI.findPotion(healingPotion.id)
        )
        assertTrue(potionAPI.isValidId(healingPotion.id))
    }

    @Test
    fun findNonExistingPotion() {
        assertNull(potionAPI.findPotion(99))
        assertFalse(potionAPI.isValidId(99))
    }

    @Test
    fun searchPotionByName() {
        potionAPI.add(healingPotion)
        potionAPI.add(manaPotion)

        val result = potionAPI.searchByName("Healing")

        assertTrue(result.contains("Healing Potion"))
        assertFalse(result.contains("Mana Potion"))
    }

    @Test
    fun searchPotionIgnoringCase() {
        potionAPI.add(healingPotion)

        val result = potionAPI.searchByName("hEaLiNg")

        assertTrue(result.contains("Healing Potion"))
    }

    @Test
    fun searchNonExistingPotion() {
        potionAPI.add(healingPotion)

        assertEquals(
            "No matching potions found",
            potionAPI.searchByName("Poison")
        )
    }

    // UPDATE TESTS

    @Test
    fun updateExistingPotion() {
        potionAPI.add(healingPotion)

        val updatedPotion = Potion(
            name = "Greater Healing Potion",
            category = "Healing",
            rarity = "Epic",
            effect = "Restores 200 HP",
            quantity = 20,
            price = 75.00,
            description = "Powerful healing potion"
        )

        val id = healingPotion.id

        assertTrue(potionAPI.update(id, updatedPotion))

        val result = potionAPI.findPotion(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals(updatedPotion.name, result.name)
        assertEquals(updatedPotion.category, result.category)
        assertEquals(updatedPotion.rarity, result.rarity)
        assertEquals(updatedPotion.effect, result.effect)
        assertEquals(updatedPotion.quantity, result.quantity)
        assertEquals(updatedPotion.price, result.price)
        assertEquals(updatedPotion.description, result.description)
    }

    @Test
    fun updateNonExistingPotion() {
        assertFalse(potionAPI.update(99, manaPotion))
    }

    @Test
    fun updatePotionWithInvalidData() {
        potionAPI.add(healingPotion)

        manaPotion.price = -10.0

        assertFalse(
            potionAPI.update(healingPotion.id, manaPotion)
        )

        val result = potionAPI.findPotion(healingPotion.id)

        assertNotNull(result)
        assertEquals("Healing Potion", result.name)
        assertEquals(25.50, result.price)
    }

    // DELETE TESTS

    @Test
    fun deleteExistingPotion() {
        potionAPI.add(healingPotion)
        potionAPI.add(manaPotion)

        assertTrue(potionAPI.delete(healingPotion.id))
        assertEquals(1, potionAPI.numberOfPotions())
        assertNull(potionAPI.findPotion(healingPotion.id))
        assertNotNull(potionAPI.findPotion(manaPotion.id))
    }

    @Test
    fun deleteNonExistingPotion() {
        potionAPI.add(healingPotion)

        assertFalse(potionAPI.delete(99))
        assertEquals(1, potionAPI.numberOfPotions())
    }
}
package ie.setu.controllers

import ie.setu.models.Potion
import ie.setu.utils.formatPrice
import ie.setu.utils.isValidPotion

class PotionAPI {

    private var potions = ArrayList<Potion>()
    private var lastId = 0

    private fun getId(): Int {
        return lastId++
    }

    fun add(potion: Potion): Boolean {
        if (!isValidPotion(potion)) {
            return false
        }
        potion.id = getId()
        return potions.add(potion)
    }

    fun numberOfPotions(): Int {
        return potions.size
    }

    fun listAllPotions(): String {
        return if (potions.isEmpty()) {
            "No potions stored"
        } else {
            potions.joinToString("\n") { potion ->
                "${potion.id}: ${potion.name} | " +
                        "${potion.category} | ${potion.rarity} | " +
                        "Quantity: ${potion.quantity} | " +
                        "Price: ${formatPrice(potion.price)}"
            }
        }
    }

    fun findPotion(id: Int): Potion? {
        return potions.find { potion -> potion.id == id }
    }

    fun isValidId(id: Int): Boolean {
        return potions.any { potion -> potion.id == id }
    }

    fun update(id: Int, potion: Potion): Boolean {
        if (!isValidPotion(potion)) {
            return false
        }
        val foundPotion = findPotion(id)

        if (foundPotion != null) {
            foundPotion.name = potion.name
            foundPotion.category = potion.category
            foundPotion.rarity = potion.rarity
            foundPotion.effect = potion.effect
            foundPotion.quantity = potion.quantity
            foundPotion.price = potion.price
            foundPotion.description = potion.description
            return true
        }

        return false
    }

    fun delete(id: Int): Boolean {
        return potions.removeIf { potion -> potion.id == id }
    }

    fun searchByName(searchString: String): String {
        return potions.filter { potion ->
            potion.name.contains(searchString, ignoreCase = true)
        }.joinToString("\n") { potion ->
            "${potion.id}: ${potion.name} | " +
                    "${potion.category} | ${potion.rarity} | " +
                    "Quantity: ${potion.quantity} | " +
                    "Price: ${formatPrice(potion.price)}"
        }.ifEmpty { "No matching potions found" }
    }
}
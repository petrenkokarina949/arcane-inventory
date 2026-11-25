package ie.setu.controllers

import ie.setu.models.Potion
import ie.setu.utils.formatPrice

class PotionAPI {

    private var potions = ArrayList<Potion>()
    private var lastId = 0

    private fun getId(): Int {
        return lastId++
    }
    fun add(potion: Potion): Boolean {
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
}
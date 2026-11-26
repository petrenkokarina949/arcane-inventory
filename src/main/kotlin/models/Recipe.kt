package ie.setu.models

data class Recipe(
    var id: Int = 0,
    var name: String,
    var potionId: Int,
    var description: String,
    var ingredients: ArrayList<RecipeIngredient> = ArrayList()
)
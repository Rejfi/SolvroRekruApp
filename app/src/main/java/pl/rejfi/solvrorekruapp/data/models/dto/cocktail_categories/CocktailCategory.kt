package pl.rejfi.solvrorekruapp.data.models.dto.cocktail_categories

enum class CocktailCategory(val value: String) {
    COCKTAIL("Cocktail"),
    ORDINARY_DRINK("Ordinary Drink"),
    PUNCH_PARTY_DRINK("Punch / Party Drink"),
    SHAKE("Shake"),
    OTHER_UNKNOWN("Other / Unknown"),
    COCOA("Cocoa"),
    SHOT("Shot"),
    COFFEE_TEA("Coffee / Tea"),
    HOMEMADE_LIQUEUR("Homemade Liqueur"),
    SOFT_DRINK("Soft Drink"),
    ALL("All");

    override fun toString(): String {
        return value
    }
}

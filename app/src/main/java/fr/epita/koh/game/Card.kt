package fr.epita.koh.game

import java.util.*

enum class CardEffect {
    None,
    ExtraAttack,
    ExtraHealTimes2,
    PermanentCostDiscount,
    ExtraDiceRoll,
    StealKingSpot,
    ExtraVictoryPointsTimes2,
    TakeAway2VictoryPointsAll
}

class Card (title : String, description : String, cost : Int, effect : EnumSet<CardEffect>) {
    val cardTitle = title;
    val cardDescription = description;
    val cardEffect = effect;
    val cardCost = cost;
}
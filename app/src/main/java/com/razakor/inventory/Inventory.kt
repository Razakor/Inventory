package com.razakor.inventory

class Inventory(character: Character) {
    val id: Int = 0
    val characterId = character.id
    var gold: Int = 0
    var silver: Int = 0
    var copper: Int = 0
    var platinum: Int = 0
    var electrum: Int = 0
    val items: MutableList<Item> = mutableListOf()

    fun addItem(item: Item) = items.add(item)
    fun removeItem(item: Item) = items.remove(item)
}
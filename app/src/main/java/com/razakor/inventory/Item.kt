package com.razakor.inventory

class Item(inventory: Inventory) {
    val id = 0
    val inventoryId = inventory.id
    var name: String = "Name"
    var rarity: String = "Rarity"
    var type: String = "Type"
    var description: String = "Description"
    var count: Int = 1
    var cost: Int = 0
}
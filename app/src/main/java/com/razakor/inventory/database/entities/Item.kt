package com.razakor.inventory.database.entities

class Item(inventory: Inventory) {
    var id = 0
    val inventoryId = inventory.id
    var name: String = "Name"
    var rarity: String = "Rarity"
    var type: String = "Type"
    var count: Int = 1
    var price: Int? = null
    var description: String? = null
}
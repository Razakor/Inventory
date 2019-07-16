package com.razakor.inventory

class Item(inventory: Inventory) {
    var id = 0
    val inventoryId = inventory.id
    var name: String = "Name"
    var rarity: String = "Rarity"
    var type: String = "Type"
    var description: String = "Description"
    var count: Int = 1
    var price: Int = 0
}
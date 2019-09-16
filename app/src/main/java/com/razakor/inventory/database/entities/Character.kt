package com.razakor.inventory.database.entities

class Character {

    var id: Int = 0
    var name: String = "Character"
    var race: String = "Race"
    var clas: String = "Class"
    var xp: Int = 0
        set(value) {
            when {
                value in 0..299 -> lvl = 1
                value in 300..899 -> lvl = 2
                value in 900..2699 -> lvl = 3
                value in 2700..6499 -> lvl = 4
                value in 6500..13999 -> lvl = 5
                value in 14000..22999 -> lvl = 6
                value in 23000..33999 -> lvl = 7
                value in 34000..47999 -> lvl = 8
                value in 48000..63999 -> lvl = 9
                value in 64000..84999 -> lvl = 10
                value in 85000..99999 -> lvl = 11
                value in 100000..119999 -> lvl = 12
                value in 120000..139999 -> lvl = 13
                value in 140000..164999 -> lvl = 14
                value in 165000..194999 -> lvl = 15
                value in 195000..224999 -> lvl = 16
                value in 225000..264999 -> lvl = 17
                value in 265000..304999 -> lvl = 18
                value in 305000..354999 -> lvl = 19
                value >= 355000 -> lvl = 20
            }
            field = value
        }
    var lvl: Int = 1
    var description: String? = null
    val inventory = Inventory(this)
}
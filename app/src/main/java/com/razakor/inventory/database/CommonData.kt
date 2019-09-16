package com.razakor.inventory.database

import android.database.sqlite.SQLiteDatabase
import com.razakor.inventory.database.entities.Character

lateinit var db: SQLiteDatabase

var characters: MutableList<Character> = mutableListOf()

lateinit var raceMap: MutableMap<String, Int>
lateinit var classMap: MutableMap<String, Int>
lateinit var rarityMap: MutableMap<String, Int>
lateinit var typeMap: MutableMap<String, Int>

var raceArray: Array<String> = emptyArray()
var classArray: Array<String> = emptyArray()
var rarityArray: Array<String> = emptyArray()
var typeArray: Array<String> = emptyArray()

fun initDatabase(database: SQLiteDatabase) {
    db = database
}

fun initMaps() {
    raceMap =
        getTableMap("character_races")
    classMap =
        getTableMap("character_classes")
    rarityMap =
        getTableMap("item_rarities")
    typeMap =
        getTableMap("item_types")
}

fun mapsToArray() {
    raceMap.forEach { (key, _) ->
        raceArray += key
    }
    classMap.forEach { (key, _) ->
        classArray += key
    }
    rarityMap.forEach { (key, _) ->
        rarityArray += key
    }
    typeMap.forEach { (key, _) ->
        typeArray += key
    }
}

private fun getTableMap(table: String) : MutableMap<String, Int> {
    val map: MutableMap<String, Int> = mutableMapOf()
    var tmpId: Int
    var tmpName: String

    val cursor = db.rawQuery("SELECT * FROM $table", null)
    cursor.moveToFirst()
    while (!cursor.isAfterLast) {
        tmpId = cursor.getInt(0)
        tmpName = cursor.getString(1)
        map[tmpName] = tmpId
        cursor.moveToNext()
    }
    cursor.close()

    return map
}
package com.razakor.inventory

import android.database.sqlite.SQLiteDatabase

lateinit var raceMap: MutableMap<String, Int>
lateinit var classMap: MutableMap<String, Int>
lateinit var rarityMap: MutableMap<String, Int>
lateinit var typeMap: MutableMap<String, Int>

var raceArray: Array<String> = emptyArray()
var classArray: Array<String> = emptyArray()
var rarityArray: Array<String> = emptyArray()
var typeArray: Array<String> = emptyArray()


fun initMaps(db: SQLiteDatabase) {
    raceMap = getTableMap(db, "character_races")
    classMap = getTableMap(db, "character_classes")
    rarityMap = getTableMap(db, "item_rarities")
    typeMap = getTableMap(db, "item_types")
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

private fun getTableMap(db: SQLiteDatabase, table: String) : MutableMap<String, Int> {
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
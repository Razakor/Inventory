package com.razakor.inventory.database

import com.razakor.inventory.database.entities.Character
import com.razakor.inventory.database.entities.Inventory
import com.razakor.inventory.database.entities.Item

fun characterDataInit(characterList: MutableList<Character>) {
    val query =
        "SELECT characters.id, characters.name, character_races.name, character_classes.name, characters.experience, characters.description\n" +
                "FROM characters\n" +
                "INNER JOIN character_races\n" +
                "ON characters.race_id = character_races.id\n" +
                "INNER JOIN character_classes\n" +
                "ON characters.class_id = character_classes.id"

    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    while (!cursor.isAfterLast) {
        val tmpCharacter = Character()
        tmpCharacter.id = cursor.getInt(0)
        tmpCharacter.name = cursor.getString(1)
        tmpCharacter.race = cursor.getString(2)
        tmpCharacter.clas = cursor.getString(3)
        tmpCharacter.xp = cursor.getInt(4)
        tmpCharacter.description = if (cursor.getString(5) != "null") cursor.getString(5) else null
        characterList.add(tmpCharacter)
        cursor.moveToNext()
    }
    cursor.close()
}

fun addCharacterToDatabase(character: Character, race_id: Int, class_id: Int) {
    val query =
        "INSERT INTO characters(name, race_id, class_id, experience, description)\n" +
                "VALUES ('${character.name}', '$race_id', '$class_id', '${character.xp}', '${character.description}')"

    db.execSQL(query)
}

fun editXpInDatabase(characterId: Int, characterXp: Int) {
    val query =
        "UPDATE characters\n" +
                "SET experience = '$characterXp'\n" +
                "WHERE characters.id = '$characterId'"
    db.execSQL(query)
}

fun editDescriptionInDatabase(characterId: Int, characterDescription: String) {
    val query =
        "UPDATE characters\n" +
                "SET description = '$characterDescription'\n" +
                "WHERE characters.id = '$characterId'"
    db.execSQL(query)
}

fun deleteCharacterFromDatabase(character: Character) {
    val query =
        "DELETE FROM characters\n" +
                "WHERE characters.id = ${character.id}"
    db.execSQL(query)
    deleteInventoryFromDatabase(character)
    deleteInventoryItemsFromDatabase(character)
}

fun setCharacterIdFromDatabase(character: Character, race_id: Int, class_id: Int) {
    val query =
        "SELECT characters.id FROM characters\n" +
                "WHERE characters.name = '${character.name}' AND characters.race_id = '$race_id' AND characters.class_id = '$class_id' AND characters.experience = '${character.xp}' AND characters.description = '${character.description}'"
    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    character.id = cursor.getInt(0)
    cursor.close()
}

fun inventoryDataInit (characterList: MutableList<Character>) {
    val query =
        "SELECT * FROM inventories"

    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    while (!cursor.isAfterLast) {
        val tmpInventory = characterList.filter { it.id == cursor.getInt(1) }[0].inventory
        tmpInventory.apply {
            id = cursor.getInt(0)
            gold = cursor.getInt(2)
            silver = cursor.getInt(3)
            copper = cursor.getInt(4)
            platinum = cursor.getInt(5)
            electrum = cursor.getInt(6)
        }
        cursor.moveToNext()
    }
    cursor.close()

}
fun createInventoryInDatabase(character: Character) {
    val query =
        "INSERT INTO inventories(character_id, gold, silver, copper, platinum, electrum)\n" +
                "VALUES ('${character.id}', '" +
                "${character.inventory.gold}', '" +
                "${character.inventory.silver}', '" +
                "${character.inventory.copper}', '" +
                "${character.inventory.platinum}', '" +
                "${character.inventory.electrum}')"
    db.execSQL(query)
}

fun setInventoryIdFromDatabase(character: Character) {
    val query =
        "SELECT inventories.id FROM inventories\n" +
                "WHERE inventories.character_id = '${character.id}'"
    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    character.inventory.id = cursor.getInt(0)
    cursor.close()
}

fun editMoneyInDatabase(inventory: Inventory) {
    with(inventory) {
        val query =
            "UPDATE inventories\n" +
                    "SET " +
                    "gold = '$gold', " +
                    "silver = '$silver', " +
                    "copper = '$copper', " +
                    "platinum = '$platinum', " +
                    "electrum = '$electrum'\n" +
                    "WHERE inventories.id = '$id'"
        db.execSQL(query)
    }
}

fun deleteInventoryFromDatabase(character: Character) {
    val query =
        "DELETE FROM inventories\n" +
                "WHERE inventories.character_id = ${character.id}"
    db.execSQL(query)
}

fun itemsDataInit(inventory: Inventory) {
    val itemList = inventory.items
    val query =
        "SELECT items.id, items.name, item_rarities.name, item_types.name, items.price, items.description, items.count\n" +
                "FROM items\n" +
                "INNER JOIN item_rarities\n" +
                "ON items.rarity_id = item_rarities.id\n" +
                "INNER JOIN item_types\n" +
                "ON items.type_id = item_types.id\n" +
                "WHERE items.inventory_id = '${inventory.id}'"

    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    while (!cursor.isAfterLast) {
        val tmpItem = Item(inventory)
        tmpItem.apply {
            id = cursor.getInt(0)
            name = cursor.getString(1)
            rarity = cursor.getString(2)
            type = cursor.getString(3)
            price = if (cursor.getInt(4) != 0) cursor.getInt(4) else null
            description = if (cursor.getString(5) != "null") cursor.getString(5) else null
            count = cursor.getInt(6)
        }
        itemList.add(tmpItem)
        cursor.moveToNext()
    }
    cursor.close()
}

fun addItemToDatabase(item: Item, rarity_id: Int, type_id: Int) {
    val query =
        "INSERT INTO items(inventory_id, name, rarity_id, type_id, price, description, count)\n" +
                "VALUES ('${item.inventoryId}', '" +
                "${item.name}', '" +
                "$rarity_id', '" +
                "$type_id', '" +
                "${item.price}', '" +
                "${item.description}', '" +
                "${item.count}')"
    db.execSQL(query)
}

fun insertItemToDatabase(item: Item, rarity_id: Int, type_id: Int) {
    val query =
        "INSERT INTO items(id, inventory_id, name, rarity_id, type_id, price, description, count)\n" +
                "VALUES ('" +
                "${item.id}', '" +
                "${item.inventoryId}', '" +
                "${item.name}', '" +
                "$rarity_id', '" +
                "$type_id', '" +
                "${item.price}', '" +
                "${item.description}', '" +
                "${item.count}')"
    db.execSQL(query)
}

fun itemCountChange(itemId: Int, operation: String) {
    val query =
        "UPDATE items\n" +
                "SET count = count $operation\n" +
                "WHERE items.id = '$itemId'"
    db.execSQL(query)
}

fun editItemInDatabase(item: Item, rarityId: Int, typeId: Int) {
    val query =
        "UPDATE items\n" +
                "SET " +
                "name = '${item.name}', " +
                "rarity_id = '$rarityId', " +
                "type_id = '$typeId', " +
                "price = '${item.price}', " +
                "description = '${item.description}', " +
                "count = '${item.count}'\n" +
                "WHERE items.id = '${item.id}'"
    db.execSQL(query)
}

fun deleteItemFromDatabase(itemId: Int) {
    val query =
        "DELETE FROM items\n" +
                "WHERE items.id = '$itemId'"
    db.execSQL(query)
}

fun setItemIdFromDatabase(item: Item, rarity_id: Int, type_id: Int) {
    val query =
        "SELECT items.id FROM items\n" +
                "WHERE items.name = '${item.name}' " +
                "AND items.rarity_id = '$rarity_id' " +
                "AND items.type_id = '$type_id' " +
                "AND items.price = '${item.price}' " +
                "AND items.description = '${item.description}' " +
                "AND items.count = '${item.count}'"

    val cursor = db.rawQuery(query, null)
    cursor.moveToFirst()
    item.id = cursor.getInt(0)
    cursor.close()
}

fun deleteInventoryItemsFromDatabase(character: Character) {
    val query =
        "DELETE FROM items\n" +
                "WHERE items.inventory_id = ${character.inventory.id}"
    db.execSQL(query)
}
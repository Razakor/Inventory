package com.razakor.inventory

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
        tmpCharacter.description = cursor.getString(5)
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

fun deleteCharacterFromDatabase(character: Character) {
    val query =
        "DELETE FROM characters\n" +
                "WHERE characters.id = ${character.id}"
    db.execSQL(query)
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


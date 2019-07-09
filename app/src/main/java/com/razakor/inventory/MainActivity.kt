package com.razakor.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.character_dialog.view.*
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener



class MainActivity : AppCompatActivity() {

    private var characters: MutableList<Character> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.database

        createCharacterRecyclerView(db)

        initMaps(db)

        characterDataInit(db, characters)





        btnAddCharacter.setOnClickListener {
            val characterDialog = LayoutInflater.from(this).inflate(R.layout.character_dialog, null)
            val characterDialogBuilder = AlertDialog.Builder(this)
                .setView(characterDialog)
                .setTitle("Create Character")

            val characterAlertDialog = characterDialogBuilder.show()

            characterDialog.btnOK.setOnClickListener {
                characterAlertDialog.dismiss()

                val character = Character()

                character.name = characterDialog.edit_name.text.toString()
                character.race = characterDialog.edit_race.text.toString()
                character.clas = characterDialog.edit_class.text.toString()
                character.xp = characterDialog.edit_experience.text.toString().toInt()

                characters.add(character)
                addCharacterToDatabase(db, character, raceMap[character.race]!!, classMap[character.clas]!!)
                setCharacterIdFromDatabase(db, character, raceMap[character.race]!!, classMap[character.clas]!!)
            }

            characterDialog.btnCancel.setOnClickListener {
                characterAlertDialog.dismiss()
            }
        }





    }

    private fun createCharacterRecyclerView(db: SQLiteDatabase){
        val characterRecyclerView: RecyclerView = findViewById(R.id.rv_character)
        val layoutManager = LinearLayoutManager(this)
        val characterAdapter = CharacterAdapter(db, characters)
        characterRecyclerView.layoutManager = layoutManager
        characterRecyclerView.adapter = characterAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }









}

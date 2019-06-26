package com.razakor.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.character_dialog.view.*
import kotlinx.android.synthetic.main.content_character.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btnAddCharacter.setOnClickListener { view ->
            val characterDialog = LayoutInflater.from(this).inflate(R.layout.character_dialog, null)
            val characterDialogBuilder = AlertDialog.Builder(this)
                .setView(characterDialog)
                .setTitle("Create Character")
            val  characterAlertDialog = characterDialogBuilder.show()
            characterDialog.btnOK.setOnClickListener {
                characterAlertDialog.dismiss()
                val name = characterDialog.edit_name.text.toString()
                val race = characterDialog.edit_race.text.toString()
                val clas = characterDialog.edit_class.text.toString()
                val xp = characterDialog.edit_experience.text.toString()
                 textView.setText("Name: $name, Race: $race, Class: $clas, XP: $xp")
            }
            characterDialog.btnCancel.setOnClickListener {
                characterAlertDialog.dismiss()
            }
        }
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

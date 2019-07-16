package com.razakor.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inventory)

        val characterId = intent.getIntExtra("character_id", 0)
        val character = characters.filter { it.id == characterId }[0]
        val inventory = character.inventory

        createItemRecyclerView(inventory.items)

        btnAddItem.setOnClickListener {
            val itemDialog = LayoutInflater.from(this).inflate(R.layout.item_dialog, null)
            val itemDialogBuilder = AlertDialog.Builder(this)
                .setView(itemDialog)
                .setTitle("Add Item")

            val itemAlertDialog = itemDialogBuilder.show()

            val spinnerType: Spinner = itemDialog.findViewById(R.id.spinner_type)
            val typeAA = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeArray)
            typeAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerType.adapter = typeAA

            val spinnerRarity: Spinner = itemDialog.findViewById(R.id.spinner_rarity)
            val rarityAA = ArrayAdapter(this, android.R.layout.simple_spinner_item, rarityArray)
            rarityAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRarity.adapter = rarityAA

            itemDialog.btnOK.setOnClickListener {
                itemAlertDialog.dismiss()

                val item = Item(inventory)

                if (itemDialog.edit_name.text.toString() != "") {
                    item.name = itemDialog.edit_name.text.toString()
                    item.type = spinnerType.selectedItem.toString()
                    item.rarity = spinnerRarity.selectedItem.toString()
                    item.price = itemDialog.edit_price.text.toString().toInt()
                    item.count = itemDialog.edit_count.text.toString().toInt()
                    item.description = itemDialog.edit_description.text.toString()

                    inventory.addItem(item)
                    //addCharacterToDatabase(db, character, raceMap[character.race]!!, classMap[character.clas]!!)
                    //setCharacterIdFromDatabase(db, character, raceMap[character.race]!!, classMap[character.clas]!!)
                } else {
                    Toast.makeText(this, "Set correct name", Toast.LENGTH_LONG).show()
                }
            }

            itemDialog.btnCancel.setOnClickListener {
                itemAlertDialog.dismiss()
            }
        }

    }

    private fun createItemRecyclerView(items: MutableList<Item>){
        val itemRecyclerView: RecyclerView = findViewById(R.id.rv_item)
        val layoutManager = LinearLayoutManager(this)
        val itemAdapter = ItemAdapter(items)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = itemAdapter
    }

}
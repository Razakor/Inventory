package com.razakor.inventory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class CharacterViewActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.character_view)

        val characterId = intent.getIntExtra("character_id", 0)
        val character = characters.filter { it.id == characterId }[0]
        val inventory = character.inventory

        val navigationView = this.findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)

        val characterName = navigationView.findViewById<TextView>(R.id.nav_name)
        val characterLvl = navigationView.findViewById<TextView>(R.id.nav_lvl)
        val characterRace = navigationView.findViewById<TextView>(R.id.nav_race)
        val characterClass = navigationView.findViewById<TextView>(R.id.nav_class)
        val characterXp = navigationView.findViewById<TextView>(R.id.nav_xp)
        val characterGold = navigationView.findViewById<TextView>(R.id.money_gold)
        val characterSilver = navigationView.findViewById<TextView>(R.id.money_silver)
        val characterCopper = navigationView.findViewById<TextView>(R.id.money_copper)
        val characterPlatinum = navigationView.findViewById<TextView>(R.id.money_platinum)
        val characterElectrum = navigationView.findViewById<TextView>(R.id.money_electrum)
        val characterDescription = navigationView.findViewById<TextView>(R.id.nav_description)

        with(character) {
            characterName.text = name
            characterLvl.text = lvl.toString()
            characterRace.text = race
            characterClass.text = clas
            characterXp.text = "Xp: $xp"
            characterGold.text = "Gold: ${inventory.gold}"
            characterSilver.text = "Silver: ${inventory.silver}"
            characterCopper.text = "Copper: ${inventory.copper}"
            characterPlatinum.text = "Platinum: ${inventory.platinum}"
            characterElectrum.text = "Electrum: ${inventory.electrum}"
            characterDescription.text = description
        }

        createItemRecyclerView(inventory.items)

        if (inventory.items.isEmpty()) {
            itemsDataInit(inventory)
        }


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
                    item.apply {
                        name = itemDialog.edit_name.text.toString()
                        type = spinnerType.selectedItem.toString()
                        rarity = spinnerRarity.selectedItem.toString()
                        price = itemDialog.edit_price.text.toString().toInt()
                        count = itemDialog.edit_count.text.toString().toInt()
                        description = itemDialog.edit_description.text.toString()
                    }
                    inventory.addItem(item)
                    addItemToDatabase(item, rarityMap[item.rarity]!!, typeMap[item.type]!!)
                    setItemIdFromDatabase(item, rarityMap[item.rarity]!!, typeMap[item.type]!!)
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
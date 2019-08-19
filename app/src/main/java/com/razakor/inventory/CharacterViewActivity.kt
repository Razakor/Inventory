package com.razakor.inventory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class CharacterViewActivity : AppCompatActivity() {

    private lateinit var deleteIcon: Drawable
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.character_view)

        val characterId = intent.getIntExtra("character_id", 0)
        val character = characters.filter { it.id == characterId }[0]
        val inventory = character.inventory


        createNavigationView(character)

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

    @SuppressLint("SetTextI18n")
    private fun createNavigationView(character: Character) {
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
    }

    private fun createItemRecyclerView(items: MutableList<Item>){
        val itemRecyclerView: RecyclerView = findViewById(R.id.rv_item)
        val layoutManager = LinearLayoutManager(this)
        val itemAdapter = ItemAdapter(items)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = itemAdapter
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete_white_40)!!

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                itemAdapter.removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                swipeBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)

                deleteIcon.setBounds(
                    itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                    itemView.top + iconMargin,
                    itemView.right - iconMargin,
                    itemView.bottom - iconMargin)

                swipeBackground.draw(c)

                c.save()
                c.clipRect(itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)
                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(itemRecyclerView)
    }
}
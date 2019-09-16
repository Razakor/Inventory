package com.razakor.inventory.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.razakor.inventory.adapters.ItemAdapter
import com.razakor.inventory.database.*
import com.razakor.inventory.database.entities.Character
import com.razakor.inventory.database.entities.Item
import com.razakor.inventory.R
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.edit_xp.view.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class CharacterViewActivity : AppCompatActivity() {

    private lateinit var deleteIcon: Drawable
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.RED)
    private lateinit var itemAdapter: ItemAdapter

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
            val typeAA = ArrayAdapter(this, android.R.layout.simple_spinner_item,
                typeArray
            )
            typeAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerType.adapter = typeAA

            val spinnerRarity: Spinner = itemDialog.findViewById(R.id.spinner_rarity)
            val rarityAA = ArrayAdapter(this, android.R.layout.simple_spinner_item,
                rarityArray
            )
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
                        count = if (itemDialog.edit_count.text.toString() != "") {
                            itemDialog.edit_count.text.toString().toInt()
                        } else 1
                        price = if (itemDialog.edit_price.text.toString() != "") {
                            itemDialog.edit_price.text.toString().toInt()
                        } else null
                        description = if (itemDialog.edit_description.text.toString() != "") {
                            itemDialog.edit_description.text.toString()
                        } else null
                    }
                    inventory.addItem(item)
                    addItemToDatabase(
                        item,
                        rarityMap[item.rarity]!!,
                        typeMap[item.type]!!
                    )
                    setItemIdFromDatabase(
                        item,
                        rarityMap[item.rarity]!!,
                        typeMap[item.type]!!
                    )
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

        val money = navigationView.findViewById<LinearLayout>(R.id.money)

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

        characterXp.setOnClickListener {
            val xpDialog = LayoutInflater.from(this).inflate(R.layout.edit_xp, null)
            val xpDialogBuilder = AlertDialog.Builder(this)
                .setView(xpDialog)
                .setTitle("Edit XP")

            val editXp = xpDialog.findViewById<EditText>(R.id.edit_xp)

            val xpAlertDialog = xpDialogBuilder.show()

            editXp.setText(character.xp.toString())
            editXp.isSelected = true

            xpDialog.button_ok.setOnClickListener {
                xpAlertDialog.dismiss()
                character.xp = editXp.text.toString().toInt()
                characterXp.text = "Xp: ${character.xp}"
                characterLvl.text = character.lvl.toString()
                editXpInDatabase(character.id, character.xp)
            }

            xpDialog.button_cancel.setOnClickListener {
                xpAlertDialog.dismiss()
            }
        }

        characterDescription.setOnClickListener {
            val descriptionDialog = LayoutInflater.from(this).inflate(R.layout.edit_description, null)
            val descriptionDialogBuilder = AlertDialog.Builder(this)
                .setView(descriptionDialog)
                .setTitle("Edit Description")

            val editDescription = descriptionDialog.findViewById<EditText>(R.id.edit_character_description)

            val descriptionAlertDialog = descriptionDialogBuilder.show()

            editDescription.setText(character.description)
            editDescription.isSelected = true

            descriptionDialog.button_ok.setOnClickListener {
                descriptionAlertDialog.dismiss()
                character.description = editDescription.text.toString()
                characterDescription.text = character.description
                editDescriptionInDatabase(
                    character.id,
                    character.description ?: ""
                )
            }

            descriptionDialog.button_cancel.setOnClickListener {
                descriptionAlertDialog.dismiss()
            }
        }


        money.setOnClickListener {
            val moneyDialog = LayoutInflater.from(this).inflate(R.layout.edit_money, null)
            val moneyDialogBuilder = AlertDialog.Builder(this)
                .setView(moneyDialog)
                .setTitle("Edit Description")

            val editGold = moneyDialog.findViewById<EditText>(R.id.edit_money_gold)
            val editSilver = moneyDialog.findViewById<EditText>(R.id.edit_money_silver)
            val editCopper = moneyDialog.findViewById<EditText>(R.id.edit_money_copper)
            val editPlatinum = moneyDialog.findViewById<EditText>(R.id.edit_money_platinum)
            val editElectrum = moneyDialog.findViewById<EditText>(R.id.edit_money_electrum)

            with(character.inventory) {
                editGold.setText(gold.toString())
                editSilver.setText(silver.toString())
                editCopper.setText(copper.toString())
                editPlatinum.setText(platinum.toString())
                editElectrum.setText(electrum.toString())
            }

            val moneyAlertDialog = moneyDialogBuilder.show()

            moneyDialog.button_ok.setOnClickListener {
                moneyAlertDialog.dismiss()

                with(character.inventory) {
                    gold = editGold.text.toString().toInt()
                    silver = editSilver.text.toString().toInt()
                    copper = editCopper.text.toString().toInt()
                    platinum = editPlatinum.text.toString().toInt()
                    electrum = editElectrum.text.toString().toInt()

                    characterGold.text = "Gold: $gold"
                    characterSilver.text = "Silver: $silver"
                    characterCopper.text = "Copper: $copper"
                    characterPlatinum.text = "Platinum: $platinum"
                    characterElectrum.text = "Electrum: $electrum"
                }
                editMoneyInDatabase(character.inventory)
            }

            moneyDialog.button_cancel.setOnClickListener {
                moneyAlertDialog.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        itemAdapter.notifyDataSetChanged()
    }

    private fun createItemRecyclerView(items: MutableList<Item>){
        val itemRecyclerView: RecyclerView = findViewById(R.id.rv_item)
        val layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(items)
        itemRecyclerView.layoutManager = layoutManager
        itemRecyclerView.adapter = itemAdapter
        deleteIcon = ContextCompat.getDrawable(this,
            R.drawable.ic_delete_white_40
        )!!

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
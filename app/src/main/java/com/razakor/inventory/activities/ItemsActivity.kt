package com.razakor.inventory.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.razakor.inventory.adapters.ItemsPagerAdapter
import com.razakor.inventory.database.*
import com.razakor.inventory.R
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.item_dialog.view.*

class ItemsActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ItemsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item)

        val position = intent.getIntExtra("position", -1)
        val inventoryId = intent.getIntExtra("inventory_id", 0)

        val items = characters.filter { it.inventory.id == inventoryId }[0].inventory.items

        viewPager = findViewById(R.id.viewPager)
        pagerAdapter =
            ItemsPagerAdapter(supportFragmentManager, items)
        viewPager.adapter = pagerAdapter
        viewPager.setCurrentItem(position, false)

        edit_button.setOnClickListener {
            val currentItem = viewPager.currentItem
            val item = items[currentItem]

            val itemDialog = LayoutInflater.from(this).inflate(R.layout.item_dialog, null)
            val itemDialogBuilder = AlertDialog.Builder(this)
                .setView(itemDialog)
                .setTitle("Edit Item")

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

            with(item) {
                itemDialog.edit_name.setText(name)
                spinnerType.setSelection(typeMap[type]!! - 1)
                spinnerRarity.setSelection(rarityMap[rarity]!! - 1)
                itemDialog.edit_price.setText(price.toString())
                itemDialog.edit_count.setText(count.toString())
                itemDialog.edit_description.setText(description)
            }

            itemDialog.btnOK.setOnClickListener {
                itemAlertDialog.dismiss()

                if (itemDialog.edit_name.text.toString() != "") {
                    with(item) {
                        name = itemDialog.edit_name.text.toString()
                        type = spinnerType.selectedItem.toString()
                        rarity = spinnerRarity.selectedItem.toString()
                        price = itemDialog.edit_price.text.toString().toInt()
                        count = itemDialog.edit_count.text.toString().toInt()
                        description = itemDialog.edit_description.text.toString()

                        pagerAdapter = ItemsPagerAdapter(
                            supportFragmentManager,
                            items
                        )
                        viewPager.adapter = pagerAdapter
                        viewPager.setCurrentItem(currentItem, false)
                    }

                    editItemInDatabase(
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
}
package com.razakor.inventory.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.razakor.inventory.database.*
import com.razakor.inventory.database.entities.Item
import com.razakor.inventory.activities.ItemsActivity
import com.razakor.inventory.R
import kotlinx.android.synthetic.main.item_dialog.view.*

class ItemAdapter(private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private lateinit var context: Context
    private lateinit var removedItem: Item
    private var removedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val layoutId: Int = R.layout.item_list_item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position].name, items[position].type, items[position].rarity, items[position].count)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ItemsActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("inventory_id", items[position].inventoryId)
            intent.putExtra("item_id", items[position].id)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            editItem(items[position])
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        removedPosition = viewHolder.adapterPosition
        removedItem = items[removedPosition]
        val count = removedItem.count

        if (count <= 1) {
            items.removeAt(viewHolder.adapterPosition)
            notifyItemRemoved(viewHolder.adapterPosition)
            deleteItemFromDatabase(removedItem.id)
        } else {
            removedItem.count--
            notifyDataSetChanged()
            itemCountChange(removedItem.id, "- 1")
        }

        Snackbar.make(viewHolder.itemView, "${removedItem.name} deleted.", Snackbar.LENGTH_LONG).setAction("UNDO") {
            if(count <= 1) {
                items.add(removedPosition, removedItem)
                notifyItemInserted(removedPosition)
                insertItemToDatabase(
                    removedItem,
                    rarityMap[removedItem.rarity]!!,
                    typeMap[removedItem.type]!!
                )
            } else {
                removedItem.count++
                notifyDataSetChanged()
                itemCountChange(removedItem.id, "+ 1")
            }
        }.show()
    }

    private fun editItem(item: Item) {
        val itemDialog = LayoutInflater.from(context).inflate(R.layout.item_dialog, null)
        val itemDialogBuilder = AlertDialog.Builder(context)
            .setView(itemDialog)
            .setTitle("Edit Item")

        val itemAlertDialog = itemDialogBuilder.show()



        val spinnerType: Spinner = itemDialog.findViewById(R.id.spinner_type)
        val typeAA = ArrayAdapter(context, android.R.layout.simple_spinner_item,
            typeArray
        )
        typeAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = typeAA

        val spinnerRarity: Spinner = itemDialog.findViewById(R.id.spinner_rarity)
        val rarityAA = ArrayAdapter(context, android.R.layout.simple_spinner_item,
            rarityArray
        )
        rarityAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRarity.adapter = rarityAA

        with(item) {
            itemDialog.edit_name.setText(name)
            spinnerType.setSelection(typeMap[type]!! - 1)
            spinnerRarity.setSelection(rarityMap[rarity]!! - 1)
            itemDialog.edit_price.setText(if (price != null) price.toString() else "")
            itemDialog.edit_count.setText(count.toString())
            itemDialog.edit_description.setText(description ?: "")
        }

        itemDialog.btnOK.setOnClickListener {
            itemAlertDialog.dismiss()
            notifyDataSetChanged()

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
                editItemInDatabase(
                    item,
                    rarityMap[item.rarity]!!,
                    typeMap[item.type]!!
                )
            } else {
                Toast.makeText(context, "Set correct name", Toast.LENGTH_LONG).show()
            }
        }

        itemDialog.btnCancel.setOnClickListener {
            itemAlertDialog.dismiss()
        }
    }




    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.item_name)
        private val itemType: TextView = itemView.findViewById(R.id.item_type)
        private val itemRarity: TextView = itemView.findViewById(R.id.item_rarity)
        private val itemCount: TextView = itemView.findViewById(R.id.item_count)

        fun bind(name: String, type: String, rarity: String, count: Int) {
            itemName.text = name
            itemType.text = type
            itemRarity.text = rarity
            if (count > 1) {
                itemCount.text = count.toString()
            } else {
                itemCount.text = ""
            }
        }
    }
}
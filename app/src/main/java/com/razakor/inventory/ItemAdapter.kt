package com.razakor.inventory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

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
/*
        holder.itemView.setOnLongClickListener {
            Toast.makeText(context, "Item deleted at position $position", Toast.LENGTH_LONG).show()
            deleteCharacterFromDatabase(db, characters[position])
            characters.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
        */
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
package com.razakor.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val layoutId: Int = R.layout.item_list_item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position].name, items[position].type, items[position].rarity, items[position].count)
/*
        holder.itemView.setOnClickListener {
            val intent = Intent(context, InventoryActivity::class.java)
            intent.putExtra("inventory_id", characters[position].inventory.id)
            context.startActivity(intent)
        }

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
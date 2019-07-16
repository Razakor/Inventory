package com.razakor.inventory

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter(_db: SQLiteDatabase, _characters: MutableList<Character>)
    : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val characters: MutableList<Character> = _characters
    private val db: SQLiteDatabase = _db
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        context = parent.context
        val layoutId: Int = R.layout.character_list_item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(layoutId, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position].name, characters[position].race, characters[position].clas, characters[position].lvl)

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
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var characterName: TextView = itemView.findViewById(R.id.character_name)
        private var characterRace: TextView = itemView.findViewById(R.id.character_race)
        private var characterClass: TextView = itemView.findViewById(R.id.character_class)
        private var characterLvl: TextView = itemView.findViewById(R.id.character_lvl)

        fun bind(_name: String, _race: String, _class: String, _lvl: Int) {
            characterName.text = _name
            characterRace.text = _race
            characterClass.text = _class
            characterLvl.text = _lvl.toString()
        }
    }
}
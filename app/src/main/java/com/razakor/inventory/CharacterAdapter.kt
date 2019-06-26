package com.razakor.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter(_character: Character, _count: Int)
    : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    var count: Int = _count
    var character: Character = _character

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val context: Context = parent.context
        val layoutId: Int = R.layout.character_list_item
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(layoutId, parent, false)
        val viewHolder = CharacterViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(character.name, character.race, character.clas, character.lvl)
    }

    override fun getItemCount(): Int {
        return count
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var character_name: TextView
        private var character_race: TextView
        private var character_class: TextView
        private var character_lvl: TextView

        init {
            character_name = itemView.findViewById(R.id.character_name)
            character_race = itemView.findViewById(R.id.character_race)
            character_class = itemView.findViewById(R.id.character_class)
            character_lvl = itemView.findViewById(R.id.character_lvl)
        }

        fun bind(_name: String, _race: String, _class: String, _lvl: Int) {
            character_name.text = _name
            character_race.text = _race
            character_class.text = _class
            character_lvl.text = _lvl.toString()
        }
    }
}
package com.razakor.inventory.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.razakor.inventory.database.characters
import com.razakor.inventory.database.entities.Item
import com.razakor.inventory.R

class ItemDetailsFragment : Fragment() {

    companion object {
        fun newInstance(item: Item): ItemDetailsFragment {
            val args = Bundle()
            args.putInt("id", item.id)
            args.putInt("inventoryId", item.inventoryId)

            val fragment = ItemDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_item_details, container, false)

        val nameTextView = view.findViewById<TextView>(R.id.item_details_name)
        val typeTextView = view.findViewById<TextView>(R.id.item_details_type)
        val rarityTextView = view.findViewById<TextView>(R.id.item_details_rarity)
        val priceTextView = view.findViewById<TextView>(R.id.item_details_price)
        val countTextView = view.findViewById<TextView>(R.id.item_details_count)
        val descriptionTextView = view.findViewById<TextView>(R.id.item_details_description)

        val args = arguments

        val inventoryId = args?.getInt("inventoryId")
        val itemId = args?.getInt("id")
        val inventory = characters.filter { it.inventory.id == inventoryId }[0].inventory
        val item = inventory.items.filter { it.id == itemId }[0]

        with(item) {
            nameTextView.text = name
            typeTextView.text = type
            rarityTextView.text = rarity
            countTextView.text = "Count: $count"
            if (price != null) {
                priceTextView.text = "Price: $price"
            } else {
                priceTextView.text = ""
            }
            if(description != null) {
                descriptionTextView.text = description
            } else {
                descriptionTextView.text = ""
            }
        }
        return view
    }
}
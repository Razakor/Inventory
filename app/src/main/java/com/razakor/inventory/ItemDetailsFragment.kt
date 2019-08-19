package com.razakor.inventory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ItemDetailsFragment : Fragment() {

    companion object {
        fun newInstance(item: Item): ItemDetailsFragment {
            val args = Bundle()
            args.putString("name", item.name)
            args.putString("type", item.type)
            args.putString("rarity", item.rarity)
            args.putInt("price", item.price)
            args.putInt("count", item.count)
            args.putString("description", item.description)

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

        nameTextView.text = args?.getString("name")
        typeTextView.text = args?.getString("type")
        rarityTextView.text = args?.getString("rarity")
        priceTextView.text = "Price: ${args?.getInt("price")}"
        countTextView.text = "Count: ${args?.getInt("count")}"
        descriptionTextView.text = args?.getString("description")

        return view
    }
}
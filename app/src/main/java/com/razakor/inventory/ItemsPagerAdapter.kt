package com.razakor.inventory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ItemsPagerAdapter (fragmentManager: FragmentManager, private val items: MutableList<Item>) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return ItemDetailsFragment.newInstance(items[position])
    }

    override fun getCount(): Int {
        return items.size
    }
}
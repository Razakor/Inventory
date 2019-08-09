package com.razakor.inventory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class ItemsActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ItemsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_item)

        val inventoryId = intent.getIntExtra("inventory_id", 0)

        val items = characters.filter { it.inventory.id == inventoryId }[0].inventory.items

        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = ItemsPagerAdapter(supportFragmentManager, items)
        viewPager.adapter = pagerAdapter






    }

}
package com.example.test_task_magents.activity.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.test_task_magents.R
import com.example.test_task_magents.adapter.PictureFragmentAdapter
import com.example.test_task_magents.util.workWith.initDatabase
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents(){
        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        CoroutineScope(Dispatchers.IO).launch {
            initDatabase(this@MainActivity.applicationContext)
        }

        viewPager2.adapter =
            PictureFragmentAdapter(
                supportFragmentManager,
                lifecycle
            )

        tabLayout.addTab(tabLayout.newTab().setText("Рандомные картинки"))
        tabLayout.addTab(tabLayout.newTab().setText("Любимые картинки"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) { viewPager2.currentItem = tab!!.position }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) { tabLayout.selectTab(tabLayout.getTabAt(position)) }
        })
    }

}
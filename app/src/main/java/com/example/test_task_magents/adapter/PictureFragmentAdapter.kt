package com.example.test_task_magents.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test_task_magents.activity.picture.fragment.favorite.FavoritePictureFragment
import com.example.test_task_magents.activity.picture.fragment.random.RandomPictureFragment

class PictureFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) RandomPictureFragment() else FavoritePictureFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}
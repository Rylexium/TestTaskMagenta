package com.example.test_task_magents.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.test_task_magents.FavoritePictureFragment;
import com.example.test_task_magents.RandomPictureFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new RandomPictureFragment() : new FavoritePictureFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
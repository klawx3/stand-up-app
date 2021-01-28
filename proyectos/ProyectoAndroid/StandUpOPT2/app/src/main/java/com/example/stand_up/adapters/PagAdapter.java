package com.example.stand_up.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.stand_up.fragments.InicioFragment;
import com.example.stand_up.fragments.infoFragment;

public class PagAdapter extends FragmentStateAdapter {

    public PagAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new InicioFragment();
            case 1:
                return new infoFragment();
            default:
                return new InicioFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


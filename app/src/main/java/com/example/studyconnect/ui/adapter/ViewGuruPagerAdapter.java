package com.example.studyconnect.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.studyconnect.R;
import com.example.studyconnect.ui.class_ui.assignment.tab_view_guru.PetunjukFragment;
import com.example.studyconnect.ui.class_ui.assignment.tab_view_guru.TugasSiswaFragment;

public class ViewGuruPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.text_petunjuk,
            R.string.text_tugas_siswa
    };
    private final Context mContext;

    public ViewGuruPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new TugasSiswaFragment();
        }
        return new PetunjukFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
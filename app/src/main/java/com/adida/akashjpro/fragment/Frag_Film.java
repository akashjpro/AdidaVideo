package com.adida.akashjpro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adida.akashjpro.livevideo.R;

/**
 * Created by Aka on 2/27/2017.
 */

public class Frag_Film extends Fragment {
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private static View mView;
    private static ViewPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.film, container, false);
        mTabLayout = (TabLayout) mView.findViewById(R.id.myTablayout);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager1);

        mPagerAdapter = new ViewPagerAdapter(getFragmentManager());

        mPagerAdapter.addFragment_Title(new FragmentFilm(), "Phim truyện");
        mPagerAdapter.addFragment_Title(new FragmentCartoon(), "Phim hoạt hình");

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);


        return mView;
    }
}

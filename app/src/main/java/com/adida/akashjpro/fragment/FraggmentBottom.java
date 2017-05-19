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

import static com.adida.akashjpro.livevideo.R.id.viewPager;

/**
 * Created by Aka on 12/11/2016.
 */

public class FraggmentBottom extends Fragment{

    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;
    private static View mView;
    private static ViewPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView  = inflater.inflate(R.layout.fragbottom, container, false);
        pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        mTabLayout = (TabLayout) mView.findViewById(R.id.myTablayout);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);

        pagerAdapter.addFragment_Title(new FragBottom_YoutubeTitle(), "*");
        pagerAdapter.addFragment_Title(new FragBottom_Chat(), "*");

        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);// keo chuyen tab


        return mView;
    }


   public static void onCreateAgain(){
       mTabLayout = (TabLayout) mView.findViewById(R.id.myTablayout);
       mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);

       pagerAdapter.addFragment_Title(new FragBottom_YoutubeTitle(), "*");
       pagerAdapter.addFragment_Title(new FragBottom_Chat(), "*");

       mViewPager.setAdapter(pagerAdapter);

       mTabLayout.setupWithViewPager(mViewPager);// keo chuyen tab
   }
}

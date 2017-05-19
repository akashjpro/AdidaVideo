package com.adida.akashjpro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Aka on 12/14/2016.
 */

public class FragmentActivityYoutube extends YouTubePlayerFragment {

    public YouTubePlayerView youTubePlayerView;

    public static FragmentActivityYoutube newInstance() {
        return new FragmentActivityYoutube();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        YouTubePlayerView view = (YouTubePlayerView) super.onCreateView(layoutInflater, viewGroup, bundle);
        youTubePlayerView = view;
        return view;
    }
}

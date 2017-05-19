package com.adida.akashjpro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adida.akashjpro.livevideo.R;

/**
 * Created by Aka on 12/9/2016.
 */

public class Fragment_PlayVideo extends android.support.v4.app.Fragment{
    private String idVideo;
    LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_play_video, container, false);
//
//        Bundle bundle = getArguments();
//        idVideo = bundle.getString("idVideo");
        //Toast.makeText(getActivity(), "id = "+ MainActivity.idVideo, Toast.LENGTH_SHORT).show();

        return view;
    }
}

package com.adida.akashjpro.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adida.akashjpro.livevideo.R;

/**
 * Created by Aka on 12/30/2016.
 */

public class FragBottom_Comment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bottom_comment, container, false);
        return view;
    }
}

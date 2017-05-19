package com.adida.akashjpro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.adapter.VideoFavoriteAdapter;
import com.adida.akashjpro.livevideo.R;
import com.google.firebase.database.ChildEventListener;

/**
 * Created by Aka on 12/18/2016.
 */

public class FragmentVideoFavorite extends Fragment {

    public static RecyclerView mRecyclerView;
    public static VideoFavoriteAdapter mVideoFavoriteAdapter;
    private ChildEventListener mChildEventListener;
//    public static RecyclerViewUtils.ShowHideToolbarOnScrollingListener showHideToolbarListener;
    Boolean finish = false;
    ProgressBar mPBrLoad;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            View view = inflater.inflate(R.layout.frag_video_favorite, container, false);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewVideoFavorite);
            mPBrLoad      = (ProgressBar) view.findViewById(R.id.prbVideoFavorite);


//        final StaggeredGridLayoutManager mGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            final GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setLayoutManager(mGridLayoutManager);

            //show & hide toolbar
//        mRecyclerView.addOnScrollListener(MainActivity.showHideToolbarListener = new RecyclerViewUtils.ShowHideToolbarOnScrollingListener(MainActivity.toolbar, getActivity()));

            mVideoFavoriteAdapter = new VideoFavoriteAdapter(getActivity(), MainActivity.mListVideo);
            mRecyclerView.setAdapter(mVideoFavoriteAdapter);



            return view;

    }
}

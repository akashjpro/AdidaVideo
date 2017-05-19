package com.adida.akashjpro.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adida.akashjpro.activity.utils.Read_Content_URL;
import com.adida.akashjpro.adapter.VideoFavoriteAdapter;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aka on 12/18/2016.
 */

public class FragmentFilm extends Fragment {

    RecyclerView mRecyclerView;
    VideoFavoriteAdapter mVideoFavoriteAdapter;
    ArrayList<Video> mListVideo;
    ProgressBar mPBrLoad;

    ArrayList<Video> mArrayVideo;
    GridLayoutManager mGridLayoutManager;

    boolean isLoad;


    public String LINK = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=";
    public String CHUOI_API_KEY = "&key=AIzaSyCax_paqrKloMoL2aYAZTcnpCnhdCWr5_E&maxResults=50";
    public String ID_PLAYLIST_PHIM = "PL8nGm1W2y9sY_fm4gBMvC7pGC7iiQXPIq";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_film, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewVideoFilm);
        mPBrLoad      = (ProgressBar) view.findViewById(R.id.prbVideoFavorite);
        mListVideo    = new ArrayList<>();
        mArrayVideo   = new ArrayList<>();

        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);

//        mRecyclerView.addOnScrollListener(MainActivity.showHideToolbarListener = new RecyclerViewUtils.ShowHideToolbarOnScrollingListener(MainActivity.toolbar, getActivity()));


        mVideoFavoriteAdapter = new VideoFavoriteAdapter(getActivity(), mArrayVideo);
        mRecyclerView.setAdapter(mVideoFavoriteAdapter);

        GET_JSON_FILM get_json_film = new GET_JSON_FILM();
        get_json_film.execute(LINK + ID_PLAYLIST_PHIM + CHUOI_API_KEY);

        return view;
    }

    public  class GET_JSON_FILM extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return Read_Content_URL.docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonItems = object.getJSONArray("items");
                String url          = "";
                String title        = "";
                String videoId      = "";
                String channelTitle = "";
                int indexVideo = 0;
                for (int i=jsonItems.length()-1; i>=0; i--){
                    JSONObject objectItem = jsonItems.getJSONObject(i);
                    JSONObject jsonSnippet = objectItem.getJSONObject("snippet");
                    //Get title
                    title = jsonSnippet.getString("title");
                    if(!title.equals("Deleted video")){
                        //Get url
                        JSONObject objectThumbnails = jsonSnippet.getJSONObject("thumbnails");
                        JSONObject objectDefault = objectThumbnails.getJSONObject("default");
                        url = objectDefault.getString("url");
                        //Get videoID
                        JSONObject objectResourceId = jsonSnippet.getJSONObject("resourceId");
                        videoId = objectResourceId.getString("videoId");
                        //Get channelTitle
                        channelTitle = jsonSnippet.getString("channelTitle");
                        mListVideo.add(new Video(title, url, videoId));
                        indexVideo++;
                    }
                }

                final int size =mListVideo.size();
                if(size <=10 ){
                    mArrayVideo.addAll(mListVideo);
                }else {
                    for (int i=0; i< 10; i++){
                        mArrayVideo.add(mListVideo.get(i));
                    }
                }
                mVideoFavoriteAdapter.notifyDataSetChanged();

                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);


                        int visibleItemcount = mGridLayoutManager.getChildCount();
                        int totalItemCount   = mGridLayoutManager.getItemCount();
                        final int pastVisibleItemCount = mGridLayoutManager.findFirstVisibleItemPosition();

                        if(!isLoad & (visibleItemcount + pastVisibleItemCount>= totalItemCount)) {
                            if (mArrayVideo.size() < size) {

                                mArrayVideo.add(null);
                                mVideoFavoriteAdapter.notifyItemInserted(mArrayVideo.size() - 1);
                                // add item null -> loadding
                                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        if (position == mArrayVideo.size() - 1)
                                            return 2;
                                        else
                                            return 1;
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //remove item loadding
                                        mArrayVideo.remove(mArrayVideo.size() - 1);
                                        mVideoFavoriteAdapter.notifyItemRemoved(mArrayVideo.size() - 1);


                                        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                            @Override
                                            public int getSpanSize(int position) {
                                                return 1;
                                            }
                                        });
                                        int size1 = mArrayVideo.size();


                                        //add data
                                        if (size - size1 <= 10) {
                                            for (int i = size1; i < size; i++) {
                                                mArrayVideo.add(mListVideo.get(i));
                                            }
                                            mVideoFavoriteAdapter.notifyDataSetChanged();
                                        } else {
                                            for (int i = size1; i < size1 + 10; i++) {
                                                mArrayVideo.add(mListVideo.get(i));
                                            }
                                            mVideoFavoriteAdapter.notifyDataSetChanged();
                                        }

                                        isLoad = false;
                                    }
                                }, 1000);

                                isLoad = true;
                            }

                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

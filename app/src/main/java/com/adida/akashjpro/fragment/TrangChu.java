package com.adida.akashjpro.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.activity.utils.NoInternet;
import com.adida.akashjpro.adapter.SnapAdapter;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Snap;
import com.adida.akashjpro.model.Video;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akashjpro on 11/20/2016.
 */

public class TrangChu extends Fragment {
    int i =0;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    public static String API_KEY =  "AIzaSyCax_paqrKloMoL2aYAZTcnpCnhdCWr5_E";
    public String LINK = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=";
    public String CHUOI_API_KEY = "&key=AIzaSyCax_paqrKloMoL2aYAZTcnpCnhdCWr5_E&maxResults=50";


//    public String ID_PLAYLIST_VIDEO_NOI_BAT = "PLO_Qu-UF4QR9WqfL96NJY4Kz4lfuo_SaE";
//    public String ID_PLAYLIST_CA_NHAC = "PLO_Qu-UF4QR8UxbV_t2vIqsbBQs__nUBy";
//    public String ID_PLAYLIST_PHIM = "PLO_Qu-UF4QR98sZ2piyF6cke5LRaZzi1U";
//    public String ID_PLAYLIST_SACH_NOI = "PLO_Qu-UF4QR-XEedcyvrjQhP6NLyGhn41";


    public String ID_PLAYLIST_VIDEO_NOI_BAT = "PL8nGm1W2y9sa44u9KjVgZnaB8x7JQPsoA";
    public String ID_PLAYLIST_CA_NHAC = "PL8nGm1W2y9sZN0tw07LgbhG-IItRHzsSX";
    public String ID_PLAYLIST_PHIM = "PL8nGm1W2y9sY_fm4gBMvC7pGC7iiQXPIq";
    public String ID_PLAYLIST_SACH_NOI = "PL8nGm1W2y9sZAGeI_KghD6Axk7LOPCgBs";



    private static ProgressBar prbLoadData;
    private static ImageView   ic_App;
    private static TextView txtNamMo, txtNoInternet;
    private static TextView txtHoiHuong;


    //String LINK = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ PLAYLIST_ID +"&key="+ API_KEY +"&maxResults=50";
    List<Video> videosPhim, videoList, videosCaNhac, videosPhatGiao, videosBongDa;
    List<String> tenVideo;
    SnapAdapter snapAdapter;
    SnapAdapter snapAdapterCaNhac;
    SnapAdapter snapAdapterPhatGiao;
    SnapAdapter snapAdapterBongDa;
    String titlePhim, titleCaNhac, titlePhatGiao, titleBongDa;

    public  static boolean isLoad = false;
    public  static boolean isfullSreen = false;

    private Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final FragmentLoad fragmentLoad = new FragmentLoad();
//        fragmentLoad.show(getFragmentManager(), "fragLoad");

//        final Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(isLoad){
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            fragmentLoad.dismiss();
//                        }
//                    },600);
//                }
//                handler1.postDelayed(this, 0);
//            }
//        },0);

        KhoiTao();



//        loadTenFilm();
        View view = inflater.inflate(R.layout.frag_trang_chu, container, false);
        prbLoadData     = (ProgressBar) view.findViewById(R.id.prdLoadData);
        ic_App          = (ImageView) view.findViewById(R.id.ic_App);
        txtNamMo        = (TextView) view.findViewById(R.id.nammo);
        txtHoiHuong     = (TextView) view.findViewById(R.id.txtHoiHuong);
        txtNoInternet   = (TextView) view.findViewById(R.id.txtNoInternet);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        txtNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prbLoadData.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), NoInternet.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        //Show and hide Toolbar
//        recyclerView.addOnScrollListener(MainActivity.showHideToolbarListener = new RecyclerViewUtils.ShowHideToolbarOnScrollingListener(MainActivity.toolbar, getActivity()));

//        if (savedInstanceState != null) {
//            showHideToolbarListener.onRestoreInstanceState((RecyclerViewUtils.ShowHideToolbarOnScrollingListener.State) savedInstanceState
//                    .getParcelable(RecyclerViewUtils.ShowHideToolbarOnScrollingListener.SHOW_HIDE_TOOLBAR_LISTENER_STATE));
//        }

        GET_JSON_PHIM_LE get_json_youtube = new GET_JSON_PHIM_LE();
        get_json_youtube.execute(LINK + ID_PLAYLIST_VIDEO_NOI_BAT + CHUOI_API_KEY);

        GET_JSON_CA_NHAC get_json_ca_nhac = new GET_JSON_CA_NHAC();
        get_json_ca_nhac.execute(LINK + ID_PLAYLIST_CA_NHAC + CHUOI_API_KEY);

        GET_JSON_PHAT_GIAO get_json_phat_giao = new GET_JSON_PHAT_GIAO();
        get_json_phat_giao.execute(LINK + ID_PLAYLIST_PHIM + CHUOI_API_KEY);

        GET_JSON_BONG_DA get_json_bong_da = new GET_JSON_BONG_DA();
        get_json_bong_da.execute(LINK + ID_PLAYLIST_SACH_NOI + CHUOI_API_KEY);

        setupAdapter();




        return view;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putParcelable(RecyclerViewUtils.ShowHideToolbarOnScrollingListener.SHOW_HIDE_TOOLBAR_LISTENER_STATE,
//                showHideToolbarListener.onSaveInstanceState());
//        super.onSaveInstanceState(outState);
//    }

    private void KhoiTao() {
        titlePhim       = "Video nổi bật";
        titleCaNhac     = "Ca nhạc Phật Giáo";
        titlePhatGiao   = "Phim Phật Giáo";
        titleBongDa     = "Sách nói Phật Giáo";

        videosPhim          = new ArrayList<>();
        videoList       = new ArrayList<>();
        tenVideo        = new ArrayList<>();
        videosCaNhac    = new ArrayList<>();
        videosPhatGiao  = new ArrayList<>();
        videosBongDa    = new ArrayList<>();

        snapAdapterCaNhac   = new SnapAdapter(getActivity());
        snapAdapter         = new SnapAdapter(getActivity());
        snapAdapterPhatGiao = new SnapAdapter(getActivity());
        snapAdapterBongDa   = new SnapAdapter(getActivity());


        FirebaseDatabase database = FirebaseDatabase.getInstance();

       // mDatabase = FirebaseDatabase.getInstance().getReference();
    }



//    private void loadTenFilm() {
//        mDatabase.child("film").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                String film = dataSnapshot.getValue().toString();
////                tenVideo.add(film);
////                videoList.add(new Video(tenVideo.get(i), videosPhim.get(i).getImage()));
////                snapAdapter.notifyItemChanged(i);
////                i++;
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    private void setupAdapter() {
        snapAdapter.addSnap(new Snap(Gravity.START, titlePhim, videosPhim));
        snapAdapter.addSnap(new Snap(Gravity.START, titleCaNhac, videosCaNhac));
        snapAdapter.addSnap(new Snap(Gravity.START, titlePhatGiao, videosPhatGiao));
        snapAdapter.addSnap(new Snap(Gravity.START, titleBongDa, videosBongDa));
        recyclerView.setAdapter(snapAdapter);


    }
    private class GET_JSON_PHIM_LE extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
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
                        videosPhim.add(new Video(title, url, videoId));
                        snapAdapter.notifyItemChanged(indexVideo);
                        indexVideo++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GET_JSON_CA_NHAC extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
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
                        videosCaNhac.add(new Video(title, url, videoId));
                        snapAdapter.notifyItemChanged(indexVideo);
                        indexVideo++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GET_JSON_PHAT_GIAO extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
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
                        videosPhatGiao.add(new Video(title, url, videoId));
                        snapAdapter.notifyItemChanged(indexVideo);
                        indexVideo++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class GET_JSON_BONG_DA extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
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
                        videosBongDa.add(new Video(title, url, videoId));
                        snapAdapter.notifyItemChanged(indexVideo);
                        indexVideo++;
                    }

                    isLoad = true;

                    prbLoadData.setVisibility(View.GONE);
                    ic_App.setVisibility(View.GONE);
                    txtHoiHuong.setVisibility(View.GONE);
                    txtNamMo.setVisibility(View.GONE);
                    MainActivity.toolbar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    public static void CheckInternet(){
        prbLoadData.setVisibility(View.GONE);
        ic_App.setVisibility(View.GONE);
        txtHoiHuong.setVisibility(View.GONE);
        txtNamMo.setVisibility(View.GONE);
        txtNoInternet.setVisibility(View.VISIBLE);
    }

    public static void hideAlertInternet(){
        txtNoInternet.setVisibility(View.GONE);
    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           //isfullSreen = true;

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            //Toast.makeText(getActivity(), "Da chuyen PORTRAIT ", Toast.LENGTH_SHORT).show();

        }

    }

}

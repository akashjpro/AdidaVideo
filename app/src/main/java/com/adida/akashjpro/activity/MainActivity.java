package com.adida.akashjpro.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.activity.utils.ShowHideProgessDialog;
import com.adida.akashjpro.draggablepanel.DraggableListener;
import com.adida.akashjpro.draggablepanel.DraggablePanel;
import com.adida.akashjpro.fragment.FragBottom_YoutubeTitle;
import com.adida.akashjpro.fragment.Frag_Film;
import com.adida.akashjpro.fragment.FraggmentBottom;
import com.adida.akashjpro.fragment.FragmentAudioBook;
import com.adida.akashjpro.fragment.FragmentLesson;
import com.adida.akashjpro.fragment.FragmentMusic;
import com.adida.akashjpro.fragment.FragmentVideoFavorite;
import com.adida.akashjpro.fragment.Fragment_internet;
import com.adida.akashjpro.fragment.TrangChu;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Chat;
import com.adida.akashjpro.model.Video;
import com.adida.akashjpro.model.VideoFavorite;
import com.adida.akashjpro.showhidetoolbar.RecyclerViewUtils;
import com.facebook.share.widget.ShareDialog;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.adida.akashjpro.fragment.FragmentVideoFavorite.mVideoFavoriteAdapter;
//import com.github.pedrovgs.DraggablePanel;


public class MainActivity extends AppCompatActivity{

    public static Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView imgUser;
    private ImageView mImageCover;
    private TextView  txtUser;
    DrawerLayout drawerLayout;
    static public DraggablePanel draggablePanel;
    static public YouTubePlayer youtubePlayer;
    static public YouTubePlayerFragment youtubeFragment;

    static public FraggmentBottom fraggmentBottom;
    static public FragBottom_YoutubeTitle mFragBottom_youtubeTitle;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;



    static public Video video;
    boolean isVideoError = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser mUser;
    public static long mIdVideoFavorite;

    public static ShareDialog shareDialog;






    public static DatabaseReference mDatabase;
    private boolean isView = true;
    // Count function onDetroy FragmentLoad
    public static boolean isLoad = true;
    public static boolean isLogin = false;
    public static FragmentManager mfragmentManager;

    public static String gender   = "";
    public static String birthday = "";
    public static String location = "";

    public static boolean isLoginFirst = false;



    private static final String YOUTUBE_API_KEY = "AIzaSyC1rMU-mkhoyTvBIdTnYU0dss0tU9vtK48";
    private static final String VIDEO_KEY = "gsjtg7m1MMM";
    public static String mUrlCoverPhoto = "https://firebasestorage.googleapis.com/v0/b/adida-video.appspot.com/o/background_profile.jpg?alt=media&token=7bbc8bac-f0e4-4d0a-b4b9-10b62f0a58ee";

    boolean isTop  = false;
    public static int indexFragment = 0;
    public static Frag_Film fragmentFilm;
    public static FragmentMusic fragmentMusic;
    public static FragmentVideoFavorite fragmentVideoFavorite;
    public static FragmentAudioBook fragmentAudioBook;
    public static FragmentLesson fragmentLesson;
    public static TrangChu trangChu;


    public static boolean isProgress = false;
    public static boolean isOneListener = true;
    public static Map<String, ArrayList<Chat>> mMapListChat;

    public static ArrayList<Integer> ArrayColor = new ArrayList<>();
    public static  ArrayList<String> mArrayID;

    public static ChildEventListener mChildEventListenerVideoFavorite;
    public static RecyclerViewUtils.ShowHideToolbarOnScrollingListener showHideToolbarListener;

    public static ArrayList<Video> mListVideo = new ArrayList<>();


    public static String name;


    BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment_internet fragment_internet = new Fragment_internet();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo() != null) {
               TrangChu.hideAlertInternet();
             }
            else {
                TrangChu.CheckInternet();
                Toast.makeText(context, "Không có internet", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        if(isProgress){
           ShowHideProgessDialog.showHideProgessDialog(MainActivity.this);
        }
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetReceiver, intentFilter);
        mAuth.addAuthStateListener(mAuthStateListener);

        super.onResume();
    }

    @Override
    protected void onStart() {
        mArrayID      = new ArrayList<>();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
//                FirebaseAuth.getInstance().signOut();


                if (mUser != null) {
                    mDatabase.child("User").child(mUser.getUid()).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue().toString();
                            if(url != null && !url.isEmpty()) {
                                txtUser.setText(dataSnapshot.getValue().toString());
                                MainActivity.name = dataSnapshot.getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    mDatabase.child("User").child(mUser.getUid()).child("profilePic").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue().toString();
                            if(url != null && !url.isEmpty()){
                                Picasso.with(MainActivity.this).load(url)
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.loading_profile)
                                        .error(R.drawable.no_image)
                                        .into(imgUser);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("User").child(mUser.getUid()).child("coverPhoto").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue().toString();
                            if(url != null && !url.isEmpty()){
                                Picasso.with(MainActivity.this).load(url)
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.load_cover)
                                        .error(R.drawable.no_video)
                                        .into(mImageCover);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    if(mChildEventListenerVideoFavorite !=null){
                        MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid()).removeEventListener(mChildEventListenerVideoFavorite);
                    }
                    mListVideo.clear();
                    mChildEventListenerVideoFavorite = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot != null) {
                                VideoFavorite videoFavorite = dataSnapshot.getValue(VideoFavorite.class);

//                    Toast.makeText(getActivity(), dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();
                                String id = dataSnapshot.getKey().toString();
                                String name = videoFavorite.getName();
                                String url = videoFavorite.getUrl();

                                mListVideo.add(new Video(name, url, id));
                                if(mVideoFavoriteAdapter != null){
                                    FragmentVideoFavorite.mVideoFavoriteAdapter
                                            .notifyItemInserted(mListVideo.size()-1);
                                    FragmentVideoFavorite.mRecyclerView.smoothScrollToPosition(
                                            FragmentVideoFavorite
                                                    .mVideoFavoriteAdapter.getItemCount());
                                }

                            }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid()).orderByChild("id").addChildEventListener(mChildEventListenerVideoFavorite);

                    if(isProgress){
                        ShowHideProgessDialog.hideProgessDialog();
                        isProgress = false;
                    }

                    mDatabase.child("idVideoFavorite").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null) {
                                mIdVideoFavorite = Long.parseLong(dataSnapshot.getValue().toString());
                            }else {
                                mDatabase.child("idVideoFavorite").child(mUser.getUid()).setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }else {

                    mListVideo.clear();
                    if(FragmentVideoFavorite.mVideoFavoriteAdapter != null){
                        FragmentVideoFavorite.mVideoFavoriteAdapter.notifyDataSetChanged();
                    }
                    txtUser.setText("Đăng nhập");
                    imgUser.setImageResource(R.mipmap.ic_user);
                    mImageCover.setImageResource(R.drawable.background_profile);
                }
            }
        };
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (internetReceiver != null){
            unregisterReceiver(internetReceiver);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        if(youtubeFragment != null) {
            youtubeFragment.onPause();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        addControls();
        shareDialog = new ShareDialog(this);





        mFragBottom_youtubeTitle = new FragBottom_YoutubeTitle();

        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            isLoginFirst = true;
        }

        /**
         * push current date to firebase
         */


        listenerControls();




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


        mfragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.linearLayout, new TrangChu(),"frag_TrangChu");
        fragmentTransaction.setCustomAnimations(R.animator.left_in, R.animator.right_out);
       // fragmentTransaction.addToBackStack("fragmentTrangChu");
        fragmentTransaction.commit();


        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLUE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// mui ten
        toolbar.setNavigationIcon(R.drawable.menu);//set icon
        navigationView.setItemIconTintList(null);// cho icon ve mau goc

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        navigationView.setCheckedItem(R.id.menuTrangChu);

        toolbar.setVisibility(View.GONE);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Trang Chủ");
                toolbar.setTitleTextColor(Color.WHITE);
            }
        },100);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
//                String timeCurrent = df.format(calendar.getTime());
//                mDatabase.child("TimeCurrent").setValue(timeCurrent);
//                handler.postDelayed(this, 1000);
//            }
//        },0);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.left_in, R.animator.right_out);
                trangChu = (TrangChu) getFragmentManager().findFragmentByTag("frag_TrangChu");
                fragmentFilm = (Frag_Film) getFragmentManager().findFragmentByTag("frag_Film");
                fragmentMusic = (FragmentMusic) getFragmentManager().findFragmentByTag("frag_Music");
                fragmentVideoFavorite = (FragmentVideoFavorite) getFragmentManager().findFragmentByTag("frag_VideoFavorite");
                fragmentAudioBook = (FragmentAudioBook) getFragmentManager().findFragmentByTag("frag_AudioBook");
                fragmentLesson = (FragmentLesson) getFragmentManager().findFragmentByTag("frag_Lesson");

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menuTrangChu :
                        if(indexFragment != 0){
                            toolbar.setBackgroundColor(Color.rgb(6, 63, 249));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(trangChu != null){
                                fragmentTransaction.show(trangChu);
                            }
                            else {
                                fragment = new TrangChu();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_TrangChu");
                            }
                            toolbar.setTitle("Trang chủ");
                        }
                        indexFragment = 0;
                        break;

                    case R.id.menuPhim :
                        if(indexFragment != 1){
                            toolbar.setBackgroundColor(Color.rgb(255, 0, 202));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(fragmentFilm != null){
                                fragmentTransaction.show(fragmentFilm);

                            }else {
                                fragment = new Frag_Film();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Film");
                            }
                            toolbar.setTitle("Phim");
                        }
                        indexFragment = 1;
                        break;

                    case R.id.menuCaNhac :
                        if(indexFragment != 2){
                            toolbar.setBackgroundColor(Color.rgb(155, 89, 182));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(fragmentMusic != null){
                                fragmentTransaction.show(fragmentMusic);

                            }else {
                                fragment = new FragmentMusic();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Music");
                            }
                            toolbar.setTitle("Ca nhạc");
                        }
                        indexFragment = 2;
                        break;

                    case R.id.menuBaiGiang :
                        if(indexFragment != 3){
                            toolbar.setBackgroundColor(Color.rgb(39, 174, 96));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(fragmentLesson != null){
                                fragmentTransaction.show(fragmentLesson);

                            }else {
                                fragment = new FragmentLesson();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Lesson");
                            }
                            toolbar.setTitle("Bài giảng");
                        }
                        indexFragment = 3;
                        break;


                    case R.id.menuSachNoi :
                        if(indexFragment != 4){
                            toolbar.setBackgroundColor(Color.rgb(250, 240, 3));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(fragmentAudioBook != null){
                                fragmentTransaction.show(fragmentAudioBook);

                            }else {
                                fragment = new FragmentAudioBook();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_AudioBook");
                            }
                            toolbar.setTitle("Sách nói");
                        }
                        indexFragment = 4;
                        break;

                    case R.id.menuVideoYeuThich :
                        if(indexFragment != 5){
                            toolbar.setBackgroundColor(Color.rgb(255, 1, 1));
                            hideFragment(indexFragment, fragmentTransaction);
                            if(fragmentVideoFavorite != null){
                                fragmentTransaction.show(fragmentVideoFavorite);
                            }else {
                                fragment = new FragmentVideoFavorite();
                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_VideoFavorite");
                            }
                            toolbar.setTitle("Video yêu thích");
                        }
                        indexFragment = 5;
                        break;
                    case R.id.menuFeedback:
                        Intent intent = new Intent(MainActivity.this, Feedback.class);
                        startActivity(intent);
                        break;
                    case R.id.menuAbout:
                        Intent intent1 = new Intent(MainActivity.this, About.class);
                        startActivity(intent1);
                        break;
                }


//                switch (item.getItemId()){
//                    case R.id.menuTrangChu :
//                        if(indexFragment != 0){
//                            toolbar.setBackgroundColor(Color.rgb(6, 63, 249));
//
//                            if(fragmentVideoFavorite != null){
//                                fragmentTransaction.remove(fragmentVideoFavorite);
//                            }
//
//                            if(fragmentMusic != null){
//                                fragmentTransaction.remove(fragmentMusic);
//                            }
//
//                            if(fragmentLesson != null){
//                                fragmentTransaction.remove(fragmentLesson);
//                            }
//
//                            if(fragmentFilm != null){
//                                fragmentTransaction.remove(fragmentFilm);
//                            }
//
//                            if(fragmentAudioBook != null){
//                                fragmentTransaction.remove(fragmentAudioBook);
//                            }
//
//                            if(trangChu != null){
//                                fragmentTransaction.show(trangChu);
//                            }
//                            else {
//                                fragment = new TrangChu();
//                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_TrangChu");
//                            }
//                            toolbar.setTitle("Trang chủ");
//                        }
//                        indexFragment = 0;
//                        break;
//
//                    case R.id.menuPhim :
//                        if(indexFragment == 0) {
//                            hideFragment(0, fragmentTransaction);
//                        }
//                        toolbar.setBackgroundColor(Color.rgb(255, 0, 202));
//                        fragment = new FragmentFilm();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Film");
//                        toolbar.setTitle("Phim");
//
//                        indexFragment = 1;
//                        break;
//
//                    case R.id.menuCaNhac :
//                        if(indexFragment == 0){
//                            hideFragment(0, fragmentTransaction);
//                        }
//                        toolbar.setBackgroundColor(Color.rgb(155, 89, 182));
//
//                        fragment = new FragmentMusic();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Music");
//
//                        toolbar.setTitle("Ca nhạc");
//
//                        indexFragment = 2;
//                        break;
//
//                    case R.id.menuBaiGiang :
//                        if(indexFragment == 0){
//                            hideFragment(0, fragmentTransaction);
//                        }
//                        toolbar.setBackgroundColor(Color.rgb(39, 174, 96));
//
//                        fragment = new FragmentLesson();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Lesson");
//
//                        toolbar.setTitle("Bài giảng");
//
//                        indexFragment = 3;
//                        break;
//
//
//                    case R.id.menuSachNoi :
//                        if(indexFragment == 0){
//                            hideFragment(0, fragmentTransaction);
//                        }
//                        toolbar.setBackgroundColor(Color.rgb(250, 240, 3));
//
//                        fragment = new FragmentAudioBook();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_AudioBook");
//
//                        toolbar.setTitle("Sách nói");
//
//                        indexFragment = 4;
//                        break;
//
//                    case R.id.menuVideoYeuThich :
//                        if(indexFragment == 0){
//                            hideFragment(0, fragmentTransaction);
//                        }
//                        toolbar.setBackgroundColor(Color.rgb(255, 1, 1));
//                        fragment = new FragmentVideoFavorite();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_VideoFavorite");
//
//                        toolbar.setTitle("Video yêu thích");
//                        indexFragment = 5;
//                        break;
//                    case R.id.menuEmail:
//                        break;
//                    case R.id.menuPhone:
//                        break;
//                }
//

//                switch (item.getItemId()){
//                    case R.id.menuTrangChu :
//                        if(indexFragment != 0){
//                            toolbar.setBackgroundColor(Color.rgb(6, 63, 249));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            if(trangChu != null){
//                                fragmentTransaction.show(trangChu);
//                            }
//                            else {
//                                fragment = new TrangChu();
//                                fragmentTransaction.add(R.id.linearLayout, fragment, "frag_TrangChu");
//                            }
//                            toolbar.setTitle("Trang chủ");
//                        }
//                        indexFragment = 0;
//                        break;
//
//                    case R.id.menuPhim :
//                        if(indexFragment != 1){
//                            toolbar.setBackgroundColor(Color.rgb(255, 0, 202));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            toolbar.setTitle("Phim");
//                        }else {
//                            fragmentTransaction.remove(fragmentFilm);
//                        }
//                        fragment = new FragmentFilm();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Film");
//                        indexFragment = 1;
//                        break;
//
//                    case R.id.menuCaNhac :
//                        if(indexFragment != 2){
//                            toolbar.setBackgroundColor(Color.rgb(155, 89, 182));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            toolbar.setTitle("Ca nhạc");
//                        }else {
//                            fragmentTransaction.remove(fragmentMusic);
//                        }
//                        fragment = new FragmentMusic();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Music");
//                        indexFragment = 2;
//                        break;
//
//                    case R.id.menuBaiGiang :
//                        if(indexFragment != 3) {
//                            toolbar.setBackgroundColor(Color.rgb(39, 174, 96));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            toolbar.setTitle("Bài giảng");
//                        }else {
//                            fragmentTransaction.remove(fragmentLesson);
//                          }
//                        fragment = new FragmentLesson();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_Lesson");
//                        indexFragment = 3;
//                        break;
//
//
//                    case R.id.menuSachNoi :
//                        if(indexFragment != 4){
//                            toolbar.setBackgroundColor(Color.rgb(250, 240, 3));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            toolbar.setTitle("Sách nói");
//                        }else {
//                            fragmentTransaction.remove(fragmentAudioBook);
//                        }
//
//                        fragment = new FragmentAudioBook();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_AudioBook");
//                        indexFragment = 4;
//                        break;
//
//                    case R.id.menuVideoYeuThich :
//                        if(indexFragment != 5){
//                            toolbar.setBackgroundColor(Color.rgb(255, 1, 1));
//                            hideFragment(indexFragment, fragmentTransaction);
//                            toolbar.setTitle("Video yêu thích");
//                        }else {
//                            fragmentTransaction.remove(fragmentVideoFavorite);
//                        }
//
//                        fragment = new FragmentVideoFavorite();
//                        fragmentTransaction.add(R.id.linearLayout, fragment, "frag_VideoFavorite");
//                        indexFragment = 5;
//                        break;
//                    case R.id.menuEmail:
//                        break;
//                    case R.id.menuPhone:
//                        break;
//                }

                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        initializeYoutubeFragment();
        hookDraggablePanelListeners();
        initializeDraggablePanel();

    }




    public void hideFragment(int i, FragmentTransaction fragmentTransaction){
        switch (i){
            case 0:
                fragmentTransaction.hide(trangChu);
                break;
            case 1:
                fragmentTransaction.hide(fragmentFilm);
                break;
            case 2:
                fragmentTransaction.hide(fragmentMusic);
                break;
            case 3:
                fragmentTransaction.hide(fragmentLesson);
                break;
            case 4:
                fragmentTransaction.hide(fragmentAudioBook);
                break;
            case 5:
                fragmentTransaction.remove(fragmentVideoFavorite);
                break;
        }
    }




    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean connected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return connected;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addControls() {

        video              = new Video();
        toolbar            = (Toolbar) findViewById(R.id.toolBar);
        navigationView     = (NavigationView) findViewById(R.id.myNavigationView);
        drawerLayout       = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        draggablePanel     = (DraggablePanel) findViewById(R.id.draggable_panel);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        /**
         * Khoi tao mang color:
         */
        ArrayColor = new ArrayList<>();
        ArrayColor.add(0);
        ArrayColor.add(0);
        ArrayColor.add(0);

        // get headerView have two solution this below
        //solution 1
//        View headerView    = navigationView.inflateHeaderView(R.layout.header_layout);
//        imgUser            = (ImageView) headerView.findViewById(R.id.imageViewUser);
        //solution 2, getHeaderView(index) default index: 0
        imgUser     = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageViewUser);
        mImageCover = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.background_profile);
        txtUser     = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewUser);


    }

    private void listenerControls() {

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser != null){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    ShowHideProgessDialog.showHideProgessDialog(MainActivity.this);

                }else {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.scale_zoom, R.anim.normal);
                    drawerLayout.closeDrawer(GravityCompat.START);

//                    FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.animator.left_in, R.animator.right_out);
//                    fragmentTransaction.add(R.id.linearLayout, new FragmentLogin(),"frag_login");
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                    fragmentTransaction.commit();
//                    showHideToolbarListener.toolbarAnimateHide();
//                    isLogin = true;
//                    FragmentTransaction fragmentTransaction1 = mfragmentManager.beginTransaction();
//                    trangChu = (TrangChu) getFragmentManager().findFragmentByTag("frag_TrangChu");
//                    fragmentFilm = (FragmentFilm) getFragmentManager().findFragmentByTag("frag_Film");
//                    fragmentMusic = (FragmentMusic) getFragmentManager().findFragmentByTag("frag_Music");
//                    fragmentVideoFavorite = (FragmentVideoFavorite) getFragmentManager().findFragmentByTag("frag_VideoFavorite");
//                    fragmentAudioBook = (FragmentAudioBook) getFragmentManager().findFragmentByTag("frag_AudioBook");
//                    fragmentLesson = (FragmentLesson) getFragmentManager().findFragmentByTag("frag_Lesson");
//                    hideFragment(indexFragment, fragmentTransaction1);
//                    fragmentTransaction1.commit();
                }




            }
        });

        txtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser != null){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    ShowHideProgessDialog.showHideProgessDialog(MainActivity.this);

                }else {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.scale_zoom, R.anim.normal);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                
            }
        });
    }



    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "onBackPressed ", Toast.LENGTH_SHORT).show();
        if(isTop){
            draggablePanel.setBottomFrag();
            return;
        }

//
//        if (mfragmentManager.getBackStackEntryCount()>0){
//            mfragmentManager.popBackStack();
//        }else {
//            super.onBackPressed();
//        }
        //Toast.makeText(this, "xong roi ", Toast.LENGTH_SHORT).show();
        finish();
       super.onBackPressed();
    }

    private void initializeDraggablePanel() {
        draggablePanel.setFragmentManager(getFragmentManager());
        draggablePanel.setTopFragment(youtubeFragment);
        fraggmentBottom = new FraggmentBottom();
        draggablePanel.setBottomFragment(fraggmentBottom);
        draggablePanel.initializeView();
        draggablePanel.setVisibility(View.INVISIBLE);
    }
    private void hookDraggablePanelListeners() {
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override public void onMaximized() {
                if(youtubePlayer != null) {
                    playVideo();
                }
                //Toast.makeText(MainActivity.this, "MaxSize", Toast.LENGTH_SHORT).show();
                isTop = true;
            }

            @Override public void onMinimized() {
                //Toast.makeText(MainActivity.this, "onMinimized", Toast.LENGTH_SHORT).show();
                isTop = false;
            }

            @Override public void onClosedToLeft() {
                isTop = false;
                pauseVideo();
            }

            @Override public void onClosedToRight() {
                isTop = false;
                pauseVideo();
            }
        });
    }

   public void initializeYoutubeFragment() {
        youtubeFragment = new YouTubePlayerFragment();
        youtubeFragment.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                          YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youtubePlayer = player;
                    //youtubePlayer.loadVideo(idVideo);
//                    youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                    youtubePlayer.setPlayerStateChangeListener(stateChangeListener);
                    youtubePlayer.setPlaybackEventListener(playbackEventListener);
                }
            }

            @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                          YouTubeInitializationResult error) {
            }
        });
    }
    private void pauseVideo() {
        if(youtubePlayer != null) {
            if (youtubePlayer.isPlaying()) {
                youtubePlayer.pause();

            }
        }
    }

    /**
     * Resume the video reproduced in the YouTubePlayer.
     */
    private void playVideo() {
        if(youtubePlayer != null) {
            if (!youtubePlayer.isPlaying()) {
                youtubePlayer.play();
            }
        }
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //Toast.makeText(this, "ORIENTATION_LANDSCAPE", Toast.LENGTH_SHORT).show();
//           youtubePlayer.setFullscreen(true);
////            draggablePanel.maximize();
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
//           // Toast.makeText(this, "ORIENTATION_PORTRAIT" +"", Toast.LENGTH_SHORT).show();
//            youtubePlayer.setFullscreen(false);
//
//        }
//
//    }


    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
//            ktEndTouch = false;
//            mShowProgress.run();
        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean isBuffering) {
//            ViewGroup ytView = (ViewGroup)youtubeFragment.getRootView();
//            ProgressBar progressBar;
//            try {
//                // As of 2016-02-16, the ProgressBar is at position 0 -> 3 -> 2 in the view tree of the Youtube Player Fragment
//                ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
//                ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
//                progressBar = (ProgressBar)child2.getChildAt(2);
//            } catch (Throwable t) {
//                // As its position may change, we fallback to looking for it
//                progressBar = findProgressBar(ytView);
//                // TODO I recommend reporting this problem so that you can update the code in the try branch: direct access is more efficient than searching for it
//            }
//
//
//            int visibility = isBuffering ? View.VISIBLE : View.INVISIBLE;
//            if (progressBar != null) {
//                progressBar.setVisibility(visibility);
//                // Note that you could store the ProgressBar instance somewhere from here, and use that later instead of accessing it again.
//            }
        }

        @Override
        public void onSeekTo(int i) {

        }
        private ProgressBar findProgressBar(View view) {
            if (view instanceof ProgressBar) {
                return (ProgressBar)view;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                    if (res != null) return res;
                }
            }
            return null;
        }
    };

    YouTubePlayer.PlayerStateChangeListener stateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
        }

        @Override
        public void onLoaded(String s) {
            youtubePlayer.play();
//            mYouTubePlayer.play();
            // mShowProgress.run();
        }

        @Override
        public void onAdStarted() {
            //Toast.makeText(PlayVideo.this, "onAdStarted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoStarted() {
            //Toast.makeText(PlayVideo.this, "onVideoStarted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoEnded() {
//            btnPlayPause.setImageResource(R.mipmap.ic_play);
//            mYouTubePlayer.pause();
//            ktEndTouch = true;
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
           //Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
//           mDatabaseReference.child("Video").addChildEventListener(new ChildEventListener() {
//               @Override
//               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                   Video video1 = dataSnapshot.getValue(Video.class);
//
//                   //Toast.makeText(MainActivity.this,video.getName(), Toast.LENGTH_SHORT).show();
//                   if(video.getVideoID().equals(video1.getVideoID().toString())) {
//                       isVideoError = true;
//                      // Toast.makeText(MainActivity.this, "da luu roi", Toast.LENGTH_SHORT).show();
//                   }
//
//                   if("end".equals(video1.getVideoID().toString())){
////                       Toast.makeText(MainActivity.this, "chay toi end roi" + (isKt()?"OK":"NO"), Toast.LENGTH_SHORT).show();
//
//                       //mDatabase.child("Video").child(dataSnapshot.getKey())
//                       if(!isVideoError){
//                           //Toast.makeText(MainActivity.this, "vo day roi", Toast.LENGTH_SHORT).show();
//                           mDatabaseReference.child("Video").push().setValue(new Video(video.getName(), video.getImage(), video.getVideoID()), new DatabaseReference.CompletionListener() {
//                               @Override
//                               public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                   if (databaseError == null){
//                                       //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                   }else {
//                                       //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           });
//                       }
//                   }
//               }
//
//               @Override
//               public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//               }
//
//               @Override
//               public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//               }
//
//               @Override
//               public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//               }
//
//               @Override
//               public void onCancelled(DatabaseError databaseError) {
//
//               }
//           });

        }
    };

    public static void loadTrangChu(){
        FragmentTransaction fragmentTransaction = mfragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearLayout, new TrangChu(), "frag_TrangChu");
    }

    public void usingCustomIcons() {

//        //shown when the button is in its default state or when unLiked.
//        smileButton.setUnlikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(this, CommunityMaterial.Icon.cmd_emoticon).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap()));
//
//        //shown when the button is liked!
//        smileButton.setLikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(this, CommunityMaterial.Icon.cmd_emoticon).colorRes(android.R.color.holo_purple).sizeDp(25).toBitmap()));
    }
//
//    @Override
//    public void onClick(View v) {
//        if(ktEndTouch){
//            mYouTubePlayer.play();
//            btnPlayPause.setImageResource(R.mipmap.ic_pause);
//        }else {
//            if(mYouTubePlayer != null & !mYouTubePlayer.isPlaying()){
//                mYouTubePlayer.play();
//                btnPlayPause.setImageResource(R.mipmap.ic_pause);
//            }else {
//                mYouTubePlayer.pause();
//                btnPlayPause.setImageResource(R.mipmap.ic_play);
//            }
//        }
//
//    }



}


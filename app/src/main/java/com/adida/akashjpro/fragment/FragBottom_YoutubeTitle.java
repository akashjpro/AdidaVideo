package com.adida.akashjpro.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.adapter.CommentAdapter;
import com.adida.akashjpro.adapter.CommentAdapterListener;
import com.adida.akashjpro.adapter.CommentRecyclerView;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Comment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.adida.akashjpro.activity.MainActivity.mUser;

/**
 * Created by Aka on 12/30/2016.
 */

public class FragBottom_YoutubeTitle extends Fragment implements CommentAdapterListener {

    private static DatabaseReference mDatabase;
    public static String idVideo;
    public static boolean isView;
    public static int numberLike;
    private int i = 0; // ktra when click like or unlike

    public static boolean mStart;



    private RecyclerView mRecyclerView;
    public static ArrayList<Comment> mListCommentData;
    public static CommentRecyclerView mCommentRecyclerView;

    private ListView mListViewComment;
    public static CommentAdapter mCommentAdapter;


    public static ChildEventListener mChildEventListenerComment;
    public static Activity context;
    private boolean isOpened = false;
    public static long mTongSoComment;

    public static ArrayList<Integer> mArrayTotalComment;





    public static boolean isFirst;
    private static Context mContext;

    private boolean isLogin = false;

    private static  boolean isFavorite;

    public static String mTitle ;
    public static String mUrl ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bottom_youtube_title, container, false);

        mDatabase  = FirebaseDatabase.getInstance().getReference();



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewComment);
        mListCommentData = new ArrayList<>();
        mArrayTotalComment = new ArrayList<>();



        StaggeredGridLayoutManager mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mCommentRecyclerView  = new CommentRecyclerView(getActivity(), mListCommentData, this);
        mRecyclerView.setAdapter(mCommentRecyclerView);


        context = getActivity();

        return view;
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        // Checks whether a hardware keyboard is available
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            Toast.makeText(getActivity(), "keyboard visible", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            Toast.makeText(getActivity(), "keyboard hidden", Toast.LENGTH_SHORT).show();
//        }
    }








    public static void  changeViewVideo(final String id, final String name, final String image){

        mStart = false;

        mTitle = name;
        mUrl   = image;

        isFirst = false;
        mListCommentData.clear();
        mListCommentData.add(null);

        isView = true;
        idVideo = id;
        mTongSoComment = 0;
        CommentRecyclerView.mTotalComment = 0;
        mCommentRecyclerView.notifyDataSetChanged();
        mChildEventListenerComment = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Comment comment = dataSnapshot.getValue(Comment.class);
                mListCommentData.add(comment);
                if(mUser != null) {
                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                            .child(mUser.getUid()).child("Comment").child(String.valueOf(mListCommentData.size() - 2))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {

                                        if (!mStart) {
                                            int index = Integer.parseInt(dataSnapshot.getKey().toString());
                                            Comment comment1 = mListCommentData.get(index + 1);
                                            String staus = dataSnapshot.getValue().toString();
                                            if (staus.equals("true")) {
                                                comment1.setLike(true);

                                            } else {
                                                comment1.setLike(false);
                                            }
                                            mListCommentData.set(index + 1, comment1);

                                            mCommentRecyclerView.notifyItemInserted(index + 1);
                                            if (index + 1 == mListCommentData.size() - 1) {
                                                mStart = true;
                                            }

                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    if (mStart) {
                        mCommentRecyclerView.notifyItemInserted(mListCommentData.size() - 1);
                    }

                }else {
                    mCommentRecyclerView.notifyItemInserted(mListCommentData.size() - 1);
                }


                CommentRecyclerView.getTotalComment(mListCommentData.size() - 1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CommentRecyclerView.getTotalComment(mListCommentData.size() - 1);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CommentRecyclerView.getTotalComment(mListCommentData.size() - 1);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("Video").child(idVideo).child("Comment").addChildEventListener(mChildEventListenerComment);











    }


    @Override
    public void onCommentLikeClick(int positon, boolean like) {

        if(mUser != null) {

            mStart = true;
            Comment comment = mListCommentData.get(positon);

            if (like) {
                comment.setLike(true);
                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                        .child(mUser.getUid()).child("Comment").child(String.valueOf(positon - 1)).setValue("true");
            } else {
                comment.setLike(false);
                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                        .child(mUser.getUid()).child("Comment").child(String.valueOf(positon - 1)).setValue("false");
            }
            mListCommentData.set(positon, comment);
            mCommentRecyclerView.notifyItemChanged(positon);
        }
    }
}

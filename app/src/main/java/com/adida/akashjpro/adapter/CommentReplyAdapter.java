package com.adida.akashjpro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.activity.ProfileActivity;
import com.adida.akashjpro.activity.utils.untilsTime;
import com.adida.akashjpro.fragment.FragBottom_YoutubeTitle;
import com.adida.akashjpro.likebutton.LikeButton;
import com.adida.akashjpro.likebutton.OnLikeListener;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.CommentReply;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aka on 1/10/2017.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.ViewHoler>{

    Context mContext;
    public static List<CommentReply> mListComment;
    public static Handler mHandlerReply;
    public static Runnable mRunnableReply;
    public static boolean isReplyComment;

    public class ViewHoler extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView name, contten, time, totalLike, idUser;
        private LikeButton mLikeCommentButton;
        private int mPosition;

        public ViewHoler(View itemView) {
            super(itemView);
            name               = (TextView)    itemView.findViewById(R.id.nameCommentReply);
            profilePic         = (ImageView)   itemView.findViewById(R.id.profilePicCommentReply);
            contten            = (TextView)    itemView.findViewById(R.id.conttenCommnetReply);
            time               = (TextView)    itemView.findViewById(R.id.timeCommentReply);
            totalLike          = (TextView)    itemView.findViewById(R.id.totalLikeCommentReply);
            idUser             = (TextView)    itemView.findViewById(R.id.idUserCommentReply);
            mLikeCommentButton = (LikeButton)  itemView.findViewById(R.id.btnLikeCommentReply);
        }

    }

    public CommentReplyAdapter(Context mContext, List<CommentReply> mListComment) {
        this.mContext = mContext;
        this.mListComment = mListComment;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reply_comment, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(final ViewHoler holder, final int position) {
        isReplyComment = true;
        final int mNumberLike[] = new int[1000];
        final CommentReply comment = mListComment.get(position);
        holder.idUser.setText(comment.getId());
        holder.contten.setText(comment.getContent());
        final String time = comment.getTime();
        mHandlerReply = new Handler();
        mRunnableReply = new Runnable() {
            @Override
            public void run() {
                if(isReplyComment) {
                    int p = holder.mPosition;
                    if(mListComment.size() != 0) {
                        CommentReply commentReply = mListComment.get(p);
                        final String time = commentReply.getTime();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
                        String timeCurrent = df.format(calendar.getTime());
                        holder.time.setText(untilsTime.getTime(time, timeCurrent));
                    }

                    mHandlerReply.postDelayed(this, 1000);
                }
            }
        };

        mHandlerReply.postDelayed(mRunnableReply, 0);

        holder.totalLike.setText(comment.getTotalLike());

        final String id =  holder.idUser.getText().toString();

        final String idVideo = FragBottom_YoutubeTitle.idVideo;
        final String positonParent  = comment.getPosition();
        final String positonComment = String.valueOf(position);


        MainActivity.mDatabase.child("User").child(id).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get image profile user
        MainActivity.mDatabase.child("User").child(id).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                if(url != null && !url.isEmpty()){
                    Picasso.with(mContext).load(url)
                            .fit().centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.no_image)
                            .into( holder.profilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("idUser", id);
                mContext.startActivity(intent);
            }
        });


        //        Check status like comment user
        MainActivity.mDatabase.child("User").child(id).child("CommentLike").child(idVideo)
                .child("CommentReply").child(positonParent).child(positonComment)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    String status = dataSnapshot.getValue().toString();
                    if(status.equals("yes")){
                        holder.mLikeCommentButton.setLiked(true);
                        comment.setLike(true);
                    }else {
                        holder.mLikeCommentButton.setLiked(false);
                        comment.setLike(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //recyclerview sroll if item is click true else false
        if(comment.isLike()){
            holder.mLikeCommentButton.setLiked(true);
        }else {
            holder.mLikeCommentButton.setLiked(false);
        }




//        Get total like comment
        MainActivity.mDatabase.child("Video").child(idVideo).child("LikeComment")
                .child("CommentReply").child(positonParent).child(positonComment)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    mNumberLike[position] = Integer.parseInt(dataSnapshot.getValue().toString());
                    holder.totalLike.setText(mNumberLike[position] + "");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Listener button
        holder.mLikeCommentButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                holder.mLikeCommentButton.setLiked(true);
                comment.setLike(true);

                String i = (mNumberLike[position] + 1) + "";
                MainActivity.mDatabase.child("Video").child(idVideo).child("LikeComment")
                        .child("CommentReply").child(positonParent).child(positonComment).setValue(i);
                MainActivity.mDatabase.child("User").child(id).child("CommentLike").child(idVideo)
                        .child("CommentReply").child(positonParent).child(positonComment).setValue("yes");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                holder.mLikeCommentButton.setLiked(false);
                comment.setLike(false);

                String i = (mNumberLike[position] - 1) + "";
                MainActivity.mDatabase.child("Video").child(idVideo).child("LikeComment")
                        .child("CommentReply").child(positonParent).child(positonComment).setValue(i);
                MainActivity.mDatabase.child("User").child(id).child("CommentLike").child(idVideo)
                        .child("CommentReply").child(positonParent).child(positonComment).setValue("no");
            }
        });


    }

    @Override
    public int getItemCount() {
        return mListComment.size();
    }


}

package com.adida.akashjpro.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.activity.LoginActivity;
import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.activity.ProfileActivity;
import com.adida.akashjpro.activity.utils.untilsTime;
import com.adida.akashjpro.fragment.FragBottom_YoutubeTitle;
import com.adida.akashjpro.fragment.FragmentVideoFavorite;
import com.adida.akashjpro.likebutton.LikeButton;
import com.adida.akashjpro.likebutton.OnLikeListener;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Comment;
import com.adida.akashjpro.model.CommentReply;
import com.adida.akashjpro.model.ReportComment;
import com.adida.akashjpro.model.VideoFavorite;
import com.facebook.share.model.ShareLinkContent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.adida.akashjpro.activity.MainActivity.mIdVideoFavorite;
import static com.adida.akashjpro.activity.MainActivity.mUser;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mArrayTotalComment;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mListCommentData;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mTitle;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mUrl;
import static com.adida.akashjpro.livevideo.R.id.commentReply;
import static com.adida.akashjpro.livevideo.R.id.likeButton;
import static java.lang.Integer.parseInt;

/**
 * Created by Aka on 1/7/2017.
 */

public class CommentRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static Context mContext;
    public static List<Comment> mListComment;
    View mItemView;
    public static int mTotalComment;
    public static ChildEventListener mChildEventListenerCommentReply;
    public static String indexListener;
    TextView timeComment[] = new TextView[1000];
    public static Handler[] mHandler = new Handler[1000];
    public static Runnable[] mRunnable = new Runnable[1000] ;

    private RecyclerView recyclerView[] = new RecyclerView[1000];
    private CommentReplyAdapter mCommentReplyAdapter[] = new CommentReplyAdapter[1000];
    private ArrayList<CommentReply> listComment[] = new ArrayList[1000];
    public static ValueEventListener listenerCheckStatus;
    public static ValueEventListener listenerTotalLike;
    public  static int mNumberLike[] = new int[1000];

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_HEADER = 1;

    public static HeaderViewHoler headerViewHoler;
    private static String mTitile;

    private  int numberLike;
    private  boolean isFavorite;
    public static ChildEventListener mChildEventListenerComment;
    private int mPosition;
    private CommentAdapterListener mCommentAdapterListener;




    public class ViewHoler extends RecyclerView.ViewHolder {
        public ImageView profilePic, more_vert;
        public TextView name, contten, time, totalLike, idUser, nameReply;
        private LikeButton mLikeCommentButton;
        private ImageButton mCommentButton;
        private int mPosition;

        public ViewHoler(View itemView) {
            super(itemView);
            name               = (TextView)    itemView.findViewById(R.id.nameComment);
            profilePic         = (ImageView)   itemView.findViewById(R.id.profilePicComment);
            contten            = (TextView)    itemView.findViewById(R.id.conttenCommnet);
            time               = (TextView)    itemView.findViewById(R.id.timeComment);
            totalLike          = (TextView)    itemView.findViewById(R.id.totalLikeComment);
            idUser             = (TextView)    itemView.findViewById(R.id.idUserComment);
            mLikeCommentButton = (LikeButton)  itemView.findViewById(R.id.btnLikeComment);
            mCommentButton     = (ImageButton) itemView.findViewById(R.id.btnComment);
            nameReply          = (TextView) itemView.findViewById(R.id.nameReply);
            more_vert          = (ImageView) itemView.findViewById(R.id.more_vert);
        }

    }

    public class HeaderViewHoler extends RecyclerView.ViewHolder{
        TextView txtTitle, txtNamo, txtTotalView, txtTotalLike, txtTotalComment;
        LikeButton btnLike, btnHeart, btnShare;
        ImageView profilePicComment, swipeLeft, fingerDrag;
        EditText contentCommentEdt;
        Button sentCommentButton;

        public HeaderViewHoler(View itemView) {
            super(itemView);

            txtTitle        = (TextView) itemView.findViewById(R.id.textViewTitle);
            txtNamo         = (TextView) itemView.findViewById(R.id.txtNamMo);
            txtTotalComment = (TextView) itemView.findViewById(R.id.tongSoComment);
            txtTotalView    = (TextView) itemView.findViewById(R.id.txtViewVideo);
            txtTotalLike    = (TextView) itemView.findViewById(R.id.txtLike);

            swipeLeft       = (ImageView) itemView.findViewById(R.id.swipeLeft);
            fingerDrag      = (ImageView) itemView.findViewById(R.id.finferDrag);

            btnLike         = (LikeButton) itemView.findViewById(likeButton);
            btnHeart        = (LikeButton) itemView.findViewById(R.id.heart_button);
            btnShare        = (LikeButton) itemView.findViewById(R.id.share_button);

            profilePicComment = (ImageView) itemView.findViewById(R.id.profilePicComment);
            contentCommentEdt = (EditText) itemView.findViewById(R.id.contentCommentEdt);
            sentCommentButton = (Button) itemView.findViewById(R.id.sentCommentButton);

        }
    }

    public CommentRecyclerView(Context mContext, List<Comment> mListComment, CommentAdapterListener mCommentAdapterListener) {
        this.mContext = mContext;
        this.mListComment = mListComment;
        this.mCommentAdapterListener = mCommentAdapterListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mListComment.get(position) == null ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            mItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false);
            return new ViewHoler(mItemView);
        }
        else if(viewType == VIEW_TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_layout_comment, parent, false);
            return new HeaderViewHoler(view);
        }
        return  null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHoler) {

            final ViewHoler holder = (ViewHoler) holder1;

            final Comment comment = mListComment.get(position);
            holder.idUser.setText(comment.getId());
            holder.contten.setText(comment.getContent());
            holder.mPosition = position;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String date = dateFormat.format(calendar.getTime());
            String time = untilsTime.getTimComment(comment.getTime(), date);
            holder.time.setText(time);


            holder.totalLike.setText(comment.getTotalLike());

            final String id = holder.idUser.getText().toString();

            final String idVideo = FragBottom_YoutubeTitle.idVideo;
            final String positonComment = String.valueOf(position - 1);

            //boolean like = false;

//
//            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
//                    .child(mUser.getUid()).child("Comment").child(positonComment)
//                    .addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.getValue() != null){
//                        int index = Integer.parseInt(dataSnapshot.getKey().toString());
//                        Comment comment1 = mListComment.get(index + 1);
//                        String staus = dataSnapshot.getValue().toString();
//                        if(staus.equals("true")){
//                            comment1.setLike(true);
//
//                        }else {
//                            comment1.setLike(false);
//                        }
//                        mListComment.set(index + 1, comment1);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });


            indexListener = positonComment;


            holder.mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser != null) {
                        final int position = (int) holder.name.getTag();
                        final String idReply = mListComment.get(position).getId();
                        if (mUser.getUid().equals(idReply)) {
                            Toast.makeText(mContext, "Đây là bình luận của bạn", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_comment_reply_input);
                        dialog.setCanceledOnTouchOutside(false);// hủy khi chạm ở bên ngoài;

                        final EditText commentReplyEdt = (EditText) dialog.findViewById(commentReply);
                        Button sentReply = (Button) dialog.findViewById(R.id.sentCommentReply);
                        Button exit = (Button) dialog.findViewById(R.id.btnExitReply);


                        sentReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String conttenComment = commentReplyEdt.getText().toString().trim();
                                if (conttenComment.isEmpty()) {
                                    Toast.makeText(mContext,
                                            "Xin nhập nội dung bình luân!!!",
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String id = mUser.getUid();
                                String name = mListComment.get(position).getName();
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                                String time = df.format(calendar.getTime());
                                Comment comment = new Comment(id, idReply, name, conttenComment, time, "0");
                                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                        .child("Comment").push().setValue(comment);

                                commentReplyEdt.setText("");
                                dialog.dismiss();
                            }
                        });

                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }else {
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();

                    }
                }
            });

            String idReply = comment.getIdReply();
            if (!idReply.equals("")) {
                MainActivity.mDatabase.child("User").child(idReply).child("name")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.nameReply.setText("->" + dataSnapshot.getValue().toString() + " ");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {
                holder.nameReply.setText("");
            }


            MainActivity.mDatabase.child("User").child(id).child("name")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.name.setText(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.name.setTag(position);

            //get image profile user
            MainActivity.mDatabase.child("User").child(id).child("profilePic")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String url = dataSnapshot.getValue().toString();
                    if (url != null && !url.isEmpty()) {
                        Picasso.with(mContext).load(url)
                                .fit().centerCrop()
                                .placeholder(R.drawable.loading)
                                .error(R.drawable.no_image)
                                .into(holder.profilePic);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mUser != null) {
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("idUser", id);
                        mContext.startActivity(intent);
                    }else {
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();

                    }
                }
            });


            //        Check status like comment user
//            listenerCheckStatus = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() != null) {
//                        String status = dataSnapshot.getValue().toString();
//                        if (status.equals("yes")) {
//                            holder.mLikeCommentButton.setLiked(true);
//                            comment.setLike(true);
//                        } else {
//                            holder.mLikeCommentButton.setLiked(false);
//                            comment.setLike(false);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//
//            MainActivity.mDatabase.child("CommentLikeVideo").child(mUser.getUid())
//                    .child("CommentLike").child(idVideo)
//                    .child(positonComment).addValueEventListener(listenerCheckStatus);


            //recyclerview sroll if item is click true else false
            if (comment.isLike()) {
                holder.mLikeCommentButton.setLiked(true);
            } else {
                holder.mLikeCommentButton.setLiked(false);
            }


            if(mArrayTotalComment.size() < position){
                mArrayTotalComment.add(Integer.parseInt(mListComment.get(position).getTotalLike()));
            }




//        Get total like comment
            listenerTotalLike = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        int toTalLike = Integer.parseInt(dataSnapshot.getValue().toString());
                        if((mArrayTotalComment.size() - 1) >= (position -1)) {
                            if (toTalLike != mArrayTotalComment.get(position - 1)) {
                                mArrayTotalComment.set(position - 1, toTalLike);
                                Comment comment1 = mListComment.get(position);
                                comment1.setTotalLike(dataSnapshot.getValue().toString());
                                mListComment.set(position, comment1);
                            }
                        }



                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            if(listenerTotalLike != null){
                MainActivity.mDatabase.child("Video").child(idVideo).child("Comment")
                        .child(positonComment).child("totalLike").removeEventListener(listenerTotalLike);
            }

            MainActivity.mDatabase.child("Video").child(idVideo).child("Comment")
                    .child(positonComment).child("totalLike").addValueEventListener(listenerTotalLike);

            holder.totalLike.setText(mListComment.get(position).getTotalLike());

            //Listener button
            holder.mLikeCommentButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if(mUser != null) {
                        //holder.mLikeCommentButton.setLiked(true);
                        mCommentAdapterListener.onCommentLikeClick((position), true);

                        String i = (mArrayTotalComment.get(position - 1) + 1) + "";
                        //edit total comment Array
                        holder.totalLike.setText(i);
                        Comment comment1 = mListComment.get(position);
                        comment1.setTotalLike(i);
                        mListComment.set(position, comment1);

                        //Save total comment in Database
                        MainActivity.mDatabase.child("Video").child(idVideo).child("Comment")
                                .child(positonComment).child("totalLike").setValue(i);
                    }else {
                        holder.mLikeCommentButton.setLiked(false);
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();

                        return;

                    }


                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (mUser != null) {
                        //holder.mLikeCommentButton.setLiked(false);
                        comment.setLike(false);
                        mCommentAdapterListener.onCommentLikeClick((position), false);

                        String i = (mArrayTotalComment.get(position - 1) - 1) + "";
                        holder.totalLike.setText(i);
                        //edit total comment Array
                        Comment comment1 = mListComment.get(position);
                        comment1.setTotalLike(i);
                        mListComment.set(position, comment1);

                        //Save total comment in Database
                        MainActivity.mDatabase.child("Video").child(idVideo).child("Comment")
                                .child(positonComment).child("totalLike").setValue(i);

                    }else {

                    }
                }
            });

            holder.more_vert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mUser != null) {
                        if (mUser.getUid().equals(mListComment.get(position).getId())) {
                            //Toast.makeText(mContext, "Same", Toast.LENGTH_SHORT).show();
                            final PopupMenu popupMenu = new PopupMenu(mContext, holder.more_vert);
                            popupMenu.getMenuInflater().inflate(R.menu.menu_comment, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    final int[] i = {0};
                                    switch (item.getItemId()) {
                                        case R.id.menuEdit:
                                            final Dialog dialog = new Dialog(mContext);
                                            dialog.setContentView(R.layout.edit_profile);
                                            dialog.setCanceledOnTouchOutside(false);

                                            Button mUpdateButton = (Button) dialog.findViewById(R.id.updateButton);
                                            Button mExitButton = (Button) dialog.findViewById(R.id.exitButton);
                                            final EditText mUpdateText = (EditText) dialog.findViewById(R.id.updateText);
                                            mUpdateText.setText(mListComment.get(position).getContent());
                                            mUpdateText.setSelection(mUpdateText.getText().length());

                                            mUpdateButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final Comment comment = new Comment(mListComment.get(position).getId(),
                                                            mListComment.get(position).getIdReply(),
                                                            mListComment.get(position).getName(),
                                                            mUpdateText.getText().toString(),
                                                            mListComment.get(position).getTime(),
                                                            mListComment.get(position).getTotalLike());


                                                    comment.setLike(mListComment.get(position).isLike());

                                                    mListCommentData.set(position
                                                            , comment);

                                                    FragBottom_YoutubeTitle.mCommentRecyclerView.notifyDataSetChanged();
                                                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                            .child("Comment").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                            if (i[0] == position - 1) {
                                                                String id = dataSnapshot.getKey().toString();
                                                                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                                        .child("Comment").child(id).setValue(comment);
                                                            }
                                                            i[0]++;
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
                                                    });
                                                    Toast.makeText(mContext, "Đã sửa ", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            });

                                            mExitButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            dialog.show();


                                            break;
                                        case R.id.menuXoa:

                                            for (int j = position; j < mListComment.size() - 1; j++) {
                                                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                        .child("Comment").child(String.valueOf(j - 1))
                                                        .setValue(mListComment.get(j + 1));

                                                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                        .child(mUser.getUid()).child("Comment")
                                                        .child(String.valueOf(j - 1))
                                                        .setValue(mListComment.get(j + 1).isLike());

                                                mListComment.set(j, mListComment.get(j + 1));
                                                mArrayTotalComment.set(j - 1, j);
                                            }


                                            FragBottom_YoutubeTitle.mCommentRecyclerView.notifyDataSetChanged();


//
                                            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                    .child("Comment").child(String.valueOf(mListComment.size() - 2)).removeValue();

                                            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                                    .child(mUser.getUid()).child("Comment")
                                                    .child(String.valueOf(mListComment.size() - 2)).removeValue();
                                            mListCommentData.remove(mListComment.size() - 1);
//
//                                        //Delete indext array
                                            mArrayTotalComment.remove(mArrayTotalComment.size() - 1);
                                            FragBottom_YoutubeTitle.mCommentRecyclerView.notifyItemRemoved(mArrayTotalComment.size() - 1);
                                            Toast.makeText(mContext, "Đã xóa", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                    return false;
                                }
                            });
                            popupMenu.show();
                        } else {
                            PopupMenu popupMenu = new PopupMenu(mContext, holder.more_vert);
                            popupMenu.getMenuInflater().inflate(R.menu.menu_comment1, popupMenu.getMenu());

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    final boolean[] isReport = {false};
                                    MainActivity.mDatabase.child("ReportComment")
                                            .addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    if (dataSnapshot.getValue() != null) {
                                                        ReportComment reportComment1 = dataSnapshot.getValue(ReportComment.class);
                                                        String idVideo = reportComment1.getIdVideo();
                                                        String content = reportComment1.getContent();
                                                        String index = reportComment1.getPosition();

                                                        if (mListComment.get(position).getContent().equals(content)
                                                                && idVideo.equals(FragBottom_YoutubeTitle.idVideo)
                                                                && index.equals(String.valueOf(position))) {
                                                            isReport[0] = true;
                                                        }

                                                        if (idVideo.equals("end")) {
                                                            if (!isReport[0]) {
                                                                ReportComment reportComment = new ReportComment
                                                                        (FragBottom_YoutubeTitle.idVideo,
                                                                                mListComment.get(position).getContent(),
                                                                                String.valueOf(position)
                                                                        );

                                                                MainActivity.mDatabase.child("ReportComment")
                                                                        .push().setValue(reportComment);
                                                                Toast.makeText(mContext
                                                                        , "Đã báo cáo"
                                                                        , Toast.LENGTH_SHORT)
                                                                        .show();
                                                            } else {
                                                                Toast.makeText(mContext, "Đã gửi báo cáo",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }

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
                                            });


                                    return false;
                                }
                            });
                            popupMenu.show();

                            //Toast.makeText(mContext, "orther", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();

                    }
                }
            });


        }

        else if (holder1 instanceof HeaderViewHoler){

            if(mUser != null){
                ((HeaderViewHoler) holder1).contentCommentEdt.setEnabled(true);
            }else {
                ((HeaderViewHoler) holder1).contentCommentEdt.setEnabled(false);
            }
            headerViewHoler = (HeaderViewHoler) holder1;
            headerViewHoler.txtTitle.setText(mTitile);
            headerViewHoler.txtTotalComment.setText("0");
            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo).child("view")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        headerViewHoler.txtTotalView.setText(dataSnapshot.getValue().toString()
                                + " lượt xem");
                        if (FragBottom_YoutubeTitle.isView) {
                            String view = parseInt(dataSnapshot.getValue().toString())
                                    + 1 + "";
                            MainActivity.mDatabase.child("Video")
                                    .child(FragBottom_YoutubeTitle.idVideo)
                                    .child("view").setValue(view);

                            FragBottom_YoutubeTitle.isView = false;
                        }
                    } else {
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("view").setValue(10);
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("like").setValue(2);
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("share").setValue(10);
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("name").setValue(mTitle);
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("image").setValue(mUrl);
                    }

                    //int i =
//                txtData.setText(dataSnapshot.getValue().toString());
//                Toast.makeText(MainActivity.this, "Da thay doi", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


            /**
             * Update lượt like of video when user clik button like
             */
            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo).child("like")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                numberLike  = parseInt(dataSnapshot.getValue().toString());
                                headerViewHoler.txtTotalLike.setText(numberLike + "");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            if(mUser != null) {
                MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                        .child(mUser.getUid()).child("like")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    String status = dataSnapshot.getValue().toString();
                                    if (status.equals("yes"))
                                        headerViewHoler.btnLike.setLiked(true); // set like
                                    else
                                        headerViewHoler.btnLike.setLiked(false); // set unlike
                                } else {
                                    MainActivity.mDatabase.child("Video")
                                            .child(FragBottom_YoutubeTitle.idVideo)
                                            .child(mUser.getUid()).child("like").setValue("no");
                                    // set defaut no is unlike for button like
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid())
                        .child(FragBottom_YoutubeTitle.idVideo)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() != null){
                                    isFavorite = true;
                                    headerViewHoler.btnHeart.setLiked(true); // set like

                                }
                                else {
                                    // set defaut no is unlike for button like
                                    isFavorite = false;
                                    headerViewHoler.btnHeart.setLiked(false); // set unlike
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            headerViewHoler.btnLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if(mUser !=null ){
                        Toast.makeText(mContext, "Đã thích video này", Toast.LENGTH_SHORT).show();
                        String totalLike = (numberLike + 1) + "";
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child("like").setValue(totalLike);
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                                .child(mUser.getUid()).child("like").setValue("yes");

                    }
                    else {
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();
                        headerViewHoler.btnLike.setLiked(false);
                    }


                }


                @Override
                public void unLiked(LikeButton likeButton) {
                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                            .child("like").setValue(numberLike);
                    String totalLike = (numberLike - 1) + "";
                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                            .child("like").setValue(totalLike);
                    Toast.makeText(mContext, "Đã bỏ thích video này", Toast.LENGTH_SHORT).show();

                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                            .child(mUser.getUid()).child("like").setValue("no");
                }
            });


           headerViewHoler.btnHeart.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if(mUser != null ){
                        Toast.makeText(mContext, "Đã lưu mục yêu thích", Toast.LENGTH_SHORT).show();
                        VideoFavorite videoFavorite = new VideoFavorite(mIdVideoFavorite, mTitle, mUrl );
                        if(isFavorite) {
                            MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid())
                                    .child(FragBottom_YoutubeTitle.idVideo).setValue(videoFavorite);
                        }else {
                            MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid())
                                    .child(FragBottom_YoutubeTitle.idVideo).setValue(videoFavorite);
                            MainActivity.mDatabase.child("idVideoFavorite").child(mUser.getUid())
                                    .setValue(mIdVideoFavorite + 1);
                        }



                    }
                    else {
                        final AlertDialog.Builder alertLogin = new AlertDialog.Builder(mContext);
                        alertLogin.setIcon(R.mipmap.ic_launcher);
                        alertLogin.setTitle("Bạm cần phải đăng nhập!!!");
                        alertLogin.setMessage("Bạn có muốn đăng nhập không?");
                        alertLogin.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                        alertLogin.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertLogin.show();
                        headerViewHoler.btnHeart.setLiked(false);
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    MainActivity.mDatabase.child("VideoFavorite").child(mUser.getUid())
                            .child(FragBottom_YoutubeTitle.idVideo).removeValue();
//
                    for (int i=0; i<MainActivity.mListVideo.size(); i++){
                        String id = MainActivity.mListVideo.get(i).getVideoID();
                        if(id.equals(FragBottom_YoutubeTitle.idVideo)) {
                            MainActivity.mListVideo.remove(i);
                            if( FragmentVideoFavorite.mVideoFavoriteAdapter != null) {
                                FragmentVideoFavorite.mVideoFavoriteAdapter.notifyItemRemoved(i);
                            }
                            break;
                        }
                    }

                    Toast.makeText(mContext, "Đã xóa khỏi mục yêu thích", Toast.LENGTH_SHORT).show();

                }
            });

            headerViewHoler.btnShare.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
//                    String message = "Xin chào các bạn, xin các bạn ủng hộ ứng dụng này" +
//                            ", cám ơn các bạn nhiều!!!";
//                    Intent share = new Intent(Intent.ACTION_SEND);
//                    share.setType("text/plain");
//                    share.putExtra(Intent.EXTRA_TEXT, message);
//
//                    mContext.startActivity(Intent.createChooser(share,
//                            "Title of the dialog the system will open"));


//                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                    Uri screenshotUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/film-youtube.appspot.com/o/ic_launcher.png?alt=media&token=6efb2de0-b13e-4185-8e01-63d539b55f48");
//
//                    sharingIntent.setType("image/png");
//                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                    mContext.startActivity(Intent.createChooser(sharingIntent, "Share image using"));

//                    ShareLinkContent content = new ShareLinkContent.Builder()
//                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.adida.akashjpro.livevideo"))
//                            .build();
//                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("A DI DA VIDIEO")
                            .setContentDescription(
                                    "Toàn tập video Phật Giáo hay nhất")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.adida.akashjpro.livevideo"))
                            .build();

                    MainActivity.shareDialog.show(linkContent);


                    headerViewHoler.btnShare.setLiked(false);
                }

                @Override
                public void unLiked(LikeButton likeButton) {

                }
            });


            Animation animationTextNamMo = AnimationUtils.loadAnimation(mContext, R.anim.transition_right_to_left);
            headerViewHoler.txtNamo.startAnimation(animationTextNamMo);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    headerViewHoler.txtNamo.setText("Nam Mô A Di Đà Phật");
                    headerViewHoler.txtNamo.setTextColor(Color.RED);
                }
            }, 30000);


            if(mUser != null) {
                MainActivity.mDatabase.child("User").child(mUser.getUid()).child("profilePic").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().toString() != null) {
                                String url = dataSnapshot.getValue().toString();
                                if (url != null && !url.isEmpty()) {
                                    Picasso.with(mContext).load(url)
                                            .fit().centerCrop()
                                            .placeholder(R.drawable.ic_launcher)
                                            .error(R.drawable.no_image)
                                            .into(headerViewHoler.profilePicComment);
                                }
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }else {
                if (headerViewHoler.profilePicComment != null) {
                    headerViewHoler.profilePicComment.setImageResource(R.drawable.ic_launcher);
                }
            }


            headerViewHoler.profilePicComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mUser != null){
                        String id   = mUser.getUid();
                        Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("idUser", id);
                        mContext.startActivity(intent);
                    }
                }
            });

            headerViewHoler.contentCommentEdt.addTextChangedListener(filterTextWatcher);


//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    headerViewHoler.swipeLeft.setVisibility(View.VISIBLE);
//                    headerViewHoler.fingerDrag.setVisibility(View.VISIBLE);
//                    headerViewHoler.fingerDrag.startAnimation(animationFinger);
//                    headerViewHoler.swipeLeft.startAnimation(animationSwipeLeft);
//
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            headerViewHoler.swipeLeft.setVisibility(View.INVISIBLE);
//                            headerViewHoler.fingerDrag.setVisibility(View.INVISIBLE);
//                        }
//                    },4000);
//                }
//            },2000);


            headerViewHoler.sentCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String conttenComment = headerViewHoler.contentCommentEdt.getText()
                            .toString().trim();
                    if(conttenComment.isEmpty()){
                        Toast.makeText(mContext, "Xin nhập nội dung bình luân!!!"
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String id   = mUser.getUid();
                    String name = MainActivity.name;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    String time = df.format(calendar.getTime());


                    Comment comment = new Comment(id, "", name, conttenComment, time, "0");
                    MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo)
                            .child("Comment").child(String.valueOf(mListComment.size() - 1)).setValue(comment);



                    headerViewHoler.contentCommentEdt.setText("");
                    headerViewHoler.sentCommentButton.setVisibility(View.GONE);





                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mListComment.size();
    }

    public static void changeTitle(String title){
        mTitile = title;
    }

    public static void getTotalComment(long total){
        headerViewHoler.txtTotalComment.setText(total + "");
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // DO THE CALCULATIONS HERE AND SHOW THE RESULT AS PER YOUR CALCULATIONS
            //Toast.makeText(getActivity(), "onTextChanged", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Toast.makeText(getActivity(), "beforeTextChanged", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!headerViewHoler.contentCommentEdt.getText().toString().trim().isEmpty())
                headerViewHoler.sentCommentButton.setVisibility(View.VISIBLE);
            else
                headerViewHoler.sentCommentButton.setVisibility(View.GONE);



        }
    };



}

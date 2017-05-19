package com.adida.akashjpro.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.activity.ProfileActivity;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.adida.akashjpro.activity.MainActivity.mUser;

/**
 * Created by Aka on 1/5/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    Activity mContext;
    int mLayout;
    List<Comment> mListComment;

    public CommentAdapter(Activity context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.mContext     = context;
        this.mLayout      = resource;
        this.mListComment = objects;

    }

    class ViewHoler{
        ImageView profilePic;
        TextView name, contten, time, totalLike, idUser;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = convertView;
        ViewHoler holer = new ViewHoler();

        if(view == null){
            view = inflater.inflate(this.mLayout, null);
            holer.name         = (TextView) view.findViewById(R.id.nameComment);
            holer.profilePic   = (ImageView) view.findViewById(R.id.profilePicComment);
            holer.contten      = (TextView) view.findViewById(R.id.conttenCommnet);
            holer.time         = (TextView) view.findViewById(R.id.timeComment);
            holer.totalLike    = (TextView) view.findViewById(R.id.totalLikeComment);
            holer.idUser       = (TextView) view.findViewById(R.id.idUserComment);
            view.setTag(holer);
        }else {
            holer = (ViewHoler) view.getTag();
        }

        Comment comment = mListComment.get(position);
        holer.name.setText(comment.getName());
        holer.idUser.setText(comment.getId());
        holer.contten.setText(comment.getContent());
        holer.time.setText(comment.getTime());
        holer.totalLike.setText(comment.getTotalLike());
        final ViewHoler finalHoler = holer;

        //get image profile user
        MainActivity.mDatabase.child("User").child("profile").child(mUser.getUid()).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                if(url != null && !url.isEmpty()){
                    Picasso.with(mContext).load(url)
                            .fit().centerCrop()
                            .placeholder(R.drawable.ic_launcher)
                            .error(R.drawable.no_image)
                            .into(finalHoler.profilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holer.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = finalHoler.idUser.getText().toString();
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("idUser", id);
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.scale_zoom_center, R.anim.normal);
            }
        });




        return view;
    }
}

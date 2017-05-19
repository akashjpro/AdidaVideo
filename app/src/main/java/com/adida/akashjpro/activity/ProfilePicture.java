

package com.adida.akashjpro.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adida.akashjpro.livevideo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ProfilePicture extends AppCompatActivity {

    private ImageView mProfilePicture;
    private FirebaseAuth mAuth;
    private String id;
    LinearLayout mLayout;

    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        mProfilePicture = (ImageView) findViewById(R.id.imageViewProfilePic);
        mLayout = (LinearLayout) findViewById(R.id.activity_profile_picture);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString("id");
        }


        MainActivity.mDatabase.child("User").child(id).child("profilePic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                if(url != null && !url.isEmpty()){
                    Picasso.with(ProfilePicture.this).load(url)
                            .placeholder(R.drawable.load_cover)
                            .error(R.drawable.no_image)
                            .into(mProfilePicture);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mAttacher = new PhotoViewAttacher(mProfilePicture);
    }


    @Override
    public void onBackPressed() {
        Animation animation =  AnimationUtils.loadAnimation(ProfilePicture.this, R.anim.scale_out_center);
        mLayout.startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },500);
        super.onBackPressed();
    }
}

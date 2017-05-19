package com.adida.akashjpro.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Snap;

import java.util.ArrayList;

/**
 * Created by Akashjpro on 11/20/2016.
 */

public class SnapAdapter  extends RecyclerView.Adapter<SnapAdapter.ViewHolder> implements GravitySnapHelper.SnapListener {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private Activity activity;

    private ArrayList<Snap> mSnaps;
    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    public SnapAdapter(Activity activity) {
        this.activity = activity;
        mSnaps = new ArrayList<>();
    }

    public void addSnap(Snap snap) {
        mSnaps.add(snap);
    }

    @Override
    public int getItemViewType(int position) {
        Snap snap = mSnaps.get(position);
        switch (snap.getmGravity()) {
            case Gravity.CENTER_VERTICAL:
                return VERTICAL;
            case Gravity.CENTER_HORIZONTAL:
                return HORIZONTAL;
            case Gravity.START:
                return HORIZONTAL;
            case Gravity.TOP:
                return VERTICAL;
            case Gravity.END:
                return HORIZONTAL;
            case Gravity.BOTTOM:
                return VERTICAL;
        }
        return HORIZONTAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_snap, parent, false);

//        if (viewType == VERTICAL) {
//            view.findViewById(R.id.recyclerView).setOnTouchListener(mTouchListener);
//        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Snap snap = mSnaps.get(position);
        holder.snapTextView.setText(snap.getmText());
        final Animation animationFinger    = AnimationUtils.loadAnimation(this.activity,R.anim.dichuyen);
        final Animation animationSwipeLeft = AnimationUtils.loadAnimation(this.activity, R.anim.swipe_left);
        final Handler handler = new Handler();



        if(MainActivity.isLoad) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.imgSwipeLeft.setVisibility(View.VISIBLE);
                            holder.imgFinger.setVisibility(View.VISIBLE);
                            holder.imgFinger.startAnimation(animationFinger);
                            holder.imgSwipeLeft.startAnimation(animationSwipeLeft);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.imgSwipeLeft.setVisibility(View.INVISIBLE);
                                    holder.imgFinger.setVisibility(View.INVISIBLE);
                                }
                            },4000);
                        }
                    },2000);
                }
            },600);

        }


        if (snap.getmGravity() == Gravity.START || snap.getmGravity() == Gravity.END) {
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setOnFlingListener(null);


            //new GravitySnapHelper(snap.getmGravity(), false, (GravitySnapHelper.SnapListener) this).attachToRecyclerView(holder.recyclerView);
        } else if (snap.getmGravity() == Gravity.CENTER_HORIZONTAL
                || snap.getmGravity() == Gravity.CENTER_VERTICAL
                || snap.getmGravity() == Gravity.CENTER) {
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext(), snap.getmGravity() == Gravity.CENTER_HORIZONTAL ?
                    LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
            holder.recyclerView.setOnFlingListener(null);


            new LinearSnapHelper().attachToRecyclerView(holder.recyclerView);
        } else { // Top / Bottom
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder
                    .recyclerView.getContext()));
            holder.recyclerView.setOnFlingListener(null);


            new GravitySnapHelper(snap.getmGravity()).attachToRecyclerView(holder.recyclerView);
        }

        holder.recyclerView.setAdapter(new VideoAdapter(this.activity, snap.getVideos()));


        //----------------Cách khác ------------------------
//        ItemClickSupport.addTo(holder.recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                Toast.makeText(context, snap.getApps().get(position).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mSnaps.size();
    }

    @Override
    public void onSnap(int position) {
        Log.d("Snapped: ", position + "");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView snapTextView;
        public RecyclerView recyclerView;
        public ImageView imgFinger, imgSwipeLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            snapTextView = (TextView) itemView.findViewById(R.id.snapTextView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            imgFinger    = (ImageView) itemView.findViewById(R.id.finferDrag);
            imgSwipeLeft    = (ImageView) itemView.findViewById(R.id.swipeLeft);
        }

    }
}


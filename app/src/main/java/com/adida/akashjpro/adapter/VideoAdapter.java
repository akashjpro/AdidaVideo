package com.adida.akashjpro.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.fragment.FragBottom_Chat;
import com.adida.akashjpro.fragment.FragBottom_YoutubeTitle;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.adida.akashjpro.activity.MainActivity.draggablePanel;
import static com.adida.akashjpro.fragment.FragBottom_Chat.idVideo;
import static com.adida.akashjpro.fragment.FragBottom_Chat.mChildEventListener;
import static com.adida.akashjpro.fragment.FragBottom_Chat.mDatabase;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mChildEventListenerComment;

/**
 * Created by Akashjpro on 11/20/2016.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHoler> {

    private List<Video> videos;
    private Activity activity;


    public VideoAdapter(Activity activity, List<Video> videos) {
        this.activity = activity;
        this.videos = videos;
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.video, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, int position) {
        final Video video = this.videos.get(position);
        //holder.imageVideo.setImageResource(R.drawable.chim1);
        Picasso.with(this.activity).load(video.getImage()).placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.no_video).into(holder.imageVideo);

//         xu ly chuoi lay 31 ky tu neu name > 34 ky tu & cong them chuoi "..."
        if(video.getName().length()>23) {
            String name = video.getName().substring(0, 20);
//           kiem tra ki tu cuoi co phai la "-" thi cat bo
            int indexLast = name.lastIndexOf(" ");

            if(indexLast>0) {
                if ("-".equals(name.charAt(indexLast - 1) + "") | "|".equals(name.charAt(indexLast - 1) + "") |
                    "{".equals(name.charAt(indexLast - 1) + "") | "[".equals(name.charAt(indexLast - 1) + "") |
                    "(".equals(name.charAt(indexLast - 1) + "") | "✔".equals(name.charAt(indexLast - 1) + "")){
                    name = name.substring(0, indexLast - 1);
                } else {
                    name = name.substring(0, indexLast);
                }
                holder.nameVideo.setText(name + "...");
            }else {
                holder.nameVideo.setText(video.getName());
            }
        }else {
            holder.nameVideo.setText(video.getName());
        }



        holder.setOnClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                //FraggmentBottom.onCreateAgain();
//                if(CommentRecyclerView.listenerCheckStatus!= null | CommentRecyclerView.listenerTotalLike != null) {
//                    for (int i = 0; i < CommentRecyclerView.mListComment.size(); i++) {
//                        MainActivity.mDatabase.child("CommentLikeVideo").child(mUser.getUid()).child("CommentLike")
//                                .child(idVideo)
//                                .child(String.valueOf(i)).removeEventListener(CommentRecyclerView.listenerCheckStatus);
//                        MainActivity.mDatabase.child("Video").child(idVideo).child("LikeComment")
//                                .child(String.valueOf(i)).removeEventListener(CommentRecyclerView.listenerTotalLike);
//                    }
//                }

                CommentReplyAdapter.isReplyComment = false;
                /**
                 * Remove listener load messages chat de khong trung khi nhan tin
                 */
                if(FragBottom_Chat.mChildEventListener != null) {
                    mDatabase.child("Chat").child("chat").child(idVideo).removeEventListener(mChildEventListener);
                }
                if(mChildEventListenerComment != null) {
                    mDatabase.child("Video").child(idVideo).child("Comment").removeEventListener(mChildEventListenerComment);
                }


                for (int i=0; i<FragBottom_YoutubeTitle.mListCommentData.size(); i++){
                    if(CommentRecyclerView.mChildEventListenerCommentReply != null){
                        MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo).child("CommentReply")
                                .child(String.valueOf(i)).removeEventListener(CommentRecyclerView.mChildEventListenerCommentReply);
                    }
                }

                for (int i=0; i< FragBottom_YoutubeTitle.mListCommentData.size(); i++) {
                    if (CommentRecyclerView.mHandler[i] != null) {
                        CommentRecyclerView.mHandler[i].removeCallbacks(CommentRecyclerView.mRunnable[i]);
                    }
                }

                MainActivity.isOneListener = true;
                FragBottom_Chat.mColorMap.clear();
                MainActivity.video = new Video(video.getName(), video.getImage(), video.getVideoID());
                String idVideo = video.getVideoID();
                FragBottom_Chat.getId(idVideo);
                CommentRecyclerView.changeTitle(video.getName() + " ✔");
                FragBottom_YoutubeTitle.changeViewVideo(idVideo, video.getName(), video.getImage());
                MainActivity.youtubePlayer.cueVideo(idVideo);
                draggablePanel.setVisibility(View.VISIBLE);
                draggablePanel.maximize();
//                FragmentManager fragmentManager = activity.getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//
//                Fragment_PlayVideo fragment = new Fragment_PlayVideo();
//                Bundle bundle = new Bundle();
//                bundle.putString("idVideo", videos.get(position).getVideoID());
//                fragment.setArguments(bundle);
//
//                fragmentTransaction.add(R.id.framLayout, fragment);
//                fragmentTransaction.commit();
//                Intent intent = new Intent(activity, PlayVideo.class);
//                intent.putExtra("videoID", videos.get(position).getVideoID());
//                activity.startActivity(intent);
//                Toast.makeText(activity, "Da bam", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class ViewHoler extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public ImageView imageVideo;
        public TextView nameVideo;
        private ItemClickListener itemClickListener;

        public ViewHoler(View itemView) {
            super(itemView);
            imageVideo = (ImageView) itemView.findViewById(R.id.image);
            nameVideo  = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        public void setOnClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

}

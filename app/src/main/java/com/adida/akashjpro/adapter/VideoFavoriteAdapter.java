package com.adida.akashjpro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.fragment.FragBottom_Chat;
import com.adida.akashjpro.fragment.FragBottom_YoutubeTitle;
import com.adida.akashjpro.livevideo.R;
import com.adida.akashjpro.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.adida.akashjpro.fragment.FragBottom_Chat.idVideo;
import static com.adida.akashjpro.fragment.FragBottom_Chat.mChildEventListener;
import static com.adida.akashjpro.fragment.FragBottom_Chat.mDatabase;
import static com.adida.akashjpro.fragment.FragBottom_YoutubeTitle.mChildEventListenerComment;

/**
 * Created by Aka on 1/24/2017.
 */

public class VideoFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Video> mListVideo;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_lOADING = 1;

    public VideoFavoriteAdapter(Context mContext, List<Video> mListVideo) {
        this.mContext = mContext;
        this.mListVideo = mListVideo;
    }

    @Override
    public int getItemViewType(int position) {
        return mListVideo.get(position) == null ? VIEW_TYPE_lOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            return new ViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.videofavorite, parent, false));
        }else if (viewType == VIEW_TYPE_lOADING){
            return new LoadingViewHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_data, parent, false));
        }
        return null;

    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {

        if(holder1 instanceof ViewHoler) {

            ViewHoler holder = (ViewHoler) holder1;

            final Video video = this.mListVideo.get(position);
            Picasso.with(mContext).load(video.getImage()).placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.no_video).into(holder.picVideo);

            //         xu ly chuoi lay 31 ky tu neu name > 34 ky tu & cong them chuoi "..."
            if (video.getName().length() > 18) {
                String name = video.getName().substring(0, 17);
//           kiem tra ki tu cuoi co phai la "-" thi cat bo
                int indexLast = name.lastIndexOf(" ");

                if (indexLast > 0) {
                    if ("-".equals(name.charAt(indexLast - 1) + "") | "|".equals(name.charAt(indexLast - 1) + "") |
                            "{".equals(name.charAt(indexLast - 1) + "") | "[".equals(name.charAt(indexLast - 1) + "") |
                            "(".equals(name.charAt(indexLast - 1) + "") | "✔".equals(name.charAt(indexLast - 1) + "")) {
                        name = name.substring(0, indexLast - 1);
                    } else {
                        name = name.substring(0, indexLast);
                    }
                    holder.nameVideo.setText(name + "...");
                } else {
                    holder.nameVideo.setText(video.getName());
                }
            } else {
                holder.nameVideo.setText(video.getName());
            }

            holder.setOnClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    CommentReplyAdapter.isReplyComment = false;
                    /**
                     * Remove listener load messages chat de khong trung khi nhan tin
                     */
                    if (FragBottom_Chat.mChildEventListener != null) {
                        mDatabase.child("Chat").child("chat").child(idVideo).removeEventListener(mChildEventListener);
                    }
                    if (mChildEventListenerComment != null) {
                        mDatabase.child("Video").child(idVideo).child("Comment").removeEventListener(mChildEventListenerComment);
                    }


                    for (int i = 0; i < FragBottom_YoutubeTitle.mListCommentData.size(); i++) {
                        if (CommentRecyclerView.mChildEventListenerCommentReply != null) {
                            MainActivity.mDatabase.child("Video").child(FragBottom_YoutubeTitle.idVideo).child("CommentReply")
                                    .child(String.valueOf(i)).removeEventListener(CommentRecyclerView.mChildEventListenerCommentReply);
                        }
                    }

                    for (int i = 0; i < FragBottom_YoutubeTitle.mListCommentData.size(); i++) {
                        if (CommentRecyclerView.mHandler[i] != null) {
                            CommentRecyclerView.mHandler[i].removeCallbacks(CommentRecyclerView.mRunnable[i]);
                        }
                    }

//                    if(FragBottom_YoutubeTitle.mArrayTotalComment != null){
//                        if(CommentRecyclerView.listenerTotalLike != null){
//                            for (int i=0; i<FragBottom_YoutubeTitle.mArrayTotalComment.size(); i++){
//                                MainActivity.mDatabase.child("Video").child(idVideo).child("Comment")
//                                        .child(String.valueOf(i)).child("totalLike").removeValue();
//                            }
//                        }
//                    }
                    MainActivity.isOneListener = true;
                    FragBottom_Chat.mColorMap.clear();
                    MainActivity.video = new Video(video.getName(), video.getImage(), video.getVideoID());
                    String idVideo = video.getVideoID();
                    FragBottom_Chat.getId(idVideo);
                    CommentRecyclerView.changeTitle(video.getName() + " ✔");
                    FragBottom_YoutubeTitle.changeViewVideo(idVideo, video.getName(), video.getImage());
                    MainActivity.youtubePlayer.cueVideo(idVideo);
                    MainActivity.draggablePanel.setVisibility(View.VISIBLE);
                    MainActivity.draggablePanel.maximize();
                }
            });

        }else if(holder1 instanceof LoadingViewHoler) {
            LoadingViewHoler loadingViewHoler = (LoadingViewHoler) holder1;
            loadingViewHoler.prbBar.setIndeterminate(true);

        }


    }

    @Override
    public int getItemCount() {
        return mListVideo.size();
    }

    public static class ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView picVideo;
        TextView  nameVideo;
        private ItemClickListener mItemClickListener;

        public ViewHoler(View itemView) {
            super(itemView);

            picVideo  = (ImageView) itemView.findViewById(R.id.image);
            nameVideo = (TextView) itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        public void setOnClickListener(ItemClickListener itemClickListener){
            this.mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.mItemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public static class LoadingViewHoler extends RecyclerView.ViewHolder{
        public ProgressBar prbBar;

        public LoadingViewHoler(View itemView) {
            super(itemView);

            prbBar = (ProgressBar) itemView.findViewById(R.id.progressBar);


        }
    }

}

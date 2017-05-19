package com.adida.akashjpro.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.akashjpro.fragment.TrangChu;
import com.adida.akashjpro.livevideo.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Formatter;
import java.util.Locale;

public class PlayVideo extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        View.OnClickListener{

    private String id = "";
    YouTubePlayerView youTubePlayerView;
    int REQUEST_VIDEO = 123;

    YouTubePlayer mYouTubePlayer;
    ImageButton btnPlayPause, imgbFullScreen;
    SeekBar seekBar;
    TextView txtStartTime, txtEndTime;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean mDragging;
    boolean ktEndTouch;
    boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        addControls();
        Intent intent = getIntent();
        id = intent.getStringExtra("videoID");
        youTubePlayerView.initialize(TrangChu.API_KEY, PlayVideo.this);
        handleEvent();

    }

    private void addControls() {
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        btnPlayPause      = (ImageButton) findViewById(R.id.imgbtnPlayPause);
        imgbFullScreen    = (ImageButton) findViewById(R.id.fullSreen);
        seekBar           = (SeekBar) findViewById(R.id.video_seekbar);
        txtStartTime      = (TextView) findViewById(R.id.thoiGianBT);
        txtEndTime        = (TextView) findViewById(R.id.thoiGianKT);
    }

    private void handleEvent() {

        btnPlayPause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        imgbFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int orientation = PlayVideo.this.getResources().getConfiguration().orientation;
                switch(orientation) {
                    case Configuration.ORIENTATION_PORTRAIT:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }
            }
        });
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;
        mYouTubePlayer.cueVideo(id);
        mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYouTubePlayer.setPlayerStateChangeListener(stateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);
        //youTubePlayer.setOnFullscreenListener(this);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_VIDEO);
        }else {
            Toast.makeText(this, "Video error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_VIDEO){
            //youTubePlayerView.initialize(MainActivity.API_KEY, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
//
//    @SuppressLint("InlinedApi")
//    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
//            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
//
//    @SuppressLint("InlinedApi")
//    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
//            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;




    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            ktEndTouch = false;
            mShowProgress.run();
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onBuffering(boolean isBuffering) {
            ViewGroup ytView = (ViewGroup)youTubePlayerView.getRootView();
            ProgressBar progressBar;
            progressBar = findProgressBar(ytView);
            try {
                // As of 2016-02-16, the ProgressBar is at position 0 -> 3 -> 2 in the view tree of the Youtube Player Fragment
                ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
                ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
                progressBar = (ProgressBar)child2.getChildAt(2);
            } catch (Throwable t) {
                // As its position may change, we fallback to looking for it
                progressBar = findProgressBar(ytView);
                // TODO I recommend reporting this problem so that you can update the code in the try branch: direct access is more efficient than searching for it
            }
            progressBar.setBackgroundColor(Color.MAGENTA);

            int visibility = isBuffering ? View.VISIBLE : View.INVISIBLE;
            if (progressBar != null) {
                progressBar.setVisibility(visibility);
                // Note that you could store the ProgressBar instance somewhere from here, and use that later instead of accessing it again.
            }
        }

        @Override
        public void onSeekTo(int i) {

        }
        private ProgressBar findProgressBar(View view) {
            if (view instanceof ProgressBar) {
                return (ProgressBar) view;
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
    private SeekBar findSeekBar(View view) {
        if (view instanceof SeekBar) {
            return (SeekBar) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                SeekBar res = findSeekBar(viewGroup.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }
    private void visibleChild(ViewGroup viewGroup) {
//        viewGroup.setVisibility(View.VISIBLE);
//        Toast.makeText(this, "vo 1", Toast.LENGTH_SHORT).show();
//        if(viewGroup.getChildCount()>0) {
//            for (int i = 1; i < 3; i++) {
//                Toast.makeText(this, "vo 2", Toast.LENGTH_SHORT).show();
//                viewGroup.getChildAt(i).setVisibility(View.INVISIBLE);
//            }
//        }
        ViewGroup viewGroup1 = (ViewGroup) viewGroup.getChildAt(3);
        viewGroup1.getChildAt(2).setVisibility(View.INVISIBLE);
        
    }





    YouTubePlayer.PlayerStateChangeListener stateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
        }

        @Override
        public void onLoaded(String s) {
            mYouTubePlayer.play();
           // mShowProgress.run();

//            ViewGroup ytView = (ViewGroup)youTubePlayerView.getRootView();
//            visibleChild(ytView);
//            SeekBar seekBar = findSeekBar(ytView);
//            seekBar.setVisibility(View.VISIBLE);

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
            btnPlayPause.setImageResource(R.mipmap.ic_play);
            mYouTubePlayer.pause();
            ktEndTouch = true;
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Toast.makeText(PlayVideo.this, "onError", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        if(ktEndTouch){
            mYouTubePlayer.play();
            btnPlayPause.setImageResource(R.mipmap.ic_pause);
        }else {
            if(mYouTubePlayer != null & !mYouTubePlayer.isPlaying()){
                mYouTubePlayer.play();
                btnPlayPause.setImageResource(R.mipmap.ic_pause);
            }else {
                mYouTubePlayer.pause();
                btnPlayPause.setImageResource(R.mipmap.ic_play);
            }
        }

    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            long duration = mYouTubePlayer.getDurationMillis();
            long newposition = (duration * progress) / 1000L;
            mYouTubePlayer.seekToMillis( (int) newposition);
            if (txtStartTime != null)
                txtStartTime.setText(stringForTime( (int) newposition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging = true;
            //Toast.makeText(PlayVideo.this, "onStartTrackingTouch", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();
            mShowProgress.run();
            //Toast.makeText(PlayVideo.this, "onStopTrackingTouch", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        exit = true;
        mYouTubePlayer.pause();
        //Toast.makeText(this, exit?"Exit = true":"Exit = false", Toast.LENGTH_SHORT).show();
        finish();
        super.onBackPressed();
    }

    private void  timeSong(){
        txtEndTime.setText(stringForTime(mYouTubePlayer.getDurationMillis()));
        seekBar.setMax(1000);
    }


    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mYouTubePlayer == null || mDragging) {
            return 0;
        }

        int position = mYouTubePlayer.getCurrentTimeMillis();
        int duration = mYouTubePlayer.getDurationMillis();
        if (seekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
                //Toast.makeText(PlayVideo.this, "setProgress111111", Toast.LENGTH_SHORT).show();
            }
        }

        if (txtEndTime != null)
            txtEndTime.setText(stringForTime(duration));
        if (txtStartTime != null)
            txtStartTime.setText(stringForTime(position));

        return position;
    }


    private void upDateCurrentTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentTime = (mYouTubePlayer.getCurrentTimeMillis()*1000)/mYouTubePlayer.getDurationMillis();
                seekBar.setProgress(currentTime);
                Toast.makeText(PlayVideo.this, "" + currentTime+ "-" + 1000, Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 1000);

            }
        }, 0);
    }

    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            if(!exit){
                int pos = setProgress();
                final Handler handler = new Handler();
                //Toast.makeText(PlayVideo.this, mYouTubePlayer.isPlaying()?"OK":"NO", Toast.LENGTH_SHORT).show();
                if (!mDragging && mYouTubePlayer.isPlaying()) {
                    handler.postDelayed(mShowProgress, 1000 - (pos % 1000));
                }
            }
        }
    };

    private void loopRunnable(final Runnable runnable){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PlayVideo.this, "DDDĐ", Toast.LENGTH_SHORT).show();
                runnable.run();
                handler.postDelayed(this, 100);
                if (mYouTubePlayer.isPlaying()){
                    handler.removeCallbacks(this);
                }
            }
        }, 0);
    }


}

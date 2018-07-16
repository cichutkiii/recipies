package pl.preclaw.recipies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


import pl.preclaw.recipies.importData.Step;


public class StepDetailActivity extends AppCompatActivity implements Player.EventListener {


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
        this.finish();

    }

    @BindView(R.id.playerView)
    PlayerView playerView;

    @BindView(R.id.descr_previous)
    Button previous;

    @BindView(R.id.descr_next)
    Button next;

    @BindView(R.id.detail_descr)
    TextView detailDescr;

     ArrayList<Step> steps;

    int recipeIndex;
    private static String STEP_INDEX = "index";
    private static String STEP_BUNDLE = "bundle";
    private static String RESUME_VIDEO_POS = "position";
    private static String RESUME_VIDEO_VIS = "visibility";
    private static String RESUME_STEPS_LIST = "list";
    private static String RESUME_STEP_INDEX = "step";
    private static String RESUME_STEP_READY = "ready";

    SimpleExoPlayer mExoPlayer;
    String userAgent;
    Long positionPlayer;
    boolean playWhenReady;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playbackBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        positionPlayer =  0L;
        ButterKnife.bind(this);
        userAgent = Util.getUserAgent(this, "recipies");

        if(savedInstanceState==null){
            Bundle bundle = getIntent().getBundleExtra("REAL");
            if(savedInstanceState ==null){
                if (bundle != null) {
                    steps = bundle.getParcelableArrayList(STEP_BUNDLE);
                    recipeIndex = bundle.getInt(STEP_INDEX);

                }
                initializeMedia();
                setStepData();

            }
        }else{
            recipeIndex = savedInstanceState.getInt(RESUME_STEP_INDEX);
            steps = savedInstanceState.getParcelableArrayList(RESUME_STEPS_LIST);
            positionPlayer = savedInstanceState.getLong(RESUME_VIDEO_POS);
            playWhenReady = savedInstanceState.getBoolean(RESUME_STEP_READY);
            initializeMedia();
            setStepData();

        }




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steps.size() - 1 == recipeIndex) {
                    recipeIndex = 0;

                } else {
                    recipeIndex++;
                }
                initializeMedia();
                releasePlayer();
                setStepData();

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeIndex == 0) {
                    recipeIndex = steps.size() - 1;

                } else {
                    recipeIndex--;
                }
                initializeMedia();
                releasePlayer();
                setStepData();
            }
        });
    }





    private void setStepData() {
        if (!steps.get(recipeIndex).getVideoURL().isEmpty()) {

            playerView.showController();
            playerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(steps.get(recipeIndex).getVideoURL()));

        } else {
            playerView.setVisibility(View.GONE);
            playerView.hideController();
     }

        detailDescr.setText(Html.fromHtml(steps.get(recipeIndex).getDescription()));


    }


    private void initializeMedia() {
        mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "MEDIASESSION");
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playbackBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
            playerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
        }
    }



    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.removeListener(this);
            mExoPlayer.stop();
            mExoPlayer.release();

            mExoPlayer = null;
        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            playbackBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RESUME_VIDEO_VIS, playerView.getVisibility());
        outState.putInt(RESUME_STEP_INDEX, recipeIndex);
        outState.putLong(RESUME_VIDEO_POS, positionPlayer);
        outState.putParcelableArrayList(RESUME_STEPS_LIST, steps);
        outState.putBoolean(RESUME_STEP_READY, playWhenReady);



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            positionPlayer = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            //resuming properly
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(positionPlayer);
        } else {
            initializeMedia();
            initializePlayer(Uri.parse(steps.get(recipeIndex).getVideoURL()));
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(positionPlayer);
        }
    }
    private class SessionCallBacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }


}

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
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
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
import pl.preclaw.recipies.importData.RecipyList;
import pl.preclaw.recipies.importData.Step;

public class StepDetailActivity extends AppCompatActivity implements Player.EventListener{

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

    private ArrayList<Step> steps;
    private int recipeIndex;
    public static String STEP_INDEX = "index";
    public static String STEP_BUNDLE = "bundle";
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private String userAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("REAL");
        RecipyList recipyList = new RecipyList();
        if (bundle != null) {
            steps = bundle.getParcelableArrayList(STEP_BUNDLE);
            recipeIndex = bundle.getInt(STEP_INDEX);


        }
        userAgent = Util.getUserAgent(this, "recipies");

        setStepData();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(steps.size()-1 == recipeIndex){
                    recipeIndex=0;

                }else{
                    recipeIndex++;
                }
                releasePlayer();

                setStepData();

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeIndex ==0){
                    recipeIndex=steps.size()-1;

                }else{
                    recipeIndex--;
                }
                releasePlayer();

                setStepData();
            }
        });
    }


    private void setStepData(){
        if(steps.get(recipeIndex).getVideoURL() != null){
            playerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(steps.get(recipeIndex).getVideoURL()));

        }
        else{
            playerView.setVisibility(View.INVISIBLE);

            playerView.setVisibility(View.GONE);
        }

        detailDescr.setText(Html.fromHtml(steps.get(recipeIndex).getDescription()));


    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,userAgent);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
            playerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }else{
            mExoPlayer.addListener(this);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }
    }
    private void releasePlayer() {
        mExoPlayer.removeListener(this);
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
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

}

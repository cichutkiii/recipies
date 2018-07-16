package pl.preclaw.recipies;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.Unbinder;
import pl.preclaw.recipies.importData.Step;


public class StepDetailFragment extends Fragment implements Player.EventListener{

    @BindView(R.id.playerView_tab)
    PlayerView playerView;
    @BindView(R.id.detail_descr)
    TextView detailDescr;
    Unbinder unbinder;
    private ArrayList<Step> steps;
    private int index;
    private SimpleExoPlayer mExoPlayer;
    private String userAgent;
    private Long positionPlayer;
    private boolean playWhenReady;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playbackBuilder;
    private static String RESUME_STEPS_LIST = "list";
    private static String RESUME_STEP_INDEX = "step";
    private static String RESUME_STEP_READY = "ready";
    private static String RESUME_VIDEO_POS = "position";


    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(String param1, String param2) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        positionPlayer =  0L;
        unbinder = ButterKnife.bind(this, view);
        userAgent = Util.getUserAgent(getContext(), "recipies");
        if(savedInstanceState !=null){
            index = savedInstanceState.getInt(RESUME_STEP_INDEX);
            steps = savedInstanceState.getParcelableArrayList(RESUME_STEPS_LIST);
            positionPlayer = savedInstanceState.getLong(RESUME_VIDEO_POS);
            playWhenReady = savedInstanceState.getBoolean(RESUME_STEP_READY);
        }
        setStepData();

        return view;
    }

    public void setSteps(ArrayList<Step> setStep) {
        steps = setStep;
    }

    public void setStepIndex(int idx) {
        index = idx;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RESUME_STEP_INDEX, index);
        outState.putParcelableArrayList(RESUME_STEPS_LIST, steps);
        outState.putLong(RESUME_VIDEO_POS, positionPlayer);
        outState.putBoolean(RESUME_STEP_READY, playWhenReady);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void setStepData() {
        if (!steps.get(index).getVideoURL().isEmpty()) {

            playerView.showController();
            playerView.setVisibility(View.VISIBLE);
            initializeMedia();
            initializePlayer(Uri.parse(steps.get(index).getVideoURL()));

            if (!(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT))
            {
                hideUI();
                playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                playerView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

        } else {
            detailDescr.setText(Html.fromHtml(steps.get(index).getDescription()));
            playerView.setVisibility(View.GONE);
            playerView.hideController();
        }




    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(trackSelectionFactory));
            playerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
        }
    }

    private void initializeMedia() {
        mediaSessionCompat = new MediaSessionCompat(getContext(), "MEDIASESSION");
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playbackBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(playbackBuilder.build());
        mediaSessionCompat.setCallback(new StepDetailFragment.SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }
    public void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.removeListener(this);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }
    private void hideUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
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
    @Override
    public void onPause() {
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
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            //resuming properly
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(positionPlayer);
        } else {
            initializeMedia();
            initializePlayer(Uri.parse(steps.get(index).getVideoURL()));
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(positionPlayer);
        }
    }
}

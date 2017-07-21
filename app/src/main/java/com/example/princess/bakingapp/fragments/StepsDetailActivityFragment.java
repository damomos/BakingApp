package com.example.princess.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.princess.bakingapp.R;
import com.example.princess.bakingapp.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import static com.example.princess.bakingapp.activities.MainActivity.isTablet;
import static com.example.princess.bakingapp.fragments.StepsActivityFragment.steps;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepsDetailActivityFragment extends Fragment implements ExoPlayer.EventListener{

    private static final String TAG = StepsDetailActivityFragment.class.getSimpleName();

    private TextView fullDescription;
    private ImageView previous;
    private ImageView next;
    private Steps mSteps;
    protected static int index = 0;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView playerView;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private ImageView mNoVideoImageView;

    boolean playWhenReady = false;
    long playbackPosition = 0;
    int currentWindow = 0;

    public StepsDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_detail, container, false);

        fullDescription = (TextView) view.findViewById(R.id.description);
        previous = (ImageView) view.findViewById(R.id.prev);
        next = (ImageView) view.findViewById(R.id.next);
        playerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        mNoVideoImageView = (ImageView) view.findViewById(R.id.no_video_image);
        initializeMediaSession();
        initializePlayer();

        if (!isTablet) {
            index = getActivity().getIntent().getExtras().getInt("item");
        }

        getActivity().setTitle(steps.get(index).getShortDescription());
        fullDescription.setText(steps.get(index).getFullDescription());

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) {
                    index--;
                    fullDescription.setText(steps.get(index).getFullDescription());
                    getActivity().setTitle(steps.get(index).getShortDescription());
                    exoPlayer.stop();
                    initializePlayer();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < steps.size() - 1) {
                    index++;
                    fullDescription.setText(steps.get(index).getFullDescription());
                    getActivity().setTitle(steps.get(index).getShortDescription());
                    exoPlayer.stop();
                    initializePlayer();
                }
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            hideSystemUI();
            playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            fullDescription.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }


        return view;
    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }

    private void initializePlayer() {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);


        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentWindow, playbackPosition);

        //set the video URL
        if (steps.get(index) != null) {
            mSteps = steps.get(index);
            if (mSteps.hasVideo()) {
                mNoVideoImageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(mSteps.getVideoURL());
                MediaSource mediaSource = mediaSource(uri);
                exoPlayer.prepare(mediaSource, true, false);

            } else {
                playerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(R.mipmap.no_video_image).fitCenter().into(mNoVideoImageView);

            }
        }
    }

        private MediaSource mediaSource(Uri uri) {
            return new ExtractorMediaSource(uri,
                    new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null);
        }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        exoPlayer.setPlayWhenReady(false);
        mediaSession.setActive(false);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaSession.setActive(true);
        initializePlayer();
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(playWhenReady);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.setPlayWhenReady(playWhenReady);
            exoPlayer.seekTo(currentWindow, playbackPosition);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            playbackPosition = exoPlayer.getCurrentPosition();
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

}

package com.vabasu.surfaceviewexample;

import java.io.IOException;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class VideoAssetActivity extends Activity implements
		TextureView.SurfaceTextureListener {

	private static final String TAG = VideoAssetActivity.class.getName();

	private static final String FILE_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

	private MediaPlayer mMediaPlayer;

	boolean isPlaying = false;
	private Button playPause;
	private Button restart;
	
	private RelativeLayout videoControlButtons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.texture_video_simple);

		initView();
	}

	private void initView() {

		TextureView textureView = (TextureView) findViewById(R.id.textureView);
		textureView.setSurfaceTextureListener(this);

		playPause = (Button) findViewById(R.id.play_video);
		restart = (Button) findViewById(R.id.restart_video);
		videoControlButtons = (RelativeLayout) findViewById(R.id.video_control_buttons);

		playPause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isPlaying) {
					mMediaPlayer.pause();
					playPause.setText("Play");
				} else {
					mMediaPlayer.start();
					playPause.setText("Pause");
				}
				isPlaying = !isPlaying;
			}
		});

		restart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMediaPlayer.pause();
				mMediaPlayer.seekTo(0);
				mMediaPlayer.start();
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i,
			int i2) {
		Surface surface = new Surface(surfaceTexture);

		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(getApplicationContext(),
					Uri.parse(FILE_URL));
			mMediaPlayer.setSurface(surface);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepareAsync();
			mMediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							isPlaying = true;
							mediaPlayer.start();
							videoControlButtons.setVisibility(View.VISIBLE);
						}
					});

		} catch (IllegalArgumentException e) {
			Log.d(TAG, e.getMessage());
		} catch (SecurityException e) {
			Log.d(TAG, e.getMessage());
		} catch (IllegalStateException e) {
			Log.d(TAG, e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
			int i, int i2) {
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
	}
}

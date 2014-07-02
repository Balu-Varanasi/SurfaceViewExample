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

public class VideoAssetActivity extends Activity implements
		TextureView.SurfaceTextureListener {

	private static final String TAG = VideoAssetActivity.class.getName();

	private static final String FILE_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

	private MediaPlayer mMediaPlayer;

	boolean isPlaying = false;
	private Button playPause;

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
			// AssetFileDescriptor afd = getAssets().openFd(FILE_NAME);
			mMediaPlayer = new MediaPlayer();
			// mMediaPlayer.setDataSource(afd.getFileDescriptor(),
			// afd.getStartOffset(), afd.getLength());

			mMediaPlayer.setDataSource(getApplicationContext(),
					Uri.parse(FILE_URL));
			mMediaPlayer.setSurface(surface);
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepareAsync();

			// Play video when the media source is ready for playback.
			mMediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							isPlaying = true;
							mediaPlayer.start();
							playPause.setVisibility(View.VISIBLE);
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

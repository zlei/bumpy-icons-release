package com.zlei.flappypipe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sessionm.api.SessionM;
import com.sessionm.api.SessionM.ActivityType;
import com.sessionm.api.SessionM.State;

public class MainActivity extends Activity {
	public static final float DEFAULT_VOLUME = 0.3f;
	public static float volume = DEFAULT_VOLUME;
	private String PATH;
	public static String[] s;
	public boolean musicShouldPlay = true;
	public static MediaPlayer musicPlayer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PATH = Environment.getExternalStorageDirectory().getPath() + "/flappy";
		this.loadLearnFile();

		((ImageButton) findViewById(R.id.play_button)).setImageBitmap(Sprite
				.createBitmap(getResources().getDrawable(R.drawable.play_pipe),
						this));
		((ImageButton) findViewById(R.id.play_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Game.class);
						i.putExtra("mode", "pipe");
						startActivity(i);
					}
				});

		((ImageButton) findViewById(R.id.flyer_button))
				.setImageBitmap(Sprite
						.createBitmap(
								getResources().getDrawable(
										R.drawable.play_flyer), this));
		((ImageButton) findViewById(R.id.flyer_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Game.class);
						i.putExtra("mode", "flyer");
						startActivity(i);
					}
				});
		((ImageButton) findViewById(R.id.compete_button)).setImageBitmap(Sprite
				.createBitmap(
						getResources().getDrawable(R.drawable.compete_button),
						this));
		((ImageButton) findViewById(R.id.compete_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Game.class);
						i.putExtra("mode", "compete");
						startActivity(i);
					}
				});
		((ImageButton) findViewById(R.id.learn_button)).setImageBitmap(Sprite
				.createBitmap(
						getResources().getDrawable(R.drawable.learn_button),
						this));
		((ImageButton) findViewById(R.id.learn_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Game.class);
						i.putExtra("mode", "learn");
						startActivity(i);
					}
				});

		findViewById(R.id.voice_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (SessionM.getInstance().getSessionState() == State.STARTED_ONLINE) {
							SessionM.getInstance().presentActivity(
									ActivityType.PORTAL);
						} else {
							Toast.makeText(
									MainActivity.this,
									"SessionM Portal is currently unavailable.",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		/**((ImageButton) findViewById(R.id.voice_button)).setImageBitmap(Sprite
				.createBitmap(getResources().getDrawable(R.drawable.voice),
						this));
		((ImageButton) findViewById(R.id.voice_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this, Game.class);
						i.putExtra("mode", "sound");
						startActivity(i);
					}
				});
		initMusicPlayer();
		*/
	}

	public void initMusicPlayer() {
		if (musicPlayer == null) {
			musicPlayer = MediaPlayer.create(this, R.raw.iml);
			musicPlayer.setLooping(true);
			// musicPlayer.setVolume(MainActivity.volume, MainActivity.volume);
		}

		musicPlayer.seekTo(0); // Reset song to position 0
		if (musicShouldPlay) {
			musicPlayer.start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SessionM.getInstance().onActivityResume(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	private boolean loadLearnFile() {
		try {
			File f = new File(PATH + "/learn");
			FileInputStream is = new FileInputStream(f);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String str = new String(buffer);
			// each cell
			String s[] = str.split("\n");
			s[0].replace("\"", "");
			s[s.length - 1].replace("\"", "");
			// System.out.println("finished!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean updateLearnFile() {
		File logFile = new File(PATH);
		logFile.mkdirs();

		File learnFile = new File(PATH + "/learn");
		try {
			// learnFile.delete();
			learnFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter file = new FileWriter(PATH + "/learn");
			for (int i = 0; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					for (int k = 0; k < 2; k++) {
						// System.out.println(QLearning.Q[i][j][k]);
						file.write(String.valueOf(QLearning.Q[i][j][k]) + "\n");
					}
				}

			}
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
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
import com.zlei.flappypipe.Game.GameMode;

public class MainActivity extends Activity implements OnClickListener {
	public static final float DEFAULT_VOLUME = 0.3f;
	public static float volume = DEFAULT_VOLUME;
	private String PATH;
	public static String[] s;
	public boolean musicShouldPlay = true;
	public static MediaPlayer musicPlayer = null;

	private static ImageButton playBtn;
	private static ImageButton flyerBtn;
	private static ImageButton competeBtn;
	private static ImageButton learnBtn;
	private static ImageButton voiceBtn;
	private static ImageButton rewardsBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PATH = Environment.getExternalStorageDirectory().getPath() + "/flappy";
		this.loadLearnFile();

		playBtn = (ImageButton) findViewById(R.id.play_button);
		flyerBtn = (ImageButton) findViewById(R.id.flyer_button);
		competeBtn = (ImageButton) findViewById(R.id.compete_button);
		learnBtn = (ImageButton) findViewById(R.id.learn_button);
		voiceBtn = (ImageButton) findViewById(R.id.voice_button);
		rewardsBtn = (ImageButton) findViewById(R.id.reward_button);

		playBtn.setOnClickListener(this);
		flyerBtn.setOnClickListener(this);
		competeBtn.setOnClickListener(this);
		learnBtn.setOnClickListener(this);
		voiceBtn.setOnClickListener(this);
		rewardsBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(MainActivity.this, Game.class);
		int id = v.getId();
		switch (id) {
		case R.id.play_button:
			Game.MODE = GameMode.Pipe;
			// i.putExtra("mode", GameMode.Pipe);
			break;
		case R.id.flyer_button:
			Game.MODE = GameMode.Flyer;
			// i.putExtra("mode", GameMode.Flyer);
			break;
		case R.id.compete_button:
			Game.MODE = GameMode.Compete;
			// i.putExtra("mode", GameMode.Compete);
			break;
		case R.id.learn_button:
			Game.MODE = GameMode.Learn;
			// i.putExtra("mode", GameMode.Learn);
			break;
		case R.id.voice_button:
			Game.MODE = GameMode.Voice;
			// i.putExtra("mode", GameMode.Voice);
			break;
		// rewards button
		case R.id.reward_button:
			if (SessionM.getInstance().getSessionState() == State.STARTED_ONLINE) {
				SessionM.getInstance().presentActivity(ActivityType.PORTAL);
			} else {
				Toast.makeText(MainActivity.this,
						"SessionM Portal is currently unavailable.",
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		startActivity(i);
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
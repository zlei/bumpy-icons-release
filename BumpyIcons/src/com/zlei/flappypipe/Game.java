/**
 * The Game
 */

package com.zlei.flappypipe;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

public class Game extends Activity {
	public static SoundPool soundPool = new SoundPool(5,
			AudioManager.STREAM_MUSIC, 0);
	private MyHandler handler;
	public static int mode;
	GameView view;
	GameOverDialog gameOverDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		// decide game type

		if (bundle.getString("mode").equals("flyer"))
			mode = 0;
		else if (bundle.getString("mode").equals("pipe"))
			mode = 1;
		else if (bundle.getString("mode").equals("learn"))
			mode = 2;
		else if (bundle.getString("mode").equals("compete"))
			mode = 3;
		else if (bundle.getString("mode").equals("sound")) {
			mode = 0;
		}
		view = new GameView(this, true);
		if (bundle.getString("mode").equals("sound"))
			view.allowSound = true;

		gameOverDialog = new GameOverDialog(this);
		handler = new MyHandler(this);
		setLayouts();
	}

	/**
	 * Creates the layout containing a layout for ads and the GameView
	 */
	private void setLayouts() {
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.addView(view);
		setContentView(mainLayout);
	}

	@Override
	protected void onPause() {
		view.pause();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		view.restart();
		super.onRestart();
	}

	/**
	 * Resumes the view (but waits the view waits for a tap) and starts the
	 * music if it should be running. Also checks whether the Google Play
	 * Services are available.
	 */
	@Override
	protected void onResume() {
		view.resumeAndKeepRunning();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Sends the handler the command to show the GameOverDialog. Because it
	 * needs an UI thread.
	 */
	public void gameOver() {
		// points = view.getPoints();
		handler.sendMessage(Message.obtain(handler, 0));
	}

	/**
	 * Shows the GameOverDialog when a message with code 0 is received.
	 */
	static class MyHandler extends Handler {
		private Game game;

		public MyHandler(Game game) {
			this.game = game;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				game.gameOverDialog.init();
				game.gameOverDialog.show();
				break;
			}
		}
	}
}
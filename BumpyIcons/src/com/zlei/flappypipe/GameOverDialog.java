package com.zlei.flappypipe;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverDialog extends Dialog {
	private Game game;

	/** Name of the SharedPreference that saves the score */
	public static String score_save_name = "score_save";

	/** Key that saves the score */
	public static String best_score_key = "score";

	private TextView tvCurrentScoreVal;
	private TextView tvBestScoreVal;

	public GameOverDialog(Game game) {
		super(game);
		this.game = game;
		this.setContentView(R.layout.gameover);
		this.setCancelable(false);

		tvCurrentScoreVal = (TextView) findViewById(R.id.tv_current_score_value);
		tvBestScoreVal = (TextView) findViewById(R.id.tv_best_score_value);
	}

	public void init() {
		Button b_continue = (Button) findViewById(R.id.b_continue);
		Button b_finish = (Button) findViewById(R.id.b_finish);
		manageScore(GameView.getPoints());
		b_continue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				GameView.resetPoints();
				game.onRestart();
			}
		});

		b_finish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				// Intent i = new Intent(game, MainActivity.class);
				// game.startActivity(i);
				game.view.gameOver = true;
				game.finish();
			}
		});
	}

	private void manageScore(int points) {
		String real_best_score_key = best_score_key
				+ Integer.toString(Game.mode);
		SharedPreferences saves = game.getSharedPreferences(score_save_name, 0);
		int oldPoints = saves.getInt(real_best_score_key, 0);
		// pipe mode: need to find lowest score
		if (Game.mode == 1) {
			if (oldPoints == 0)
				oldPoints = Integer.MAX_VALUE;
			if (points < oldPoints) {
				SharedPreferences.Editor editor = saves.edit();
				oldPoints = points;
				editor.putInt(real_best_score_key, points);
				tvBestScoreVal.setTextColor(Color.RED);
				editor.commit();
			}
		}
		// all the other modes, normal score system
		else {
			if (points > oldPoints) {
				// Save new highscore
				SharedPreferences.Editor editor = saves.edit();
				oldPoints = points;
				editor.putInt(real_best_score_key, points);
				tvBestScoreVal.setTextColor(Color.RED);
				editor.commit();
			}
		}
		tvCurrentScoreVal.setText("" + points);
		tvBestScoreVal.setText("" + oldPoints);
	}
}
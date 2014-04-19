package com.zlei.flappypipe;

import com.zlei.flappypipe.R;

import android.graphics.Bitmap;

public class PipeUp extends Sprite {
	public double [] m_state = {0, 0}; // [0]:vertical_distance [1]:horizontal_distance
	public double [] m_state_dash = {0, 0};
	public double [] vertical_dist_range = {0, 2000};
	public double [] horizontal_dist_range = {0, 900};  
	public double explore = 0;
	public int action_to_perform = 1;
	public double resolution = 4;
	public double alpha_QL = 0.7;  
	public int reward = 1; 

	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;

	public PipeUp(GameView view, Game game) {
		super(view, game);
		if (globalBitmap == null) {
			globalBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.pipe_up));
		}
		this.bitmap = globalBitmap;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
	}

	/**
	 * Sets the position
	 * 
	 * @param x
	 * @param y
	 */
	public void init(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

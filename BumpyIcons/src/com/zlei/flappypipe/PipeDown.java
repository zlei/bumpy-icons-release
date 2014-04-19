package com.zlei.flappypipe;

import com.zlei.flappypipe.R;

import android.graphics.Bitmap;

public class PipeDown extends Sprite {

	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;

	public PipeDown(GameView view, Game game) {
		super(view, game);
		if (globalBitmap == null) {
			globalBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.pipe_down));
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

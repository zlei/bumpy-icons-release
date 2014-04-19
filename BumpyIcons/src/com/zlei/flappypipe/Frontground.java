/**
 * Manages the bitmap at the front
 */

package com.zlei.flappypipe;

import com.zlei.flappypipe.R;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Frontground extends Background {
	/**
	 * Height of the ground relative to the height of the bitmap
	 */
	public static final float GROUND_HEIGHT = (1f * /* 45 */35) / 720;

	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;

	public Frontground(GameView view, Game game) {
		super(view, game);
		if (globalBitmap == null) {
			BitmapDrawable bd = (BitmapDrawable) game.getResources()
					.getDrawable(R.drawable.fg_new);
			globalBitmap = bd.getBitmap();
		}
		this.bitmap = globalBitmap;
	}

}

package com.zlei.flappypipe;

/**
 * To generator the icons. 
 */

import android.graphics.Bitmap;

public class Icons extends PlayableCharacter {
	public static Bitmap iconBitmap;

	public enum IconType {
		Chrome, Safari, Firefox, IE
	};

	/**
	 * Constructor for different icons
	 * 
	 * @param view
	 * @param game
	 * @param type
	 *            : string name of types
	 */
	public Icons(GameView view, Game game) {
		super(view, game);
		iconBitmap = null;
		switch (GameView.ICONTYPE) {
		case Chrome:
			iconBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.chrome));
			break;
		case Safari:
			iconBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.safari));
			break;
		case Firefox:
			iconBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.firefox));
			break;
		case IE:
			iconBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.ieie));
			break;
		}

		this.bitmap = iconBitmap;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
		int rand = (int) (Math.random() * height * 2);
		this.x = this.view.getWidth() / 6 + rand;
		rand = (int) (Math.random() * height * 2);
		this.y = game.getResources().getDisplayMetrics().heightPixels / 2
				+ rand;
	}

	@Override
	public void onTap() {
		super.onTap();
	}
}
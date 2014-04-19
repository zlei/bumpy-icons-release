package com.zlei.flappypipe;

import android.graphics.Bitmap;

public class FireFox extends PlayableCharacter {
	public static Bitmap globalBitmap;

	public FireFox(GameView view, Game game) {
		super(view, game);

		gravity = 200;

		if (globalBitmap == null) {
			globalBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.firefox));
		}

		this.bitmap = globalBitmap;
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
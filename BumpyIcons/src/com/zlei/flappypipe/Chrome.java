package com.zlei.flappypipe;

import com.zlei.flappypipe.R;

import android.graphics.Bitmap;

public class Chrome extends PlayableCharacter {
	public static Bitmap globalBitmap;

	public Chrome(GameView view, Game game) {
		super(view, game);

		if (globalBitmap == null) {
			globalBitmap = createBitmap(game.getResources().getDrawable(
					R.drawable.chrome));
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
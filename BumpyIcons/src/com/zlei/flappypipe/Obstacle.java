/**
 * Obstacles
 */

package com.zlei.flappypipe;

import android.graphics.Canvas;

public class Obstacle extends Sprite{
	public PipeDown pipe_down;
	public PipeUp pipe_up;
	
	/** Necessary so the onPass method is just called once */
	public boolean isAlreadyPassed = false;

	public Obstacle(GameView view, Game game, int y) {
		super(view, game);
		pipe_down = new PipeDown(view, game);
		pipe_up = new PipeUp(view, game);
		
		initPos(y);
	}
	
	/**
	 * With a certain gap between them.
	 * The vertical position is in a certain area random.
	 */
	private void initPos(int touchY){
		int height = game.getResources().getDisplayMetrics().heightPixels;
		int gab = height / 4 - view.getSpeedX();
		
		if(gab < height / 5){
			gab = height / 5;
		}
		
		int y = (int) (Math.random() * height * 2 / 5);
		int y1, y2;
		
		if (touchY == -1) {
			y1 = height / 12 + y - pipe_down.height;
			y2 = height / 12 + y + gab;
		} else {
			touchY = Math.max(Math.min(touchY, height * 5 / 6), height / 6);
			
			y1 = touchY - pipe_down.height - height / 6;
			y2 = touchY + gab - height / 6;
		}
		
		pipe_down.init(game.getResources().getDisplayMetrics().widthPixels, y1);
		pipe_up.init(game.getResources().getDisplayMetrics().widthPixels, y2);
	}

	/**
	 * Draws pipes.
	 */
	@Override
	public void draw(Canvas canvas) {
		pipe_down.draw(canvas);
		pipe_up.draw(canvas);
	}

	/**
	 * Checks whether pipes are out of range.
	 */
	@Override
	public boolean isOutOfRange() {
		return pipe_down.isOutOfRange() && pipe_up.isOutOfRange();
	}

	/**
	 * Checks whether the pipes are colliding with the sprite.
	 */
	@Override
	public boolean isColliding(Sprite sprite) {
		return pipe_down.isColliding(sprite) || pipe_up.isColliding(sprite);
	}

	/**
	 * Moves both pipes.
	 */
	@Override
	public void move() {
		pipe_down.move();
		pipe_up.move();
	}

	/**
	 * Sets the speed of the pipe_down and the pipe_up.
	 */
	@Override
	public void setSpeedX(float speedX) {
		pipe_down.setSpeedX(speedX);
		pipe_up.setSpeedX(speedX);
	}
	
	/**
	 * Checks whether the pipe_down and the pipe_up are passed.
	 */
	@Override
	public boolean isPassed(){
		return pipe_down.isPassed() && pipe_up.isPassed();
	}
	
	/**
	 * Will call obstaclePassed of the game, if this is the first pass of this obstacle.
	 */
	@Override
	public void onPass(){
		if(!isAlreadyPassed){
			isAlreadyPassed = true;
		}
	}

}

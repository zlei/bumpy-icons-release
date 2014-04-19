/**
 * GameView
 */

package com.zlei.flappypipe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.zlei.flappypipe.Game.GameMode;
import com.zlei.flappypipe.Icons.IconType;

public class GameView extends SurfaceView implements Runnable, OnTouchListener {
	public static long UPDATE_INTERVAL = 10;
	public int numOfIcons = 1, numOfLearners = 100;
	private Thread thread;
	private SurfaceHolder holder;
	private int obstacleY = -1;
	private boolean deleteAll = false;
	volatile private boolean shouldRun = false;
	public boolean allowLearning = false;
	private static int points = 0;
	boolean gameOver = false;
	private int bestScore = 0;

	private Game game;
	private Background bg;
	private Frontground fg;
	private List<PlayableCharacter> players = new ArrayList<PlayableCharacter>();
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private SoundMeter sound = new SoundMeter();
	boolean allowSound = false;
	public static IconType ICONTYPE;

	/**
	 * create icons
	 * 
	 * @param r2
	 * @return
	 */
	public PlayableCharacter createIcon(int r2) {
		int r = 0;

		if (r2 == -1)
			r = (int) Math.ceil(Math.random() * 4);
		else
			r = r2;
		switch (r) {
		case 1:
			ICONTYPE = IconType.Firefox;
			break;
		case 2:
			ICONTYPE = IconType.IE;
			break;
		case 3:
			ICONTYPE = IconType.Safari;
			break;
		case 4:
			ICONTYPE = IconType.Chrome;
			break;
		}
		return new Icons(this, game);
	}

	public GameView(Context context, boolean playable) {
		super(context);
		this.game = (Game) context;

		switch (Game.MODE) {
		case Flyer:
			numOfIcons = 1;
			break;
		case Pipe:
			numOfIcons = 1;
			break;
		case Compete:
			numOfIcons = 4;
			break;
		case Learn:
			allowLearning = true;
			numOfIcons = this.numOfLearners;
			break;
		case Voice:
			allowSound = true;
			numOfIcons = 1;
			break;
		}

		holder = getHolder();

		if (Game.MODE.equals(GameMode.Compete)) {
			players.add(createIcon(4));
			players.add(createIcon(1));
			players.add(createIcon(2));
			players.add(createIcon(3));
			players.get(0).isPlayer = true;
		} else
			for (int i = 0; i < numOfIcons; i++)
				players.add(createIcon(-1));
		bg = new Background(this, game);
		fg = new Frontground(this, game);

		setOnTouchListener(this);
		if (allowSound)
			sound.start();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (shouldRun == false) {
				shouldRun = true;
				deleteAll = true;
			}

			if (allowLearning) {
				gameOver = true;
				resetPoints();
				game.finish();
			}

			if (Game.MODE.equals(GameMode.Pipe)
					|| Game.MODE.equals(GameMode.Learn))
				obstacleY = (int) event.getY();
			else
				this.players.get(0).onTap();
		}

		return true;
	}

	/**
	 * The thread runs this method
	 */
	int looptime = 0;

	public void run() {
		draw();

		while (!gameOver) {
			if (allowSound && sound.getAmplitude() > 1000 && looptime++ < 1000) {
				looptime = 0;

				if (shouldRun == false) {
					shouldRun = true;
				}

				if (Game.MODE.equals(GameMode.Pipe)
						|| Game.MODE.equals(GameMode.Learn))
					;
				else if (players.size() > 0)
					this.players.get(0).onTap();
			}

			if (deleteAll) {
				if (Game.MODE.equals(GameMode.Flyer))
					players.get(0).isPlayer = true;

				deleteAll = false;
			}

			if (shouldRun) {
				checkPasses();
				checkOutOfRange();
				checkCollision();
				createObstacle();
				QLearning.learnAndPerform(players, obstacles);
				move();
				draw();

				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (allowLearning) {
					checkPasses();
					checkOutOfRange();
					checkCollision();
					createObstacle();
					QLearning.learnAndPerform(players, obstacles);
					move();

					if (points > 20) {
						draw();

						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Joins the thread
	 */
	public void pause() {
		if (allowSound)
			sound.stop();

		while (thread != null) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread = null;
	}

	/**
	 * Activates the thread. But shouldRun will be false. This means the canvas
	 * will be drawn once. If this is the first start of a game, the tutorial
	 * will be drawn.
	 */
	public void resume() {
		pause(); // make sure the old thread isn't running
		if (allowSound)
			sound.start();
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Start the thread and let it run.
	 */
	public void resumeAndKeepRunning() {
		pause(); // make sure the old thread isn't running
		if (allowSound)
			sound.start();
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Draws all gameobjects on the surface
	 */
	private void draw() {
		while (!holder.getSurface().isValid()) {/* wait */
		}
		Canvas canvas = holder.lockCanvas();
		drawCanvas(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	/**
	 * Draws all gameobjects on the canvas
	 * 
	 * @param canvas
	 */
	private void drawCanvas(Canvas canvas) {
		bg.draw(canvas);

		for (Obstacle r : obstacles)
			r.draw(canvas);

		for (PlayableCharacter player : players)
			player.draw(canvas);

		fg.draw(canvas);

		// Score Text
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(getScoreTextMetrics());
		canvas.drawText(
				game.getResources().getString(R.string.onscreen_score_text)
						+ points, getScoreTextMetrics(), getScoreTextMetrics(),
				paint);
	}

	/**
	 * Let the player fall to the ground
	 */
	private void playerDeadFall(PlayableCharacter player) {
		player.dead();
	}

	/**
	 * Checks whether a obstacle is passed.
	 */
	private void checkPasses() {
		for (Obstacle o : obstacles) {
			if (o.isPassed()) {
				if (!o.isAlreadyPassed) {
					o.onPass();
					points++;

					if (points > bestScore)
						bestScore = points;
				}
			}
		}
	}

	/**
	 * Checks whether the obstacles or powerUps are out of range and deletes
	 * them
	 */
	private void checkOutOfRange() {
		for (int i = 0; i < obstacles.size(); i++) {
			if (this.obstacles.get(i).isOutOfRange()) {
				this.obstacles.remove(i);
				i--;
			}
		}
	}

	/**
	 * Checks collisions and performs the action
	 */
	private void checkCollision() {
		boolean allDead = true;

		for (Obstacle o : obstacles) {
			for (int i = players.size() - 1; i >= 0; i--) {
				PlayableCharacter player = players.get(i);
				if (!player.isDead && o.isColliding(player)) {
					o.onCollision();
					player.isDead = true;
					playerDeadFall(player);
				}
			}
		}

		for (int i = players.size() - 1; i >= 0; i--) {
			PlayableCharacter player = players.get(i);

			if (!player.isTouchingGround())
				allDead = false;
			else
				player.setX(-100);

			if (!player.isDead) {
				if (player.isTouchingEdge()) {
					player.isDead = true;
					playerDeadFall(player);
				}
			}
		}

		if (allDead) {
			obstacles.removeAll(obstacles);
			players.removeAll(players);

			if (!allowLearning)
				setOnTouchListener(null);

			if (allowLearning) {
				for (int i = 0; i < numOfLearners; i++) {
					players.add(createIcon(-1));
				}

				shouldRun = false;
				draw();

				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else
				gameOver();
		}
	}

	public void restart() {
		if (Game.MODE.equals(GameMode.Compete)) {
			players.add(createIcon(4));
			players.add(createIcon(1));
			players.add(createIcon(2));
			players.add(createIcon(3));
			players.get(0).isPlayer = true;
		} else
			for (int i = 0; i < numOfIcons; i++)
				players.add(createIcon(-1));

		shouldRun = false;
		resetPoints();
		draw();
		setOnTouchListener(this);

		try {
			Thread.sleep(UPDATE_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * if no obstacle is present a new one is created
	 */
	private void createObstacle() {
		if (obstacles.size() < 1) {
			obstacles.add(new Obstacle(this, game, obstacleY));
			obstacleY = -1;
		}

		else if (obstacles.size() == 1) {
			Obstacle obs = obstacles.get(0);

			if (obs.pipe_up.getX() < 5 * getWidth() / 13) {
				obstacles.add(new Obstacle(this, game, obstacleY));
				obstacleY = -1;
			}
		}
	}

	/**
	 * Update sprite movements
	 */
	private void move() {
		for (Obstacle o : obstacles) {
			o.setSpeedX(-getSpeedX());
			o.move();
		}

		bg.setSpeedX(-getSpeedX() / 2);
		bg.move();

		fg.setSpeedX(-getSpeedX() * 4 / 3);
		fg.move();

		for (PlayableCharacter player : players) {
			if (!player.isTouchingGround())
				player.move();
		}
	}

	/**
	 * return the speed of the obstacles/cow
	 */
	public int getSpeedX() {
		// 16 @ 720x1280 px
		int speedDefault = this.getWidth() / 90;
		// 1,2 every 4 points @ 720x1280 px
		// int speedIncrease = (int) (this.getWidth() / 600f *
		// (game.accomplishmentBox.points / 4));

		int speed = speedDefault;

		if (speed > 2 * speedDefault) {
			return 2 * speedDefault;
		} else {
			return speed;
		}
	}

	/**
	 * Let's the player fall down dead, makes sure the runcycle stops and
	 * invokes the next method for the dialog and stuff.
	 */
	public void gameOver() {
		this.shouldRun = false;
		game.gameOver();
	}

	/**
	 * A value for the position and size of the onScreen score Text
	 */
	public int getScoreTextMetrics() {
		return this.getHeight() / 20;
	}

	public PlayableCharacter getPlayer() {
		if (players.size() > 0)
			return this.players.get(0);
		return null;
	}

	public Game getGame() {
		return this.game;
	}

	public static int getPoints() {
		return points;
	}

	public static void resetPoints() {
		points = 0;
	}
}
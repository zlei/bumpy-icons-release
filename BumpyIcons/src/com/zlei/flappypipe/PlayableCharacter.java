package com.zlei.flappypipe;

public abstract class PlayableCharacter extends Sprite {
	public boolean isDead = false;
	public boolean isPlayer = false;
	 
	public double[] m_state = { 0, 0 }; 
	// [0]:vertical_distance
	// [1]:horizontal_distance
	public double[] m_state_dash = { 0, 0 };
	public double explore = 0;
	public int action_to_perform = 1;
	public double resolution = 4;
	public double alpha_QL = 0.7;
	public double[] vertical_dist_range = { -350, 190 };
	public double[] horizontal_dist_range = { 0, 180 };
	public int reward = 1; 
	
	public PlayableCharacter(GameView view, Game game) {
		super(view, game);
	}

	public double[][][] getQ() {
		return null;
	}
	
	/**
	 * Calls super.move Moves the character to 1/6 of the horizontal screen
	 * Manages the speed changes -> Falling
	 */

	protected double bumprate = 0.7;

	@Override
	public void move() {
		if (isDead)
			setSpeedX(-view.getSpeedX());

		if (speedY < 0) {
			speedY = (float) (speedY * bumprate + getSpeedTimeDecrease() / 2);
		} else {
			this.speedY += getSpeedTimeDecrease();
		}

		super.move();
	}

	/**
	 * A dead character falls slowly to the ground.
	 */
	public void dead() {
	}

	/**
	 * Let the character flap up.
	 */
	public void onTap() {
		this.speedY = getTabSpeed();
		this.y += getPosTabIncrease();
	}

	/**
	 * Falling speed limit
	 * 
	 * @return
	 */
	protected float getMaxSpeed() {
		// 25 @ 720x1280 px
		return view.getHeight() / 51.2f;
	}

	protected int gravity = 240;

	/**
	 * Every run cycle the speed towards the ground will increase.
	 * 
	 * @return
	 */
	protected float getSpeedTimeDecrease() {
		// 4 @ 720x1280 px
		return view.getHeight() / gravity;
	}

	/**
	 * The character gets this speed when taped.
	 * 
	 * @return
	 */
	protected float getTabSpeed() {
		// -80 @ 720x1280 px
		return -view.getHeight() / 16f;
	}

	/**
	 * The character jumps up the pixel height of this value.
	 * 
	 * @return
	 */
	protected int getPosTabIncrease() {
		// -12 @ 720x1280 px
		return -view.getHeight() / 100;
	}
}

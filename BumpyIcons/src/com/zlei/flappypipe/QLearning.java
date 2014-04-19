package com.zlei.flappypipe;

import java.util.List;

public class QLearning {
	private static QLearning singleton = new QLearning();
	public static double[][][] Q;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private QLearning() {
		int width = 500;// Math.max(getWidth(), getHeight());
		Q = new double[width][width][2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < 2; k++) {
					Q[i][j][k] = 0.0;
				}
			}
		}
	}

	public static void setQ(String[] s) {
		int width = 500;// Math.max(getWidth(), getHeight());
		Q = new double[width][width][2];
		int n = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < 2; k++) {
					System.out.println(s[n]);
					Q[i][j][k] = Double.parseDouble(s[n]);
					n++;
				}
			}
		}
	}

	/* Static 'instance' method */
	public static QLearning getInstance() {
		return singleton;
	}

	/* Other methods protected by singleton-ness */
	protected static void demoMethod() {
	}
 
	public static void learnAndPerform(List<PlayableCharacter> players,
			List<Obstacle> obstacles) {
		for (PlayableCharacter player : players) {
			if (player.isDead) {
				if (player.reward == -1000)
					return;
				player.reward = -1000;
			} else {
				player.reward = 1;
			}

			// Step 2: Observe State S'
			double horizontal_distance = 9999;
			double vertical_distance = 9999;

			for (Obstacle obs : obstacles) {
				PipeUp pipe = obs.pipe_up;
				if (pipe.getX() + pipe.getWidth() >= player.getX()) {
					int diff = (pipe.getX() + pipe.getWidth() - player.getX());
					if (horizontal_distance > diff) {
						horizontal_distance = diff;
						vertical_distance = (player.getY() - pipe.getY());
					}
				}
			}

			player.m_state_dash[0] = vertical_distance;
			player.m_state_dash[1] = horizontal_distance;

			// Step 3: Update Q(S, A)
			int state_bin_v = (int) Math.max(Math.min(Math
					.floor((player.vertical_dist_range[1]
							- player.vertical_dist_range[0] - 1)
							/ player.resolution), Math
					.floor((player.m_state[0] - player.vertical_dist_range[0])
							/ player.resolution)), 0);

			int state_bin_h = (int) Math
					.max(Math.min(
							Math.floor((player.horizontal_dist_range[1]
									- player.horizontal_dist_range[0] - 1)
									/ player.resolution),
							Math.floor((player.m_state[1] - player.horizontal_dist_range[0])
									/ player.resolution)), 0);

			int state_dash_bin_v = (int) Math
					.max(Math.min(
							Math.floor((player.vertical_dist_range[1]
									- player.vertical_dist_range[0] - 1)
									/ player.resolution),
							Math.floor((player.m_state_dash[0] - player.vertical_dist_range[0])
									/ player.resolution)), 0);

			int state_dash_bin_h = (int) Math
					.max(Math.min(
							Math.floor((player.horizontal_dist_range[1]
									- player.horizontal_dist_range[0] - 1)
									/ player.resolution),
							Math.floor((player.m_state_dash[1] - player.horizontal_dist_range[0])
									/ player.resolution)), 0);

			double click_v = QLearning.Q[state_dash_bin_v][state_dash_bin_h][0];
			double do_nothing_v = QLearning.Q[state_dash_bin_v][state_dash_bin_h][1];
			double V_s_dash_a_dash = Math.max(click_v, do_nothing_v);

			double Q_s_a = QLearning.Q[state_bin_v][state_bin_h][player.action_to_perform];
			QLearning.Q[state_bin_v][state_bin_h][player.action_to_perform] = Q_s_a
					+ player.alpha_QL
					* (player.reward + V_s_dash_a_dash - Q_s_a);

			// Step 4: S <- S'
			player.m_state = new double[2];
			player.m_state[0] = player.m_state_dash[0];

			// Step 1: Select and perform Action A
			if (Math.random() < player.explore) {
				int rand = (Math.random() < 0.5) ? 0 : 1;
				player.action_to_perform = rand == 0 ? 0 : 1;
			} else {
				state_bin_v = (int) Math
						.max(Math.min(
								Math.floor((player.vertical_dist_range[1]
										- player.vertical_dist_range[0] - 1)
										/ player.resolution),
								Math.floor((player.m_state[0] - player.vertical_dist_range[0])
										/ player.resolution)), 0);

				state_bin_h = (int) Math
						.max(Math.min(
								Math.floor((player.horizontal_dist_range[1]
										- player.horizontal_dist_range[0] - 1)
										/ player.resolution),
								Math.floor((player.m_state[1] - player.horizontal_dist_range[0])
										/ player.resolution)), 0);

				// [0]: click; [1]: do_nothing
				click_v = QLearning.Q[state_bin_v][state_bin_h][0];
				do_nothing_v = QLearning.Q[state_bin_v][state_bin_h][1];
				player.action_to_perform = click_v > do_nothing_v ? 0 : 1;
			}
 
			if (player.action_to_perform == 0 && !player.isPlayer) {
				player.onTap();
			}
		}

		// System.out.println(QLearning.Q[100][50][0]);
	}

	public static double[][][] Q2; 

	public static void learnAndPerform2(List<PlayableCharacter> players,
			List<Obstacle> obstacles) { 
		for (PlayableCharacter player : players) {
			int reward = -10;
			if (player.isDead) 
				reward = 10;

			// Step 2: Observe State S'  
			PipeUp pipe = null;
			for (Obstacle obs : obstacles) 
				pipe = obs.pipe_up; 

			if (pipe == null)
				return;

			pipe.m_state_dash[0] = pipe.getY();
			pipe.m_state_dash[1] = player.getY();

			// Step 3: Update Q(S, A)
			int state_bin_v = (int) Math.max(Math.min(Math
					.floor((pipe.vertical_dist_range[1])
							/ pipe.resolution), Math
							.floor((pipe.m_state[0])
									/ pipe.resolution)), 0);

			int state_bin_h = (int) Math
					.max(Math.min(
							Math.floor((pipe.horizontal_dist_range[1])
									/ pipe.resolution),
									Math.floor((pipe.m_state[1])
											/ pipe.resolution)), 0);

			int state_dash_bin_v = (int) Math
					.max(Math.min(
							Math.floor((pipe.vertical_dist_range[1])
									/ pipe.resolution),
									Math.floor((pipe.m_state_dash[0])
											/ pipe.resolution)), 0);

			int state_dash_bin_h = (int) Math
					.max(Math.min(
							Math.floor((pipe.horizontal_dist_range[1])
									/ pipe.resolution),
									Math.floor((pipe.m_state_dash[1])
											/ pipe.resolution)), 0);

			double max = -99999;
			for (int i=0; i<10; i++) {
				double temp = QLearning.Q2[state_dash_bin_v][state_dash_bin_h][i];
				if (temp > max)
					max = temp; 
			}
			double Q_s_a = QLearning.Q2[state_bin_v][state_bin_h][pipe.action_to_perform];
			QLearning.Q2[state_bin_v][state_bin_h][pipe.action_to_perform] = Q_s_a
					+ pipe.alpha_QL
					* (reward + max - Q_s_a);

			// Step 4: S <- S'
			pipe.m_state = new double[2];
			pipe.m_state[0] = pipe.m_state_dash[0];
			pipe.m_state[1] = pipe.m_state_dash[1];

			// Step 1: Select and perform Action A 
			state_bin_v = (int) Math
					.max(Math.min(
							Math.floor((pipe.vertical_dist_range[1]
									- pipe.vertical_dist_range[0] - 1)
									/ pipe.resolution),
									Math.floor((pipe.m_state[0] - pipe.vertical_dist_range[0])
											/ pipe.resolution)), 0);

			state_bin_h = (int) Math
					.max(Math.min(
							Math.floor((pipe.horizontal_dist_range[1]
									- pipe.horizontal_dist_range[0] - 1)
									/ pipe.resolution),
									Math.floor((pipe.m_state[1] - pipe.horizontal_dist_range[0])
											/ pipe.resolution)), 0); // [0]: click; [1]: do_nothing
			//click_v = QLearning.Q[state_bin_v][state_bin_h][0];
			//do_nothing_v = QLearning.Q[state_bin_v][state_bin_h][1];
			//player.action_to_perform = click_v > do_nothing_v ? 0 : 1; 
 
			//obstacle.onTap(); 
		} 
	}
}
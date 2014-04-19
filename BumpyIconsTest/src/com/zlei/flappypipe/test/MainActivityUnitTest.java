package com.zlei.flappypipe.test;

import android.content.Intent;
import android.widget.ImageButton;

import com.zlei.flappypipe.MainActivity;

public class MainActivityUnitTest extends
		android.test.ActivityUnitTestCase<MainActivity> {

	private int buttonId;
	private MainActivity activity;

	public MainActivityUnitTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);
		startActivity(intent, null, null);
		activity = getActivity();
	}

	public void testPreconditions() {
	    assertNotNull("MainActivity is null", MainActivity.class);
	}
	
	public void testIntentTriggerViaOnClick_playButton() {
		buttonId = com.zlei.flappypipe.R.id.play_button;
		ImageButton view = (ImageButton) activity.findViewById(buttonId);
		assertNotNull("Button not allowed to be null", view);

		view.performClick();
		
		// Check the intent which was started
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
	}
	
	public void testIntentTriggerViaOnClick_flyerButton() {
		buttonId = com.zlei.flappypipe.R.id.flyer_button;
		ImageButton view = (ImageButton) activity.findViewById(buttonId);
		assertNotNull("Button not allowed to be null", view);

		view.performClick();
		
		// Check the intent which was started
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
	}
	
	public void testIntentTriggerViaOnClick_competeButton() {
		buttonId = com.zlei.flappypipe.R.id.compete_button;
		ImageButton view = (ImageButton) activity.findViewById(buttonId);
		assertNotNull("Button not allowed to be null", view);

		view.performClick();
		
		// Check the intent which was started
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
	}
	
	public void testIntentTriggerViaOnClick_learnButton() {
		buttonId = com.zlei.flappypipe.R.id.learn_button;
		ImageButton view = (ImageButton) activity.findViewById(buttonId);
		assertNotNull("Button not allowed to be null", view);

		view.performClick();
		
		// Check the intent which was started
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
	}
	public void testIntentTriggerViaOnClick_voiceutton() {
		buttonId = com.zlei.flappypipe.R.id.voice_button;
		ImageButton view = (ImageButton) activity.findViewById(buttonId);
		assertNotNull("Button not allowed to be null", view);

		view.performClick();
		
		// Check the intent which was started
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
	}
}

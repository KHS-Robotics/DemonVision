package org.usfirst.frc.team4342.robot.vision.api.listeners;

import org.usfirst.frc.team4342.robot.vision.api.target.Target;

/**
 * Test listener to print information about the processed targets
 */
public class TestListener implements Listener {
	/**
	 * Prints information about the processed targets
	 */
	@Override
	public void processTargets(Target[] targets) {
		System.out.println("Target Count: " + targets.length);
		for(int i = 0; i < targets.length; i++) {
			System.out.println("targets[" + i + "] =" + targets[0]);
		}
	}
}

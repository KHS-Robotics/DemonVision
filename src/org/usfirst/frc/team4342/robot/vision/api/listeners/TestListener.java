package org.usfirst.frc.team4342.robot.vision.api.listeners;

import org.usfirst.frc.team4342.robot.vision.api.target.TargetReport;

/**
 * Test listener to print information about the processed targets
 */
public class TestListener implements Listener {
	/**
	 * Prints information about the processed targets
	 */
	@Override
	public void processTargets(TargetReport report) {
		System.out.println("Target Count: " + report.getTargetCount());
		for(int i = 0; i < report.getTargetCount(); i++) {
			System.out.println("targets[" + i + "] =" + report.getTarget(i));
		}
	}
}

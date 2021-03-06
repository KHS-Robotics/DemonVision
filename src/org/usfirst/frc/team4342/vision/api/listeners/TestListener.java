package org.usfirst.frc.team4342.vision.api.listeners;

import org.usfirst.frc.team4342.vision.api.target.TargetReport;

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
		System.out.println(report);
	}
}

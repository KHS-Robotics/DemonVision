package org.usfirst.frc.team4342.robot.vision.api.target;

import org.opencv.core.Rect;

/**
 * Processes targets from a <code>TargetSoure</code>
 * @see TargetSource
 */
public final class TargetProcessor {
	private TargetProcessor() {}
	
	/**
	 * Processes raw targets
	 * @param source the source of the raw targets
	 * @return processed targets
	 */
	public static TargetReport process(TargetSource source) {
		Rect[] rawTargets = source.getRawTargets();
		Target[] targets = new Target[rawTargets.length];
		
		for(int i = 0; i < rawTargets.length; i++) {
			Rect rawTarget = rawTargets[i];
			double centerXRatio = (double) (rawTarget.x + ((double) rawTarget.width / 2.0)) / ((double) source.getResolution().getX());
			double centerYRatio = (double) (rawTarget.y + ((double) rawTarget.height / 2.0)) / ((double) source.getResolution().getY());
			
			targets[i] = new Target(rawTarget.width, rawTarget.height, centerXRatio, centerYRatio);
		}

		return new TargetReport(targets);
	}
}

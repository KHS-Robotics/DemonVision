package org.usfirst.frc.team4342.vision.api.listeners;

import org.usfirst.frc.team4342.vision.api.target.TargetReport;

/**
 * Listener interface for a callback that should run
 * after a pipeline has processed its input
 */
@FunctionalInterface
public interface Listener {
	/**
	 * Processes the targets
	 * @param targets the processed targets
	 * @see TargetReport
	 */
	public void processTargets(TargetReport report);
}

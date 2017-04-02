package org.usfirst.frc.team4342.robot.vision.api.listeners;

import org.usfirst.frc.team4342.robot.vision.api.target.Target;

/**
 * Listener interface for a callback that should run
 * after a pipeline has processed its input
 */
@FunctionalInterface
public interface Listener {
	/**
	 * Processes the targets
	 * @param targets the processed targets
	 * @see Target
	 */
	public void processTargets(Target[] targets);
}
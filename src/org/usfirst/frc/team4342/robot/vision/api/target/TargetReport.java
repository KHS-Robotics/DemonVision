package org.usfirst.frc.team4342.robot.vision.api.target;

/**
 * Report for processed targets
 */
public class TargetReport {
	private Target[] targets;
	
	/**
	 * Creates a new <codeTargetReport</code>
	 * @param targets the processed targets
	 */
	public TargetReport(Target[] targets) {
		this.targets = targets;
	}
	
	/**
	 * Gets the number of found targets
	 * @return the number of found targets
	 */
	public int getTargetCount() {
		return targets.length;
	}
	
	/**
	 * Gets the found targets
	 * @return the found targets
	 */
	public Target[] getTargets() {
		return targets;
	}
	
	/**
	 * Gets a found target from the list of targets
	 * @param number the target's index in the array
	 * @return the target at the specified index
	 */
	public Target getTarget(int number) {
		return targets[number];
	}
	
	/**
	 * <p>Determines if two <code>TargetReport</code> objects are equal</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		TargetReport report = (TargetReport) obj;
		
		if(this.getTargetCount() != report.getTargetCount())
			return false;
		
		for(int i = 0; i < report.getTargetCount(); i++) {
			if(!this.getTarget(i).equals(report.getTarget(i)))
				return false;
		}
		
		return true;
	}
}

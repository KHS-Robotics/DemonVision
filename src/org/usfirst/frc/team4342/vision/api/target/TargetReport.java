package org.usfirst.frc.team4342.vision.api.target;

import java.util.Arrays;

/**
 * Report for processed targets
 */
public class TargetReport implements java.io.Serializable {
	private static final long serialVersionUID = 1431104252371129970L;
	
	private TargetComparator.Type lastSortType = TargetComparator.Type.AREA;
	
	private Target[] targets;
	
	/**
	 * Creates a new <codeTargetReport</code>
	 * @param targets the processed targets
	 */
	public TargetReport(Target[] targets) {
		if(targets == null)
			throw new IllegalArgumentException("targets cannot be null");
		
		this.targets = targets;
		Arrays.sort(this.targets);
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
	 * Gets the found targets in an order specified
	 * by the sort type
	 * @param sortType the type of sort
	 * @return the found targets in an order specified
	 * by the sort type
	 * @see TargetComparator.Type
	 */
	public Target[] getTargets(TargetComparator.Type sortType) {
		if(sortType != lastSortType)
			Arrays.sort(targets, new TargetComparator(sortType));
		lastSortType = sortType;
		
		return targets;
	}
	
	/**
	 * Gets a found target from the list of targets
	 * @param index the target's index in the array
	 * @return the target at the specified index
	 */
	public Target getTarget(int index) {
		return targets[index];
	}
	
	/**
	 * <p>Represents the <code>TargetReport</code> as a <code>String</code></p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if(targets.length == 0)
			return "[]";
		
		String str = "[";
		
		for(Target t : targets) {
			str += t.toString() + ", ";
		}
		
		str = str.substring(0, str.length()-2) + "]";
		
		return str;
	}
	
	/**
	 * <p>Determines if two <code>TargetReport</code> objects are equal</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
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

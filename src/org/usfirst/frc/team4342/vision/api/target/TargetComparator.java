package org.usfirst.frc.team4342.vision.api.target;

import java.util.Comparator;

/**
 * Comparator for targets
 */
public class TargetComparator implements Comparator<Target> {
	private Type type;
	
	/**
	 * The type of comparison
	 */
	public enum Type {
		WIDTH,
		HEIGHT,
		AREA,
		X,
		Y
	}
	
	/**
	 * Creates a new <code>TargetComparison</code> with a specified
	 * compare type
	 * @param type the type of comparison
	 * @throws IllegalArgumentException if type is null
	 * @see Type
	 */
	public TargetComparator(Type type) {
		if(type == null)
			throw new IllegalArgumentException("type cannot be null");
		
		this.type = type;
	}
	
	/**
	 * Creates a new <code>TargetComparison</code> with an area comparison
	 * @see Type
	 */
	public TargetComparator() {
		this(Type.AREA);
	}
	
	/**
	 * Compares targets a and b
	 * @param a the first target
	 * @param b the second target
	 * @return 0 if a == b, 1 if a > b, -1 if a < b
	 */
	@Override
	public int compare(Target a, Target b) {
		switch(type) {
			case WIDTH:
				return (Math.abs(a.getWidth() - b.getWidth()) < 5) ? 0 : a.getWidth() - b.getWidth() > 0 ? 1 : -1;
			
			case HEIGHT:
				return (Math.abs(a.getHeight() - b.getHeight()) < 3) ? 0 : a.getHeight() - b.getHeight() > 0 ? 1 : -1;
			
			case AREA:
				return (Math.abs(a.getArea() - b.getArea()) < 4) ? 0 : a.getArea() - b.getArea() > 0 ? 1 : -1;
			
			case X:
				return (Math.abs(a.getCenterXRatio() - b.getCenterXRatio()) < 0.01) ? 0 : a.getCenterXRatio() - b.getCenterXRatio() > 0 ? 1 : -1;
			
			case Y:
				return (Math.abs(a.getCenterYRatio() - b.getCenterYRatio()) < 0.01) ? 0 : a.getCenterYRatio() - b.getCenterYRatio() > 0 ? 1 : -1;
			
			default:
				return -2; // should never reach here
		}
	}
}

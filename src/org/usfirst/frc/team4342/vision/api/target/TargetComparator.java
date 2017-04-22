package org.usfirst.frc.team4342.vision.api.target;

import java.util.Comparator;

/**
 * Comparator for targets
 */
public class TargetComparator implements Comparator<Target> {
	private Type type;
	private boolean ascending;
	
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
	 * @param ascending true to invert compare, false otherwise
	 * @throws IllegalArgumentException if type is null
	 * @see Type
	 */
	public TargetComparator(Type type, boolean ascending) {
		if(type == null)
			throw new IllegalArgumentException("type cannot be null");
		
		this.type = type;
	}
	
	/**
	 * Creates a new <code>TargetComparison</code> with an area comparison
	 * @param ascending true to invert compare, false otherwise
	 */
	public TargetComparator(boolean ascending) {
		this(Type.AREA, ascending);
	}
	
	/**
	 * Creates a new <code>TargetComparison</code>
	 * @param type the type of comparison
	 */
	public TargetComparator(Type type) {
		this(type, true);
	}
	
	/**
	 * Creates a new <code>TargetComparison</code> with an area comparison
	 * @see Type
	 */
	public TargetComparator() {
		this(Type.AREA, true);
	}
	
	/**
	 * Compares targets a and b with respect to the type
	 * of comparison
	 * @param type the type of comparison
	 * @param a the first target
	 * @param b the second target
	 * @return 0 if a == b, 1 if a > b, -1 if a < b
	 * @see Type
	 */
	public static int compare(Type type, Target a, Target b) {
		return new TargetComparator(type, true).compare(a, b);
	}
	
	/**
	 * Compares targets a and b
	 * @param a the first target
	 * @param b the second target
	 * @return 0 if a == b, 1 if a > b, -1 if a < b
	 */
	@Override
	public int compare(Target a, Target b) {
		double difference = 0.0;
		double tolerance = 0.0;
		
		switch(type) {
		
			case WIDTH:
				difference = a.width - b.width;
				tolerance = 3.0;
				
			break;
			
			case HEIGHT:
				difference = a.height - b.height;
				tolerance = 3.0;
			break;
			
			case AREA:
				difference = a.area - b.area;
				tolerance = 5.0;
			break;
			
			case X:
				difference = a.x - b.x;
				tolerance = 0.01;
			break;
			
			case Y:
				difference = a.y - b.y;
				tolerance = 0.01;
			break;
			
			default:
				throw new IllegalArgumentException("how is type null????"); // should never reach here
		}
		
		if(Math.abs(difference) < tolerance)
			return 0;
		
		if(ascending)
			return difference > 0 ? 1 : -1;
		
		return difference > 0 ? -1 : 1;
	}
}

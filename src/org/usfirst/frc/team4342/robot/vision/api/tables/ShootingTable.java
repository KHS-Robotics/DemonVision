package org.usfirst.frc.team4342.robot.vision.api.tables;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Class to hold shooting table data
 */
public class ShootingTable {
	private ShootingKeyComparator keyCompare;
	private TreeMap<Double, Double> map;
	
	/**
	 * Creates a new <code>ShootingTable</code>
	 * @param positions the positions for the shooting table
	 */
	public ShootingTable(ShootingPosition[] positions) {
		keyCompare = new ShootingKeyComparator();
		map = new TreeMap<>(keyCompare);
		
		for(ShootingPosition position : positions) {
			map.put(position.getKey(), position.getValue());
		}
	}
	
	/**
	 * Gets a value from the shooting table
	 * @param key the key too look up
	 * @return the value at or near the specified key
	 */
	public double get(double key) {
		if(map.containsKey(key)) {
			return map.get(key);
		}

		// Key is not in table, try to find
		// the next best key
		Double lowKey = map.lowerKey(key);
		Double highKey = map.ceilingKey(key);
		
		double lowToCurrent = lowKey != null ? Math.abs(lowKey - key) : Double.MAX_VALUE;
		double highToCurrent = highKey != null ? highKey - key : Double.MAX_VALUE;

		int comp = keyCompare.compare(lowToCurrent, highToCurrent);
		if(comp >= 0) // overestimate if we are directly in the middle, hence >= and not just >
			key = highKey;
		else
			key = lowKey;

		return map.get(key);
	}
	
	/**
	 * Comparator for the shooting table
	 */
	private class ShootingKeyComparator implements Comparator<Double> {
		private static final double TOLERANCE = 0.01;
		
		/**
		 * <p>Compares {@link Double} values with a tolerance of 0.01</p>
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Double a, Double b) {
			double difference = Math.abs(a - b);
			
			if(difference < TOLERANCE) {
				return 0;
			}
			
			return a.compareTo(b);
		}
	}
}

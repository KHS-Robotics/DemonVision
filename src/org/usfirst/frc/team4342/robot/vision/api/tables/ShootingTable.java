package org.usfirst.frc.team4342.robot.vision.api.tables;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Class to hold shooting table data
 */
public class ShootingTable {
	private HashMap<Double, Double> map;
	
	private ShootingKeyCompare keyCompare;
	private TreeSet<Double> keys;
	
	/**
	 * Creates a new <code>ShootingTable</code>
	 * @param positions the positions for the shooting table
	 */
	public ShootingTable(ShootingPosition[] positions) {
		map = new HashMap<>();
		
		keyCompare = new ShootingKeyCompare();
		keys = new TreeSet<>(keyCompare);
		
		for(ShootingPosition position : positions) {
			map.put(position.getKey(), position.getValue());
			keys.add(position.getKey());
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
		Double lowKey = keys.lower(key);
		Double highKey = keys.ceiling(key);
		
		double lowToCurrent = lowKey != null ? Math.abs(lowKey - key) : Double.MAX_VALUE;
		double highToCurrent = highKey != null ? highKey - key : Double.MAX_VALUE;

		if(lowToCurrent < highToCurrent) {
			key = lowKey;
		}
		else {
			key = highKey;
		}

		return map.get(key);
	}
	
	/**
	 * Comparator for the shooting table
	 */
	private class ShootingKeyCompare implements Comparator<Double> {
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

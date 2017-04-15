package org.usfirst.frc.team4342.vision.api.tables;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Class to hold shooting table data
 */
public class ShootingTable {
	private KeyComparator keyCompare;
	private TreeMap<Double, Double> map;
	
	/**
	 * Creates a new <code>ShootingTable</code>
	 * @param tolerance the tolerance for the keys
	 * @param positions the positions for the shooting table
	 */
	public ShootingTable(double tolerance, ShootingPosition[] positions) {
		keyCompare = new KeyComparator(tolerance);
		map = new TreeMap<>(keyCompare);
		
		for(ShootingPosition position : positions) {
			map.put(position.getKey(), position.getValue());
		}
	}
	
	/**
	 * Creates a new <code>ShootingTable</code>
	 * @param positions the positions for the shooting table
	 */
	public ShootingTable(ShootingPosition[] positions) {
		this(0.01, positions);
	}
	
	/**
	 * Gets a value from the shooting table
	 * @param key the key too look up
	 * @return the value at or near the specified key
	 */
	public double getOrNear(double key) {
		return map.getOrDefault(key, getNear(key));
	}
	
	/**
	 * Gets the value that's mapped to the specified key
	 * @param key the key
	 * @return the value that's mapped to the specified key
	 */
	public double get(double key) {
		return map.get(key);
	}
	
	/**
	 * Finds a value in the table closest to the specified key
	 * @param key the key
	 * @return a value in the table closest to the specified key
	 */
	public double getNear(double key) {
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
	 * Comparator for the shooting table's keys
	 */
	public class KeyComparator implements Comparator<Double> {
		private final double tolerance;
		
		/**
		 * Constructs a <code>KeyComparator</code>
		 * @param tolerance the tolerance
		 */
		public KeyComparator(double tolerance) {
			this.tolerance = tolerance;
		}
		
		/**
		 * Constructs a <code>KeyComparator</code> with a tolerance
		 * of 0.01
		 */
		public KeyComparator() {
			this(0.01);
		}
		
		/**
		 * <p>Compares {@link Double} values with a tolerance of 0.01</p>
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Double a, Double b) {
			double difference = Math.abs(a - b);
			
			if(difference < tolerance) {
				return 0;
			}
			
			return a.compareTo(b);
		}
	}
}

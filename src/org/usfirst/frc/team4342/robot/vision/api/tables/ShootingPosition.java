package org.usfirst.frc.team4342.robot.vision.api.tables;

/**
 * Class to hold information about a position
 * to shoot from
 */
public class ShootingPosition {
	private double key;
	private double value;
	
	/**
	 * Creates a new <code>ShootingPosition</code>
	 * @param key the unique key of the position
	 * @param value the value at this position
	 */
	public ShootingPosition(double key, double value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Gets the unique key at this position
	 * @return the unique key at this position
	 */
	public double getKey() {
		return key;
	}
	
	/**
	 * Gets the value at this position
	 * @return the value at this position
	 */
	public double getValue() {
		return value;
	}
 }

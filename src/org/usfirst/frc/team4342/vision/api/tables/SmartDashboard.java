package org.usfirst.frc.team4342.vision.api.tables;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Wrapper for SmartDashboard Network Table
 */
public final class SmartDashboard {
	private SmartDashboard() {}
	
	private static NetworkTable table;
	
	/**
	 * Gets the SmartDashboard Network Table
	 * @return the SmartDashboard Network Table
	 */
	public static NetworkTable getSmartDashboard() {
		if(table == null)
			table = NetworkTable.getTable("SmartDashboard");
		
		return table;
	}
	
	/**
	 * Puts a number to the SmartDashboard
	 * @param key the key
	 * @param value the value
	 */
	public static void putNumber(String key, double value) {
		getSmartDashboard().putNumber(key, value);
	}
	
	/**
	 * Puts a boolean to the SmartDashboard
	 * @param key the key
	 * @param value the value
	 */
	public static void putBoolean(String key, boolean value) {
		getSmartDashboard().putBoolean(key, value);
	}
	
	/**
	 * Gets a number from the SmartDashboard
	 * @param key the key
	 * @param defaultValue the default value if the key is not in the table
	 * @return the value of the key if in the table, defaultValue otherwise
	 */
	public static double getNumber(String key, double defaultValue) {
		return getSmartDashboard().getNumber(key, defaultValue);
	}
	
	/**
	 * Gets a boolean from the SmartDashboard
	 * @param key the key
	 * @param defaultValue the default value if the key is not in the table
	 * @return the value of the key if in the table, defaultValue otherwise
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		return getSmartDashboard().getBoolean(key, defaultValue);
	}
}

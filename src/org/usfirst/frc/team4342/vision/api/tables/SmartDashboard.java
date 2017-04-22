package org.usfirst.frc.team4342.vision.api.tables;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team4342.vision.DemonVision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Wrapper for SmartDashboard Network Table
 */
public final class SmartDashboard {
	private SmartDashboard() {}
	
	private static boolean initialized;
	private static NetworkTable table;
	
	/**
	 * Initializes Network Tables
	 * @param teamNumber your team number
	 */
	public static void initialize(int teamNumber) {
		if(initialized)
			return;
		initialized = true;
		
		// Configure NetworkTables
		try {
			NetworkTable.setClientMode();
			NetworkTable.setNetworkIdentity("coprocessor-" + teamNumber + "-frc");
			NetworkTable.setIPAddress("roborio-" + teamNumber + "-frc.local");
		} catch(Exception ex) {
			Logger.getLogger(DemonVision.class.getName()).log(Level.SEVERE, "Failed to initialize network tables", ex);
		}
		
		table = NetworkTable.getTable("SmartDashboard");
	}
	
	/**
	 * Puts a number to the SmartDashboard
	 * @param key the key
	 * @param value the value
	 */
	public static void putNumber(String key, double value) {
		verifyInitializedAndThrow();
		table.putNumber(key, value);
	}
	
	/**
	 * Puts a boolean to the SmartDashboard
	 * @param key the key
	 * @param value the value
	 */
	public static void putBoolean(String key, boolean value) {
		verifyInitializedAndThrow();
		table.putBoolean(key, value);
	}
	
	/**
	 * Gets a number from the SmartDashboard
	 * @param key the key
	 * @param defaultValue the default value if the key is not in the table
	 * @return the value of the key if in the table, defaultValue otherwise
	 */
	public static double getNumber(String key, double defaultValue) {
		verifyInitializedAndThrow();
		return table.getNumber(key, defaultValue);
	}
	
	/**
	 * Gets a boolean from the SmartDashboard
	 * @param key the key
	 * @param defaultValue the default value if the key is not in the table
	 * @return the value of the key if in the table, defaultValue otherwise
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		verifyInitializedAndThrow();
		return table.getBoolean(key, defaultValue);
	}
	
	/**
	 * Verifies that Network Tables were initialized
	 * @throws NullPointerException if {@link SmartDashboard#initialize(int)} wasn't called
	 * or if <code>table</code> is null
	 * @see SmartDashboard#init
	 */
	private static void verifyInitializedAndThrow() {
		if(!initialized)
			throw new NullPointerException("Network Tables were never initialized! Call SmartDashboard.initialize(int)!");
		
		if(table == null)
			throw new NullPointerException("SmartDashboard was never instantiated!"); 
	}
}

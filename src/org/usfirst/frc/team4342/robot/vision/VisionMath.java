package org.usfirst.frc.team4342.robot.vision;

public class VisionMath {
	private VisionMath() {}
	
	private static final double CAMERA_VIEW_ANGLE = 68.5;
	private static final int ACCELERATION_DUE_TO_GRAVITY_INCHES = 384;
	// TODO: Get wheel shooter radius
	private static final double SHOOTER_WHEEL_RADIUS_INCHES = 2.0;
	// TODO: Measure far and close angle
	private static final double FAR_ANGLE = 60;
	private static final double CLOSE_ANGLE = 45;
	
	/**
	 * Calculates an adjusted yaw to orient the robot center with the boiler
	 * @param yaw the current yaw of the robot
	 * @param boiler the boiler
	 * @return an adjusted yaw that the robot should goto to align center with the boiler
	 */
	public static double getAdjustedYaw(double yaw, Boiler boiler) {
		return yaw + CAMERA_VIEW_ANGLE*(boiler.getTopCenterXRatio() - 0.5);
	}
	
	/**
	 * Calculates an ideal shooter wheel RPM
	 * @param farAngle true to use the far angle (shooter set to far), 
	 * false to use close (shooter set to close)
	 * @param boiler the boiler
	 * @return an ideal shooter RPM
	 */
	public static double getIdealShooterRPM(boolean farAngle, Boiler boiler) {
		final double ANGLE = farAngle ? FAR_ANGLE : CLOSE_ANGLE;
		
		final double NUMERATOR = ACCELERATION_DUE_TO_GRAVITY_INCHES * 67.5;
		final double DENOMINATOR = Math.tan(Math.toRadians(CAMERA_VIEW_ANGLE)*boiler.getTopCenterYRatio())*Math.sin(2*ANGLE);
		final double VELOCITY = Math.sqrt(NUMERATOR / DENOMINATOR);
		
		final double RPM = 2 * Math.PI * SHOOTER_WHEEL_RADIUS_INCHES * VELOCITY;
		
		return RPM;
	}
}

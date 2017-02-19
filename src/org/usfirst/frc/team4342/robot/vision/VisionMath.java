package org.usfirst.frc.team4342.robot.vision;

public class VisionMath {
	private VisionMath() {}
	
	private static final double CAMERA_VIEW_ANGLE = 68.5;
	private static final int ACCELERATION_DUE_TO_GRAVITY_INCHES = 384;
	private static final double SHOOTER_WHEEL_RADIUS_INCHES = 2.0;
	private static final double ANGLE = 45;
	
	public static double getAdjustedYaw(double yaw, double topCenterXRatio) {
		return yaw + CAMERA_VIEW_ANGLE*(topCenterXRatio - 0.5);
	}
	
	public static double getIdealShooterRPM(double topCenterYRatio) {
		final double NUMERATOR = ACCELERATION_DUE_TO_GRAVITY_INCHES * 67.5;
		final double DENOMINATOR = Math.tan(Math.toRadians(CAMERA_VIEW_ANGLE)*topCenterYRatio)*Math.sin(2*ANGLE);
		final double VELOCITY = Math.sqrt(NUMERATOR / DENOMINATOR);
		
		final double RPM = 2 * Math.PI * SHOOTER_WHEEL_RADIUS_INCHES * VELOCITY;
		
		return RPM;
	}
}

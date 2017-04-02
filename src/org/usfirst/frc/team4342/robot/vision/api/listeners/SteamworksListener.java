package org.usfirst.frc.team4342.robot.vision.api.listeners;

import org.usfirst.frc.team4342.robot.vision.api.cameras.Camera;
import org.usfirst.frc.team4342.robot.vision.api.target.Target;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <p>Listener for FIRST SteamWORKS</p>
 * 
 * Calculates the yaw of the boiler and sends a recommended yaw
 * that the robot should orient to to shoot
 */
public class SteamworksListener implements Listener {
	private Camera camera;
	
	/**
	 * Constructs a new <code>SteamworksListener</code>
	 * @param camera the camera that retrieved the image to process the targets
	 */
	public SteamworksListener(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * <p>Calculates the yaw of boiler and puts it up to the SmartDashboard</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void processTargets(Target[] targets) {
		SmartDashboard.putNumber("DV-Targets", targets.length);
		
		if(targets.length > 1) {
			Target top = targets[0];
			
			double robotYaw = SmartDashboard.getNumber("NavX-Yaw", 0.0);
			double boilerYaw = robotYaw + top.getYawOffset(camera.getFOV());
			
			SmartDashboard.putNumber("Boiler-Yaw", boilerYaw);
		}
	}
}

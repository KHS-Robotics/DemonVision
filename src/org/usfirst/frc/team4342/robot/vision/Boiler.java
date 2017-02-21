package org.usfirst.frc.team4342.robot.vision;

import org.opencv.core.Rect;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Boiler {
	private static final double RES_X = 360.0;
	private static final double RES_Y = 240.0;
	
	private final double topWidthRatio, topHeightRatio, bottomWidthRatio, bottomHeightRatio;
	private final double topCenterXRatio, topCenterYRatio, bottomCenterXRatio, bottomCenterYRatio;
	
	private Rect top, bottom;
	
	public Boiler(Rect top, Rect bottom) {
		this.top = top;
		this.bottom = bottom;
		
		topWidthRatio = (double) top.width / RES_X;
		topHeightRatio = (double) top.height / RES_Y;
		bottomWidthRatio = (double) bottom.width / RES_X;
		bottomHeightRatio = (double) bottom.height / RES_Y;
		topCenterXRatio = (double) (top.x + ((double) top.width / 2.0)) / RES_X;
		topCenterYRatio = (double) (top.y + ((double) top.height / 2.0)) / RES_Y;
		bottomCenterXRatio = (double) (bottom.x + ((double) bottom.height / 2.0)) / RES_X;
		bottomCenterYRatio = (double) (bottom.y + ((double) bottom.height / 2.0)) / RES_Y;
	}
	
	public void publishData(NetworkTable table) {
		table.putNumber("Top-Width", topWidthRatio);
		table.putNumber("Top-Height", topHeightRatio);
		table.putNumber("Bottom-Width", bottomWidthRatio);
		table.putNumber("Bottom-Height", bottomHeightRatio);
		table.putNumber("Top-Center-X", topCenterXRatio);
		table.putNumber("Top-Center-Y", topCenterYRatio);
		table.putNumber("Bottom-Center-X", bottomCenterXRatio);
		table.putNumber("Bottom-Center-Y", bottomCenterYRatio);
	}
	
	public double getTopCenterXRatio() {
		return topCenterXRatio;
	}
	
	public double getTopCenterYRatio() {
		return topCenterYRatio;
	}
	
	public Rect getTop() {
		return top;
	}
	
	public Rect getBottom() {
		return bottom;
	}
}

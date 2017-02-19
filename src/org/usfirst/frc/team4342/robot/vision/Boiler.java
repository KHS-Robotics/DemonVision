package org.usfirst.frc.team4342.robot.vision;

import org.opencv.core.Rect;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Boiler {
	private static final int RES_X = 360;
	private static final int RES_Y = 240;
	
	private NetworkTable table;
	
	private final double topWidthRatio, topHeightRatio, bottomWidthRatio, bottomHeightRatio;
	private final double topCenterXRatio, topCenterYRatio, bottomCenterXRatio, bottomCenterYRatio;
	
	private Rect top, bottom;
	
	public Boiler(Rect top, Rect bottom) {
		this.top = top;
		this.bottom = bottom;
		
		topWidthRatio = top.width / RES_X;
		topHeightRatio = top.height / RES_Y;
		bottomWidthRatio = bottom.width / RES_X;
		bottomHeightRatio = bottom.height / RES_Y;
		topCenterXRatio = (top.x + (top.width / 2)) / RES_X;
		topCenterYRatio = (top.y + (top.height / 2)) / RES_Y;
		bottomCenterXRatio = (bottom.x + (bottom.height / 2)) / RES_X;
		bottomCenterYRatio = (bottom.y + (bottom.height / 2)) / RES_Y;
		
		table = NetworkTable.getTable("SmartDashboard");
	}
	
	public void publishData() {
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

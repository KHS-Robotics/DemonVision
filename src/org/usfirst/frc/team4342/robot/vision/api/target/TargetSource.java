package org.usfirst.frc.team4342.robot.vision.api.target;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.Resolution;

/**
 * Source for unprocessed targets
 */
public interface TargetSource {
	/**
	 * Processes an image to find contours in it, AKA targets
	 * @param source the image to process
	 */
	public void process(Mat source);
	
	/**
	 * Releases outputs to free resources
	 */
	public void releaseOutputs();
	
	/**
	 * Gets the resolution the processed target originated from
	 * @return the resolution the processed target originated from
	 */
	public Resolution getResolution();
	
	/**
	 * Gets the processed contours
	 * @return the processed contours
	 */
	public List<MatOfPoint> getContoursReport();
	
	/**
	 * Utilizes the processed contours and wraps them
	 * into bounding rectangles
	 * @return the unprocessed targets as bounding rectangles
	 * @see Rect
	 */
	default Rect[] getRawTargets() {
		List<MatOfPoint> contours = getContoursReport();
		Rect[] rawTargets = new Rect[contours.size()];
		
		for(int i = 0; i < rawTargets.length; i++) {
			rawTargets[i] = Imgproc.boundingRect(contours.get(i));
		}
		
		return rawTargets;
	}
}

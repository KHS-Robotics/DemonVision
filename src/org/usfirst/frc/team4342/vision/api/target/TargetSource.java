package org.usfirst.frc.team4342.vision.api.target;

import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team4342.vision.api.pipelines.parameters.Resolution;

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
	 * @see TargetSource#getContoursReport()
	 * @see Rect
	 */
	default Rect[] getRawTargets() {
		List<MatOfPoint> contours = this.getContoursReport();
		Rect[] rawTargets = new Rect[contours.size()];
		
		for(int i = 0; i < rawTargets.length; i++) {
			rawTargets[i] = Imgproc.boundingRect(contours.get(i));
		}
		
		return rawTargets;
	}
	
	/**
	 * Processes raw targets
	 * @return processed targets
	 * @see TargetSource#process(Mat)
	 * @see TargetSource#getRawTargets()
	 * @see TargetReport
	 */
	default TargetReport getTargetReport() {
		Rect[] rawTargets = this.getRawTargets();
		Target[] targets = new Target[rawTargets.length];
		
		for(int i = 0; i < rawTargets.length; i++) {
			Rect rawTarget = rawTargets[i];
			
			// Calculate the X and Y coordinates of the center of the target
			double x = (double) (rawTarget.x + ((double) rawTarget.width / 2.0)) / ((double) this.getResolution().getX());
			double y = (double) (rawTarget.y + ((double) rawTarget.height / 2.0)) / ((double) this.getResolution().getY());
			
			targets[i] = new Target(rawTarget.width, rawTarget.height, x, y);
		}

		return new TargetReport(targets);
	}
}

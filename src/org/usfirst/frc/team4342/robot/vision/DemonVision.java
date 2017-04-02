package org.usfirst.frc.team4342.robot.vision;

import org.opencv.core.Mat;
import org.usfirst.frc.team4342.robot.vision.api.cameras.Camera;
import org.usfirst.frc.team4342.robot.vision.api.listeners.Listener;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.DemonVisionPipeline;
import org.usfirst.frc.team4342.robot.vision.api.pipelines.parameters.PiplelineParameters;
import org.usfirst.frc.team4342.robot.vision.api.target.TargetProcessor;

/**
 * Class to handle the dirty work for processing a target
 */
public class DemonVision implements Runnable {
	private DemonVisionPipeline pipeline;
	
	private Camera cam;
	private Listener[] listeners;
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param parameters the parameters for the pipeline processing images from the camera
	 * @param processor the processor to process raw target information from the pipeline
	 * @param listeners the listeners to utilize processed targets from the processor
	 */
	public DemonVision(Camera cam, PiplelineParameters parameters, Listener[] listeners) {
		this.cam = cam;
		this.pipeline = new DemonVisionPipeline(parameters);
		this.listeners = listeners;
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param parameters the parameters for the pipeline processing images from the camera
	 * @param processor the processor to process raw target information from the pipeline
	 * @param listener the listener to utilize processed targets from the processor
	 */
	public DemonVision(Camera cam, PiplelineParameters parameters, Listener listener) {
		this(cam, parameters, new Listener[] { listener });
	}
	
	/**
	 * <p>Processes images from the camera until the thread is interrupted</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			runOnce();
		}
	}
	
	/**
	 * Processes an image
	 */
	public void runOnce() {
		Mat img = cam.getFrame();
		pipeline.process(img);
		
		for(Listener listener : listeners) {
			listener.processTargets(TargetProcessor.process(pipeline));
		}
		
		pipeline.releaseOutputs();
		img.release();
	}
	
	/**
	 * Indefinitely processes images from the camera
	 */
	public void runForever() {
		while(true) {
			runOnce();
		}
	}
	
	/**
	 * Processes images from the camera a fixed number of times
	 * @param times the number of times
	 * @throws IllegalArgumentException if times is equal to or less than zero
	 */
	public void run(final int times) {
		if(times < 0)
			throw new IllegalArgumentException("times must be be greater positive");
			
		int current = 0;
		while(current < times) {
			runOnce();
			current++;
		}
	}
}

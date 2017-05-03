package org.usfirst.frc.team4342.vision;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.usfirst.frc.team4342.vision.api.cameras.Camera;
import org.usfirst.frc.team4342.vision.api.listeners.Listener;
import org.usfirst.frc.team4342.vision.api.pipelines.DemonVisionPipeline;
import org.usfirst.frc.team4342.vision.api.pipelines.parameters.PipelineParameters;
import org.usfirst.frc.team4342.vision.api.target.TargetReport;
import org.usfirst.frc.team4342.vision.api.target.TargetSource;

/**
 * Class to handle the dirty work for processing a target
 */
public class DemonVision implements Runnable {
	private static Logger log;
	
	private Camera cam;
	private TargetSource source;
	private Listener[] listeners;
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param source the source to get raw targets from
	 * @param listeners the listeners to utilize processed targets
	 */
	public DemonVision(Camera cam, TargetSource source, Listener[] listeners) {
		try {
			log = Logger.getLogger(DemonVision.class.getName());
			log.addHandler(new java.util.logging.FileHandler("demon_vision.log"));
		} catch (Exception ex) {
			System.err.println("Failed to add file handler for DemonVision logger!");
			ex.printStackTrace();
		}
		
		this.cam = cam;
		this.source = source;
		this.listeners = listeners;
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param source the source to get raw targets from
	 * @param listener the listener to utilize processed targets
	 */
	public DemonVision(Camera cam, TargetSource source, Listener listener) {
		this(cam, source, new Listener[] { listener });
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param parameters the parameters for the pipeline processing images
	 * @param listeners the listeners to utilize processed targets
	 */
	public DemonVision(Camera cam, PipelineParameters parameters, Listener[] listeners) {
		this(cam, new DemonVisionPipeline(parameters), listeners);
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param cam the camera being used
	 * @param parameters the parameters for the pipeline processing images
	 * @param listener the listener to utilize processed targets
	 */
	public DemonVision(Camera cam, PipelineParameters parameters, Listener listener) {
		this(cam, new DemonVisionPipeline(parameters), listener);
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param parameters the parameters for the pipeline processing images
	 * @param listeners the listeners to utilize processed targets
	 */
	public DemonVision(PipelineParameters parameters, Listener[] listeners) {
		this(null, parameters, listeners);
	}
	
	/**
	 * Constructs a new <code>DemonVision</code>
	 * @param parameters the parameters for the pipeline processing images
	 * @param listener the listener to utilize processed targets
	 */
	public DemonVision(PipelineParameters parameters, Listener listener) {
		this(null, parameters, listener);
	}
	
	/**
	 * <p>Processes images from the camera until the thread is interrupted</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				runOnce();
			} catch(Exception ex) {
				break;
			}
		}
	}
	
	/**
	 * Processes an image from the camera
	 * @throws NullPointerException if camera is null
	 */
	public void runOnce() {
		if(cam == null) {
			NullPointerException ex = new NullPointerException("Camera was not specified at construction! Please specify a camera in the constructor");
			log.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
		
		runOnce(cam.getFrame());
	}
	
	/**
	 * Processes a specified image
	 * @param path the path to the image
	 */
	public void runOnce(String path) {
		runOnce(Imgcodecs.imread(path));
	}
	
	/**
	 * Processes a specified image
	 * @param img the image to process
	 */
	public void runOnce(Mat img) {
		try {
			source.process(img);
			
			TargetReport report = source.getTargetReport();
			for(Listener listener : listeners) {
				listener.processTargets(report);
			}
			
			source.releaseOutputs();
			img.release();
		} catch(Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			throw ex;
		}
	}
	
	/**
	 * Indefinitely processes images from the camera
	 */
	public void runForever() {
		while(true) {
			try {
				runOnce();
			} catch(Exception ex) {
				break;
			}
		}
	}
	
	/**
	 * Processes images from the camera a fixed number of times
	 * @param times the number of times
	 * @throws IllegalArgumentException if times is negative
	 */
	public void run(final int times) {
		if(times < 0)
			throw new IllegalArgumentException("times cannot be negative");
			
		int current = 0;
		while(current < times) {
			try {
				runOnce();
				current++;
			} catch(Exception ex) {
				break;
			}
		}
	}
}

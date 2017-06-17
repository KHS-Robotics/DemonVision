package org.usfirst.frc.team4342.vision.api.pipelines.parameters;

/**
 * Class to encapsulate all of the parameters for the DemonVision pipeline
 * @see org.usfirst.frc.team4342.vision.api.pipelines.DefaultPipeline
 */
public class PipelineParameters {
	private Resolution res;
	private Blur blur;
	private RGBBounds rgb;
	
	/**
	 * Constructs a new <code>PiplelineParameters</code>
	 * @param res the resolution of the processed image
	 * @param blur the type and amount to blur
	 * @param rgb the bounds for the RGB threshold
	 */
	public PipelineParameters(Resolution res, Blur blur, RGBBounds rgb) {
		this.res = res;
		this.blur = blur;
		this.rgb = rgb;
	}
	
	/**
	 * Gets the resolution of the processed image
	 * @return the resolution of the processed image
	 */
	public Resolution getResolution() {
		return res;
	}
	
	/**
	 * Gets the blur
	 * @return the blur
	 */
	public Blur getBlur() {
		return blur;
	}
	
	/**
	 * Gets the RGB Bounds
	 * @return the RGB bounds
	 */
	public RGBBounds getRGB() {
		return rgb;
	}
}

package org.usfirst.frc.team4342.vision.api.pipelines.parameters;

/**
 * Blurs an image to reduce noise
 */
public class Blur {
	private Type type;
	private double radius;
	
	/**
	 * Constructs a new <code>Blur</code>
	 * @param type the type of blur
	 * @param radius the radius of the blur
	 */
	public Blur(Type type, double radius) {
		this.type = type;
		this.radius = radius;
	}
	
	/**
	 * Gets the radius of the blur
	 * @return
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Gets the type of blur
	 * @return the type of blur
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Types of blurs
	 */
	public enum Type {
		BOX("Box Blur"), 
		GAUSSIAN("Gaussian Blur"), 
		MEDIAN("Median Filter"), 
		BILATERAL("Bilateral Filter");

		private final String label;
	
		private Type(String label) {
			this.label = label;
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
}

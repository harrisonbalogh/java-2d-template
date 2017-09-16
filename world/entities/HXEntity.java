package world.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import world.HXWorld;
import world.helper.properties.HXPhysicsBody;

public abstract class HXEntity {
	
	private double xPos;
	private double yPos;
	private double xPos_prev;
	private double yPos_prev;
	private double xAnchor = 0.5;
	private double yAnchor = 0.5;
	private double width;
	private double height;
	private double scale;
	private int    draw_xPos;
	private int    draw_yPos;
	
	private Rectangle rect;
	private HXWorld parentWorld;
	private Image img;
	private double mass;
	private double lifespan; // an entity with -1 lifespan will not decay
	private double xVel;
	private double yVel;
	
	private double LIFESPAN_DECAY = 0.03;
	
	// PHYSICS BODY
	// Friction can still be applied to an entity w/o a physicsBody
	private double FRICTION = 0.2;
	private double rotation = 0;
	private double sphere_radius; // Circle around square shape.
	
	/**
	 * The constructors of the classes in the 'entities' package should call this init() method.
	 * <p>
	 * Adds the newly instantiated entity to the parent HXWorld's array list of all entities and instatiates a new Rectangle object.
	 * @param x - x coordinate of spawn location.
	 * @param y - y coordinate of spawn location.
	 * @param w - width of entity.
	 * @param h - height of entity.
	 * @param m - mass of entity.
	 * @param parent - HXWorld this entity will belong to.
	 */
	protected void init(
			Image i, 
			double x, double y, 
			double withWidth, double withHeight, 
			double velX, double velY, 
			double withMass, double life, 
			double withScale,
			HXWorld parent) {
		this.img = i;
		this.xPos = x;
		this.yPos = y;
		this.width = withWidth;
		this.height = withHeight;
		this.scale = withScale;
		this.mass = withMass;
		this.xPos_prev = x;
		this.yPos_prev = y;
		this.lifespan = life;
		this.rect = new Rectangle((int) xPos, (int) yPos, (int) width, (int) height); 
		this.parentWorld = parent;
		this.xVel = velX;
		this.yVel = velY;
		
		this.sphere_radius = Math.sqrt(Math.pow((width/2), 2) + Math.pow((height/2), 2));
		
		this.parentWorld.entityAdd(this);
	}
	
	/**
	 * Called by HXWorldPanel within its paintComponent() based on HXClock repaint timer.
	 * <p>
	 * Uses interpolation on constant timestep in HXClock to do smooth drawing as well as update the Rectangle object collider.
	 * @param g - The Graphics object context that will get painted on.
	 * @param interpolation - Sent by the HXClock to smooth movements when thread stutters or CPU lags.
	 */
	public void draw(Graphics g, float interpolation) { 
		draw_xPos = (int) (((xPos - xPos_prev) * interpolation + xPos_prev) * scale);
		draw_yPos = (int) (((yPos - yPos_prev) * interpolation + yPos_prev) * scale);

		// draw() updates Rectangle location with where it visually is rendered
		rect.setLocation((int) draw_xPos, (int) draw_yPos); 
	}
	
	/**
	 * Called whenever an entity needs to be updated.
	 * <p>
	 * Used in conjunction with another class changing an entity's x or y positions.
	 */
	public void update() {
		if (this instanceof HXPhysicsBody) {
			
			for (HXEntity e : parentWorld.getPhysicsBodies()) {
				// d^2 < (r1^2 + r2^2)
				double distanceToPeer = Math.sqrt(Math.pow(e.getyPos() - getyPos(), 2) + Math.pow(e.getxPos() - getxPos(), 2));
				if (distanceToPeer < sphere_radius + e.getSphereRadius()) {
					
				}
			}
			
		} else {
			// If entity does not have a PhysicsBody then
			// use simple movement scheme with general
			// deceleration from friction.
			xPos += xVel;
			yPos += yVel;
		
			if (xVel >= FRICTION)
				xVel -= FRICTION;
			else
				xVel = 0;
			if (yVel >= FRICTION)
				yVel -= FRICTION;
			else
				yVel = 0;
		}
		
		xPos_prev = xPos;
		yPos_prev = yPos;
		
		if (lifespan != -1) {
			lifespan -= LIFESPAN_DECAY;
			if (lifespan <= 0) {
				remove();
			}
		}
		
		// TODO Change xPos or yPos variables for movement
	}
	
	/**
	 * Used to delete an entity
	 * <p>
	 * Removes the caller from the world's entity array list.
	 */
	public void remove() {
		this.parentWorld.entityRemove(this);
	}
	
	// ============================   MARK: Getters and Setters ============================ 
	public double getSphereRadius() {
		return sphere_radius;
	}
	public double getxPos() {
		return xPos;
	}
	public void setxPos(double xPos) {
		this.xPos = xPos;
	}
	public double getyPos() {
		return yPos;
	}
	public void setyPos(double yPos) {
		this.yPos = yPos;
	}
	public double getxPos_Prev() {
		return xPos_prev;
	}
	public void setxPos_Prev(double prevX) {
		this.xPos_prev = prevX;
	}
	public double getyPos_Prev() {
		return yPos_prev;
	}
	public void setyPos_Prev(double prevY) {
		this.yPos_prev = prevY;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	public Double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	public HXWorld getWorld() {
		return parentWorld;
	}
	public Double getLifespan() {
		return lifespan;
	}
	public void setLifespan(double lifespan) {
		this.lifespan = lifespan;
	}
	public double getxVel() {
		return xVel;
	}
	public void setxVel(double xVel) {
		this.xVel = xVel;
	}
	public double getyVel() {
		return yVel;
	}
	public void setyVel(double yVel) {
		this.yVel = yVel;
	}
	public int getDraw_xPos() {
		return draw_xPos;
	}
	public void setDraw_xPos(int draw_xPos) {
		this.draw_xPos = draw_xPos;
	}
	public int getDraw_yPos() {
		return draw_yPos;
	}
	public void setDraw_yPos(int draw_yPos) {
		this.draw_yPos = draw_yPos;
	}
	public Image getImg() {
		return this.img;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double s) {
		this.scale = s;
	}
}
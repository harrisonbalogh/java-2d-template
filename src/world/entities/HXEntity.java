package world.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import world.helper.math.HXCollider;
import world.helper.properties.HXHoverable;
import world.helper.properties.HXInteractable;
import world.helper.properties.HXPhysicsBody;
import world.helper.properties.HXEntityOwner;
import world.helper.math.HXVector;

public abstract class HXEntity {
	
	private double xPos;
	private double yPos;
	private double rotation = 0;
	// For interpolating rendering. Reference previous positions
	private double xPos_prev;
	private double yPos_prev;
//	private double anchor = 0.5; // NYI
	private double width;
	private double height;
	
	private HXEntityOwner owner;
	private Image img;
	private double lifespan; // an entity with -1 lifespan will not decay
	private HXVector velocity;
	private double FRICTION = 0.1;
	private Boolean stasis = false;
	private double rVel; // rotational velocity
	
	private double LIFESPAN_DECAY = 3;
	
//	private HXVector GRAVITY = new HXVector(0, -1); // update ignores GRAVITY.x
//	private double TERMINAL_FREE_FALL = 5;
	
	private double sphere_radius; // Circle around square shape.
	
	// All dynamic movements happen in the collider
	private HXCollider collider = null;
	
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
	protected HXEntity(
			Image i, 
			double x, double y, 
			double withWidth, double withHeight, 
			double velX, double velY, 
			double life) {
		this.img = i;
		this.xPos = x;
		this.yPos = y;
		this.width = withWidth;
		this.height = withHeight;
		this.xPos_prev = x;
		this.yPos_prev = y;
		this.lifespan = life;
		this.velocity = new HXVector(velX, velY);
		
		this.sphere_radius = Math.sqrt(Math.pow((width/2), 2) + Math.pow((height/2), 2));
		
		// Only generate collider if it implements an interface that requires checking collisions.
		if ( this instanceof HXInteractable || 
			 this instanceof HXPhysicsBody || 
			 this instanceof HXHoverable ) {
			collider = new HXCollider(new HXVector[]{
					new HXVector( width/2, height/2),
					new HXVector( width/2,-height/2),
					new HXVector(-width/2,-height/2),
					new HXVector(-width/2, height/2)
			}, this);
		}
	}
	
	/**
	 * Called by HXWorldPanel within its paintComponent() based on HXClock repaint timer.
	 * <p>
	 * Uses interpolation on constant timestep in HXClock to do smooth drawing as well as update the Rectangle object collider.
	 * @param g - The Graphics object context that will get painted on.
	 * @param interpolation - Sent by the HXClock to smooth movements when thread stutters or CPU lags.
	 */
	public void draw(Graphics g, float interpolation) { 
		
		
		// Draw Collider
//		if (collider != null) {
//			if (isHovering()) {
//				g.setColor(Color.green);
//			} else if (isInteracting()) {
//				g.setColor(Color.red);
//			} else {
//				g.setColor(Color.black);
//			}
//			
//			g.drawLine(0, 0, (int)velocity.x(), (int)velocity.y());
//			
//			for(int v = 0; v < this.getCollider().getVertices().length; v++)
//				g.drawLine(
//						(int) (this.getCollider().getVertices()[v].x()*getScale() + getDraw_xPos(interpolation)), 
//						(int) (this.getCollider().getVertices()[v].y()*getScale() + getDraw_yPos(interpolation)), 
//						(int) (this.getCollider().getVertices()[(v+1)%this.getCollider().getVertices().length].x()*getScale() + getDraw_xPos(interpolation)), 
//						(int) (this.getCollider().getVertices()[(v+1)%this.getCollider().getVertices().length].y()*getScale() + getDraw_yPos(interpolation))
//				);
//			
//			HXVector mtv = collider.getTestMtv();
//			if (mtv != null)
//				g.drawLine(
//						(int) (getDraw_xPos(interpolation)), 
//						(int) (getDraw_yPos(interpolation)), 
//						(int) (getDraw_xPos(interpolation) + mtv.x() * owner.getScale()), 
//						(int) (getDraw_yPos(interpolation) + mtv.y() * owner.getScale())
//				);
//		}
	}
		
	/**
	 * Called whenever an entity needs to be updated.
	 * <p>
	 * Used in conjunction with another class changing an entity's x or y positions.
	 */
	public void update(double dT) {

		// Apply velocity - First check if velocity's magnitude was set and, if so, if its !zero. Otherwise, check if either components are !zero.
		if (velocity.x() != 0 || velocity.y() != 0) {
			System.out.println("Got some velocity: " + velocity);
			xPos += velocity.x();
			yPos += velocity.y();
			// Apply friction
			velocity.reduceBy(FRICTION);
		}
		
		// Apply rotational velocity
		rotation = (rotation + rVel);
		collider.rotateBy(rVel);
		if (rotation >= 2 * Math.PI) rotation -= (2 * Math.PI);
		else if (rotation < 0) rotation += (2 * Math.PI);
		
		// Apply decay
		if (lifespan != -1) {
			lifespan -= LIFESPAN_DECAY;
			if (lifespan <= 0) {
				remove();
			}
		}
		// Update previous position for interpolation
		xPos_prev = xPos;
		yPos_prev = yPos;
	}
	
	/**
	 * Used to delete an entity
	 * <p>
	 * Removes the caller from the world's entity array list.
	 */
	public void remove() {
		if (owner != null) {
			this.owner.entityRemove(this);
		}
	}
	
	/**
	 * Applies a force to the collider. Simply adds the force vector to
	 * the owning entity's velocity. The force will not be applied to the
	 * entity if it is interacted with by the cursor (that is, its being
	 * held by the user) or if the entity is in stasis. These two states
	 * oppose all force actions on the object.
	 * @param force - The vector applied to the collider.
	 */
	public void impulse(HXVector force) {
		if (!interacting && !stasis)
			velocity.add(force);			
	}
	
	/**
	 * Primarily used when calculating the minimum translation vector of overlapping
	 * (colliding) objects.
	 * @param tv - Translation vector.
	 */
	public void translate(HXVector tv) {
		if (!interacting && !stasis) {
			xPos += tv.x();
			yPos += tv.y();
		}
	}
	
	// ======== Interacting =========
	private Boolean interacting = false;
	public void interact_start() {
		velocity.zero();
		interacting = true;
	}
	public void interact_stop() {
		interacting = false;
	}
	public void interact_impulse(int x, int y) {
		xPos = x;
		yPos = y;
	}
	public Boolean isInteracting() {
		return interacting;
	}
	
	// ======== Hovering =========
	private Boolean hovering = false;
	public void hovered() {
		hovering = true;
	}
	public void not_hovered() {
		hovering = false;
	}
	public Boolean isHovering() {
		return hovering;
	}
	
	// ============================   MARK: Getters and Setters ============================ 
	
	public void setRotationalVelocity(double rVel) {
		this.rVel = rVel;
	}
	public double getRotation() {
		return rotation;
	}
	public void setStatic(boolean stasis) {
		this.stasis = stasis;
	}	
	public boolean isStaticObject() {
		return stasis;
	}
	public double getSphereRadius() {
		return sphere_radius;
	}
	public void setPosition(double x, double y) {
		xPos = x; yPos = y;
	}
	public double getxPos() {
		return xPos;
	}
	public double getyPos() {
		return yPos;
	}
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	public Double getLifespan() {
		return lifespan;
	}
	public Image getImg() {
		return this.img;
	}
	public HXCollider getCollider() {
		return collider;
	}
	public void setOwner(HXEntityOwner o) {
		this.owner = o;
	}
	protected double getScale() {
		return owner.getScale();
	}
	
	// --- Derived Getters ---
	protected double getDraw_xPos(float interpolation) {
		return ((xPos - xPos_prev) * interpolation + xPos_prev) * owner.getScale() - getDraw_width()/2;
	}
	protected double getDraw_yPos(float interpolation) {
		return ((yPos - yPos_prev) * interpolation + yPos_prev) * owner.getScale() - getDraw_height()/2;
	}
	protected double getDraw_width() {
		return width * owner.getScale();
	}
	protected double getDraw_height() {
		return height * owner.getScale();
	}
}
package world;

import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import world.entities.*;
import world.helper.properties.HXEntityOwner;
import world.helper.math.HXVector;
import world.helper.properties.HXHoverable;
import world.helper.properties.HXInteractable;
import world.helper.properties.HXPhysicsBody;
 
public class HXWorld implements HXEntityOwner {
	
	private HXInteractable interactTarget = null;
	private HXHoverable hoverTarget = null;
	
	private int width;
	private int height;
	private double scale = 1;
	
	/* === Updates and drawing === */
	private final CopyOnWriteArrayList<HXEntity> entities = new CopyOnWriteArrayList<HXEntity>();

	/**
	 * The HXWorld object owns all entities in a CopyOnWriteArrayList but is drawn in a HXWorldPanel.
	 * <p>
	 * @param parentPanel - The JPanel that draws the world.
	 */
	public HXWorld(int w, int h) {
		this.width = w;
		this.height = h;
		
		// Run anything at start of world...
		for (int x = 0; x < w/60; x++) {
			for (int y = 0; y < h/60; y++) {
				entityAdd(new HXEntityTemplate(x * 60, y * 60));
			}
		}
		
		// ...
	}
	
	/**
	 * Informs the world that the mouse was pressed at the specified coordinates.
	 * <p>
	 * Checks if any HXInteractable entities have been hit by the provided
	 * coordinates. This will notify the interacted target as well as
	 * update the world's current interactTarget field until the mouse is
	 * lifted.
	 * @param x - The x coordinate of the interaction.
	 * @param y - The y coordinate of the interaction.
	 */
	public void interactAt(int x, int y) {
		for (HXEntity e : entities) {
			if (e instanceof HXInteractable) {
				if (e.getCollider().contains((int) (x/scale), (int) (y/scale))) {
					if (hoverTarget != null) {
						hoverTarget.not_hovered();
						hoverTarget = null;
					}
					interactTarget = (HXInteractable) e;
					interactTarget.interact_start();
					break;
				}
			}
		}
	}
	/**
	 * Informs the world object that the mouse was moved.
	 * @param x
	 * @param y
	 */
	public void interactMove(int x, int y) {
		if (interactTarget != null) {
			interactTarget.interact_impulse((int) (x/scale), (int) (y/scale));
		}
	}
	/**
	 * Informs the world object that the mouse was lifted.
	 */
	public void interactStop() {
		if (interactTarget != null) {
			interactTarget.interact_stop();
			interactTarget = null;
		}
	}
	
	/**
	 * Calls any hovered entities' interaction methods
	 * <p>
	 * Goes through all entities that implement the HXHoverable interface
	 * and calls its hovered method if its hitbox was intersected by the
	 * intersection coordinates else it will call the not_hovered method.
	 * @param x - The x coordinate of the interaction. These will be translated
	 * into world coordinates internally (scale and translation).
	 * @param y - The y coordinate of the interaction. These will be translated
	 * into world coordates internally (scale and translation).
	 */
	public void hoverAt(int x, int y) {
		if (interactTarget != null)
			return;
		Boolean hoveredSomething = false;
		for (HXEntity e : entities) {
			if (e instanceof HXHoverable) {
				if (e.getCollider().contains((int) (x/scale), (int) (y/scale))) {
					hoveredSomething = true;
					if (hoverTarget != null)
						hoverTarget.not_hovered();
					hoverTarget = (HXHoverable) e;
					hoverTarget.hovered();
				}
			}
		}
		if (!hoveredSomething && hoverTarget != null) {
			hoverTarget.not_hovered();
			hoverTarget = null;
		}
	}
	
	/**
	 * Adds the argument entity to the target world's entity list and
	 * sets the entity's owner as the target world.
	 * @param e - The entity to be added to the world list and have its
	 * owner field set.
	 */
	private void entityAdd(HXEntity e) {
		entities.add(0, e);
		e.setOwner(this);
	}
	public void entityRemove(HXEntity e) {
		entities.remove(e);
	}
		
	/**
	 * Iterates through all entities in the world's entity list and calls
	 * their draw method.
	 * @param g
	 * @param interpolation
	 */
	public void draw(Graphics g, float interpolation) {
		for (HXEntity e : entities) {
			e.draw(g, interpolation);
		}
	}
	public void updateTick(double dT) {
		
		// Check physicsbody collisions
		for (int e1 = 0; e1 < entities.size()-1; e1++) {
			if (entities.get(e1) instanceof HXPhysicsBody) {
				
				for (int e2 = e1 + 1; e2 < entities.size(); e2++) {
					if (entities.get(e2) instanceof HXPhysicsBody) {
						
						HXVector mtv = entities.get(e1).getCollider().doesOverlap(entities.get(e2).getCollider());
						if (mtv != null) {
							entities.get(e1).impulse(mtv.opposite());
							entities.get(e2).impulse(mtv);
							entities.get(e1).translate(mtv.opposite());
							entities.get(e2).translate(mtv);
						}
					}
				}
				
			}
		}
		
		// Call entity updates
		for (HXEntity e : entities) {
			e.update(dT);
		}
	}
	
	// Mark: Getters/Setters =======================================
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale() {
		return scale;
	}
	public boolean isInteracting() {
		if (interactTarget != null) {
			return true;
		}
		return false;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
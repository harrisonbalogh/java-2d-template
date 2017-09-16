package world;

import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;

import world.entities.HXEntity;
import world.entities.HXEntityTemplate;
import world.helper.properties.HXInteractable;
 
public class HXWorld {
	
	private int width;
	private int height;
	private double scale = 1;
	
	/* === Updates and drawing === */
	private final CopyOnWriteArrayList<HXEntity> entities = new CopyOnWriteArrayList<HXEntity>();
	private final CopyOnWriteArrayList<HXEntity> physicsBodies = new CopyOnWriteArrayList<HXEntity>();

	/**
	 * The HXWorld object owns all entities in a CopyOnWriteArrayList but is drawn in a HXWorldPanel.
	 * <p>
	 * @param parentPanel - The JPanel that draws the world.
	 */
	public HXWorld(int w, int h) {
		this.width = w;
		this.height = h;
		
		// Run anything at start of world...
		for (int x = 0; x < width/50; x++) {
			for (int y = 0; y < height/50; y++) {
				// This is making the visual grid in the world
				new HXEntityTemplate(x*50, y*50, 0, 0, this);
			} 
		}
		// ...
	}
	
	/**
	 * Calls any targeted entities' interaction methods
	 * <p>
	 * Goes through all entities that deploy the HXInteractable interface
	 * and calls its interact method if its hitbox was intersected by the 
	 * interaction coordinates.
	 * @param x - The x coordinate of the interaction
	 * @param y - The y coordinate of the interactoin
	 */
	public void interactAt(int x, int y) {
		for (HXEntity e : entities) {
			if (e instanceof HXInteractable) {
				if (e.getRect().contains(x, y)) {
					((HXInteractable) e).interact();
				}
			}
		}
	}
	
	public void entityAdd(HXEntity e) {
		entities.add(0, e);
	}
	public void entityRemove(HXEntity e) {
		entities.remove(e);
	}
	
	public void draw(Graphics g, float interpolation) {
		for (HXEntity e : entities) {
			e.draw(g, interpolation);
		}
	}
	public void updateTick() {
		for (HXEntity e : entities) {
			e.update();
		}
	}
	
	// Mark: Getters/Setters =======================================
	
	public CopyOnWriteArrayList<HXEntity> getPhysicsBodies() {
		return physicsBodies;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		for (HXEntity e : entities) {
			e.setScale(scale);
		}
	}
	public double getScale() {
		return scale;
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
package world.entities;

import java.awt.Color;
import java.awt.Graphics;
import readers.HXKey;
import world.helper.properties.HXHoverable;
import world.helper.properties.HXInteractable;
import world.helper.properties.HXPhysicsBody;

public class HXEntityTemplate extends HXEntity implements HXInteractable, HXHoverable {

	private static final int DEFAULT_WIDTH = 60, DEFAULT_HEIGHT = DEFAULT_WIDTH;

	/**
	 * Template for creating a new entiity.
	 * @param xPos
	 * @param yPos
	 * @param xVel
	 * @param yVel
	 * @param w
	 */
	public HXEntityTemplate(int xPos, int yPos) {
		super(null, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, 0, 0, -1);
	}
	
	@Override
	public void draw(Graphics g, float interpolation) {
		super.draw(g, interpolation);
		
		// Draw Shape
		if (isHovering()) {
			g.setColor(Color.lightGray);
			g.fillRect((int)getDraw_xPos(interpolation), (int)getDraw_yPos(interpolation), (int)getDraw_width(), (int)getDraw_height());
		}
		if (isInteracting()) {
			g.setColor(Color.lightGray);
			g.fillRect((int)getDraw_xPos(interpolation), (int)getDraw_yPos(interpolation), (int)getDraw_width(), (int)getDraw_height());
			g.setColor(Color.red);
		} else
			g.setColor(Color.black);
		g.drawRect((int)getDraw_xPos(interpolation), (int)getDraw_yPos(interpolation), (int)getDraw_width(), (int)getDraw_height());

	}

	@Override
	public void update(double dT) {
		super.update(dT);
		
	}
}

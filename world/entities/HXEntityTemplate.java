package world.entities;

import java.awt.Color;
import java.awt.Graphics;

import world.HXWorld;
import world.helper.properties.HXInteractable;

public class HXEntityTemplate extends HXEntity implements HXInteractable{

	private final int DEFAULT_WIDTH = 50, DEFAULT_HEIGHT = 50;
	
	/**
	 * Template for creating a new entiity. Should always override drawing, to have
	 * some sort of visual appearence in the world. But should still call super.draw()
	 * Constructor should always call init() method of HXEntity
	 * @param xPos
	 * @param yPos
	 * @param xVel
	 * @param yVel
	 * @param w
	 */
	public HXEntityTemplate(int xPos, int yPos, double xVel, double yVel, HXWorld w) {
		init(null, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, xVel, yVel, 1, -1, w.getScale(), w);
	}
	
	/**
	 * Unless it has no visuals, it should override draw() but still call super.draw()
	 */
	@Override
	public void draw(Graphics g, float interpolation) {
		super.draw(g, interpolation);
		
		g.setColor(Color.gray);
		g.drawRect(
				getDraw_xPos(), 
				getDraw_yPos(), 
				(int) (getWidth() * getScale()), 
				(int) (getHeight() * getScale()));
	}

	@Override
	public void update() {
		super.update();
	}

	/**
	 * From the HXClickable interface. Add functionality for what happens when the object is clicked on in world
	 */
	@Override
	public void interact() {
		// TODO Auto-generated method stub
		
	}
}
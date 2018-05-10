package main;

import java.awt.EventQueue;
import java.util.Random;

import ui.HXMasterWindow;

public class HXStartup {
	
	// Version 2 implemented a camera class for panning.
	// Version 3 implemented a zoomable camera class.
	// Version 4 updates:
	/*
	  	HXEntity no longer uses init(). This method required remembering to call init() in a child
	  	classes ctor whenever subclassing HXEntity. Changing 'init' to HXEntity's ctor and making its
	  	visibility 'protected' forces subclasses to call this ctor or a compile error will be thrown.
	  	
	  	The 'contains' method of HXCollider now handles a polygon of any vertex count.
	  	
	  	No longer necessary to implement the HXHoverable or HXInteractable interface methods in an Entity subclass,
	  	the interface methods are already in the Entity abstract class. This doesn't mean that all entities
	  	have the hoverable and/or interactable property, they will still need to declare their interface implementation
	  	in the class prototype in order to be checked with mouse interactions.
	  	
	  	Entity colliders are automatically created if the entity subclass requires them. That is, if the
	  	entity implements HXPhysicsBody, HXHoverable, HXInteractable, etc it will be given a collider.
		
		Colliders no longer have to moved with the entity as they are now relative to the entity's position.
		So the collider will reference the entity's position when performing calculations.
		
		Many more fields are private in the Entity abstract class and some are derived methods from instance variables.
	 */
	

	public static Random rand = new Random();
	public static HXMasterWindow masterWindow;

	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					masterWindow = new HXMasterWindow();
					masterWindow.pack();
					masterWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

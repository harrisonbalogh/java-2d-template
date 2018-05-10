package main;

import java.awt.EventQueue;
import java.util.Random;

import ui.HXMasterWindow;

public class HXStartup {
	
	// VERSION LIST
	// ================================================
	// Version 2 implemented a camera class for panning.
	// Version 3 implemented a zoomable camera class.
	

	public static Random rand = new Random();
	public static HXMasterWindow masterWindow;

	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					new HXImageLoader();
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

# java-2d-template

## Template

This project has the core implementations for rendering various interactive graphics
in Java. View some of the projects made from this project below:

---
<iframe width="700" height="394" src="https://www.youtube.com/embed/M0oaNy9K2H8" title="YouTube video player" frameborder="0" allow="clipboard-write; encrypted-media; picture-in-picture" allowfullscreen /> 
---

## Structure

____
main.engine.HXStartup
	-> Where the main method resides
	• Static reference to singleton HXMasterWindow and instantiates

____
ui.HXMasterWindow:
-> The applications primary UI window
• Contains the panel for 2D world rendering
  subclassed as HXViewPanel
• Adjustable constants for application window’s
  dimensions plus child HXViewPanel dimensions.
• References input.HXKey file for keyboard button
  presses, initialized in this class

____
input.HXKey:
-> Stores static fields for all keyboard
    keys used in the application
• Add to the static HashMap to listen for
  more keyboard presses

____
ui.HXViewPanel: (Deprecated - Now only uses a HXWorldPanel with all entities shifted)
-> Owns a panel where the 2D world’s entities 
    are drawn subclassed as HXWorldPanel. Also 
    handles the panning of that world with the 
    selected keys
• Nothing in this class needs adjusting for
  forked projects
____
ui.HXWorldPanel:
-> A panel that renders all of its HXWorld’s entities array
• Nothing in this class needs adjusting for forked projects
• Owns a HXClock
• Implements the HXClockRenderer interface for repaintWorld

____
world.HXClock:
-> Pulses on a fixed timestep loop to its parent panel
• Nothing in this class needs adjusting for forked
  projects
• Allows world to be paused
• Calls parent panel repaintWorld
• Calls parent panel’s world updateEntities

____
world.HXWorld:
-> Intended to hold properties like friction and gravity. 
    Currently contains an array of all entities as well as
    the size of the world
• Adjustable constants for the world’s x:y dimensions

    
package world.helper.properties;

public interface HXInteractable {
	
	public void interact_start();
	public void interact_stop();
	public void interact_impulse(int x, int y);
	
	public Boolean isInteracting();
	
}

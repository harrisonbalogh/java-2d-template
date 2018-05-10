package world.helper.properties;

import world.entities.HXEntity;

/**
 * When a world adds an entity, it will set that entities owner field
 * to itself as an HXEntityOwner, so the methods in this interface are
 * what get exposed to calls on the owner field.
 * @author harrisonbalogh
 *
 */
public interface HXEntityOwner {
	
	public void entityRemove(HXEntity e);
	public double getScale();

}

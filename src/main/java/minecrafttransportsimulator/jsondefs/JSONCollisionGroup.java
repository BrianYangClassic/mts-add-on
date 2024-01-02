package minecrafttransportsimulator.jsondefs;

import java.util.List;

import minecrafttransportsimulator.packloading.JSONParser.JSONDescription;
import minecrafttransportsimulator.packloading.JSONParser.JSONRequired;

public class JSONCollisionGroup{
	
	@JSONDescription("Normally, collision boxes collide with blocks.  However, excess block-based collision checks will SIGNIFICANTLY impact TPS performance.  As such, this should be set to true for all but the most essential collision boxes.")
    public boolean isInterior;
	
	@JSONDescription("How much health this collision group has.  When the health reaches 0, this collision group will be disabled.  Useful for armor that can only take so many hits, or parts of models that you want to break off when hit.  If this value is 0, then the collision group will always be present, and damage inflicted will be charged to the entity rather than this collision group.")
    public int health;
	
	@JSONRequired
	@JSONDescription("A listing of collisions for this group.")
    public List<JSONCollisionBox> collisions;
	
	@JSONDescription("A optional listing of animations to use to modify the collision boxes.  Translation/rotation animations do what you would expect.  Visibiity animations will completely disable the hitbox if they are false.")
	public List<JSONAnimationDefinition> animations;
}

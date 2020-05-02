import java.util.List;

/**
 * The actor class is a super class for all variables in the field including 
 * the abstract class Animal and its concrete subclasses. 
 *
 * @author Erik k   
 * @version 5/1/2020
 */
public interface Actor
{
    /**
     * Puts the object into action, whatever the action may be for the object.
     * @param newActors A list to recieve new actor objects.
     */
    void act(List<Actor> newActors);
    
    /**
     * Returns a boolean value to find out if the object is till active.
     * @return Is this thing still doing stuff
     */
    boolean isActive();
    
    /**
     * Kills the thing.
     */
    void setDead();
}

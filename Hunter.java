import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Hunters move to random location in the field and fire a fixed number of 
 * shots to random locations in the field, killing whatever is in that 
 * location, even other hunters or themselves. They don't eat or breed and
 * have no max age. The hunter collects his or her kills (probably a him 
 * because only guys are dumb enough to hunt foxes and rabbits) mostly so
 * we can make sure that the hunter is actually working.
 * 
 * @author Erik
 * @version 5/2/2020
 */
public class Hunter implements Actor
{
    // Number of shots per step.
    private final int NUMBER_OF_SHOTS = 3;
    // Is this hunter still active? or has he been shot by another hunter
    private boolean isActive;
    // The field that the hunter accupies.
    private Field field;
    // The location of the hunter.
    private Location location;
    // The animals (or other hunters) that the huner has shot.
    private ArrayList<Actor> bagOfDeadAnimals;
    // A randomizer for this hunter that can be shared.
    private Random rando = Randomizer.getRandom();

    /**
     * Constructor for objects of class Hunter
     */
    public Hunter(Field field, Location location)
    {
        isActive = true;
        this.field = field;
        this.location = location;
        bagOfDeadAnimals = new ArrayList<>();
    }

    /**
     * This is the primary method for the hunter class that puts the hunter
     * into action. more to come!!!!!!!!!!!!!!!
     * @param Ignore.
     */
    public void act(List<Actor> newActors){
        if (isActive()){
            // Move to random location and kill objects in location.
            Location newLocation = field.getRandomLocation();
            setLocation(newLocation);
            // Let the hunter hunt
            hunt();
        }
    }
    
    /**
     * The hunter wildly fires a set number of shots at random locations
     * hoping to kill anything that moves so he can put it in his bag of 
     * dead animals to show off to his buddies later while they drink 
     * budlight. If he is not careful he may end up in his own bag of dead
     * animals.
     */
    private void hunt(){
        for(int shot = 0; shot < NUMBER_OF_SHOTS; shot++){
            kill(field.getRandomLocation());
        }
    }
    
    // /**
     // * Return a radom location in field.
     // * @return Random location in field.
     // */
    // public Location getRandomLocation(){
        // int row = rando.nextInt(1 + field.getDepth());
        // int col = rando.nextInt(1 + field.getWidth());
        // Location location = new Location(row, col);
        // return location;
    // }
    
    /**
     * Returns the boolean value indicating if the hunter is still hunting.
     * @return Is the hunter still active.
     */
    public boolean isActive(){
        return isActive;
    }

    /**
     * Return the array that holds all the animals killed by this hunter.
     * @return The bagOfDeadAnimals array.
     */
    public ArrayList<Actor> getbagOfDeadAnimals(){
        return bagOfDeadAnimals;
    }

    /**
     * Put the hunter in their gender neutral new location.
     * @param newLocation The new location of this hunter.
     */
    private void setLocation(Location newLocation)
    {
        if(location != null) {
            // The hunter also kills anything nearby.
            kill(newLocation);
            // Clear location is redundant on purpose.
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Kill the animal in the space the hunter occupies in the field.
     * @param location The new location of the hunter.
     */
    private void kill(Location location){
        Object nearbyAnimal = field.getObjectAt(location);
        if (nearbyAnimal instanceof Actor && nearbyAnimal != null){
            Actor deadAnimal = (Actor) nearbyAnimal;
            deadAnimal.setDead(); 
            bagOfDeadAnimals.add(deadAnimal);
            field.clear(location);
        }
    }
    
    /**
     * Kill this hunter.
     */
    public void setDead(){
        isActive = false;
    }
}
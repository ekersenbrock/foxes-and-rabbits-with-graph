import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.03.18
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setBreedingAge(5);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    protected void act(List<Animal> newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    // /**
     // * Check whether or not this rabbit is to give birth at this step.
     // * New births will be made into free adjacent locations.
     // * @param newRabbits A list to return newly born rabbits.
     // */
    // private void giveBirth(List<Animal> newRabbits)
    // {
        // // New rabbits are born into adjacent locations.
        // // Get a list of adjacent free locations.
        // Field field = getField();
        // List<Location> free = field.getFreeAdjacentLocations(getLocation());
        // int births = breed(); 
        // for(int b = 0; b < births && free.size() > 0; b++) {
            // Location loc = free.remove(0);
            // Rabbit young = new Rabbit(false, field, loc);
            // newRabbits.add(young);
        // }
    // }
    
    /**
     * Returns the max age of this animal.
     * @return The max age of the animal.
     */
    protected int getMaxAge(){
        return MAX_AGE;
    }
    
    /**
     * Return the breeding probability of this rabbit.
     * @return The breeding probability of the rabbit.
     */
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns the maximum number of offspring this animal can have.
     * @return The max number of offspring.
     */
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Returns a new rabbit.
     * @param randomAge Decide of the rabbit is at the beginning of life.
     * @param field The field the rabbit is in.
     * @param loc The location of the Rabbit in the field.
     * @return The new rabbit.
     */
    protected Rabbit newAnimal(Boolean randomAge,Field field, Location loc){
        return new Rabbit(false, field, loc);
    }
}

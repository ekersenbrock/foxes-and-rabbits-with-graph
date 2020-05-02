import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Write a description of class Cat here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Cat extends Animal
{
    // Characteristics shared by all cats (class variables).

    // The age to which a cat can live.
    private static final int MAX_AGE = 210;
    // The likelihood of a cat breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a cat can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 13;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The cat's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a cat. A cat can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the cat will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Cat(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setBreedingAge(15);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            setAge(0);
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the cat does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newCats A list to return newly born cats.
     */
    public void act(List<Actor> newCats) 
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newCats);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Make this cat more hungry. This could result in the cat's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this cat is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCats A list to return newly born cats.
     */
    protected void giveBirth(List<Actor> newCats)
    {
        // New cats are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Cat young = new Cat(false, field, loc);
            newCats.add(young);
        }
    }
    
    protected int getMaxAge(){
        return MAX_AGE;
    }
    
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }
    
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Returns a new cat.
     * @param randomAge Decide of the cat is at the beginning of life.
     * @param field The field the cat is in.
     * @param loc The location of the cat in the field.
     * @return The new cat.
     */
    protected Cat newAnimal(Boolean randomAge,Field field, Location loc){
        return new Cat(false, field, loc);
    }
}

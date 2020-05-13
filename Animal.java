import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.03.18
 */
public abstract class Animal implements Actor
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    //The age at which the animal can start to breed.
    private int breedingAge = 0;
    // The age of the animal
    private int age;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0;
    }
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Set the age at which an animal can start to breed.
     * @param breedingAge The max age an animal can breed.
     */
    protected void setBreedingAge(int breedingAge){
        this.breedingAge = breedingAge;
    }
    
    /**
     * Get the breeding age of an animal
     */
    protected abstract int getBreedingAge();
    
    /**
     * An animal can breed if it has reached the breeding age.
     * @return true if the animal can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }
    
    protected int getAge(){
        return age;
    }
    
    protected void setAge(int age){
        this.age = age;
    }
    
    /**
     * Returns the max age of this animal.
     * @return The max age of the animal.
     */
    abstract protected int getMaxAge();
    
    /**
     * Increase the age. This could result in the animal's death.
     */
    protected void incrementAge() 
    {
        age++;
        if(getAge() > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Return the breeding probability of this animal.
     * @return The breeding probability of the animal.
     */
    protected abstract double getBreedingProbability();
    
    /**
     * Returns the maximum number of offspring this animal can have.
     * @return The max number of offspring.
     */
    protected abstract int getMaxLitterSize();
    
    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newanimales A list to return newly born animals.
     */
    protected void giveBirth(List<Actor> newAnimals)
    {
        // New animales are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = newAnimal(false, field, loc);
            newAnimals.add(young);
        }
    }
    
    /**
     * Returns a new animal.
     * @param randomAge Decide of the animal is at the beginning of life.
     * @param field The field the animal is in.
     * @param loc The location of the animal in the field.
     * @return The new animal.
     */
    protected abstract Animal newAnimal(Boolean randomAge,Field field, 
        Location loc);
        
    /**
     * 
     */
    public boolean isActive(){
        return isAlive();
    }
}

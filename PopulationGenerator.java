import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.util.HashMap;

/**
 * The population generator is in charge of populating the field during 
 * initialization of the simulator or during resets. It also keeps an 
 * array of the actors objects and the field that they occupy. 
 * 
 * @author Erik K
 * @version 5/13/2020
 */
public class PopulationGenerator
{
    // The probability that a fox will be created.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;  
    // The probability that a cat will be created.
    private static final double CAT_CREATION_PROBABILITY = 0.01;
    // The number of hunters in the field.
    private static final int NUMBER_OF_HUNTERS = 5;
    // Decide if cats are included in the simulation.
    private final boolean withCats = false;
    // Decide if hunters are include in the simulation.
    private final boolean withHunters = true;
    
    // List of actors in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;
    // Stores the color associated with the animal.
    private HashMap<String, String> animalColors;
    
    /**
     * Construct the population generator and give it a field to populate
     * and a list of the available views.
     * 
     * @param field The field the actors occupy.
     * @param views the available viewing options.
     */
    public PopulationGenerator(Field field,List<SimulatorView> views)
    {
        actors = new ArrayList<>();
        this.field = field;
        for (SimulatorView view : views){
            setColor(view);
        }
    }
    
    /**
     * Sets the colors for each actor in the simulator views.
     */
    private void setColor(SimulatorView view){
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        if (withCats){
            view.setColor(Cat.class, Color.RED);
        }
        if (withHunters){
            view.setColor(Hunter.class, Color.BLACK);
        }
    }
    
    /**
     * Randomly populate the field with the animals. Optionally, a given
     * number of hunters are also created.
     */
    public void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    actors.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    actors.add(rabbit);
                }
                else if(rand.nextDouble() <= CAT_CREATION_PROBABILITY && withCats) {
                    Location location = new Location(row, col);
                    Cat cat = new Cat(true, field, location);
                    actors.add(cat);
                }
                // else leave the location empty.
            }
        }
        int numberOfHunters = 0;
        // Place the hunters in random empty locations in the field.
        while (numberOfHunters < NUMBER_OF_HUNTERS && withHunters){
            Location location = field.getRandomLocation();
            if (field.getObjectAt(location) == null){
                Hunter hunter = new Hunter(field, location);
                actors.add(hunter);
                numberOfHunters++;
            }
        }
    }
    
    /**
     * Add actors the the actors list.
     *
     * @param newactors The actors to be added.
     */
    public void addActors(List<Actor> newActors){
        actors.addAll(newActors);
    }
    
    /**
     * Returns the list of the actors.
     *
     * @return actors The list of actors.
     */
    public List<Actor> getActors(){
        return actors;
    }
    
    /**
     * Return the field.
     * 
     * @return The field.
     */
    public Field getField(){
        return field;
    }
    
    /**
     * Empty the list of actors for resets.
     */
    public void clearListOfActors(){
        actors.clear();
    }
    
    /**
     * Get a fixed size array containing the hunters that haven't been 
     * hunted.
     * @return An array containing the active hunters.
     */
    public Object[] getLivingHunters(){
        Object[] livingHunters = actors.stream()
            .filter(actor -> actor instanceof Hunter)
            .toArray();
        return livingHunters;
    }
    
    /**
     * Check to see if any of the hunters killed any of the other hunters
     * @return The number of hunters killed by each hunter.
     */
    public void whoKilledTheHunters(){
        Object[] livingHunters = getLivingHunters();  
        for (int i = 0; i < livingHunters.length; i++){
            Hunter hunter = (Hunter) livingHunters[i];
            long deadHunters = hunter.getBagOfDeadAnimals().stream()
                .filter(deadAnimal -> deadAnimal instanceof Hunter)
                . count();
            if (deadHunters == 1){
                System.out.println("Hunter " + (i + 1) + " killed " + 
                deadHunters + " other hunter");
            }
            else {
                System.out.println("Hunter " + (i + 1) + " killed " + 
                deadHunters + " other hunters");
            }
        }
    }
    
    /**
     * How many of each animal did the hunters kill.
     */
    public void countKills(){
        Hunter[] hunters = (Hunter[]) getLivingHunters();
        for (int i = 0; i < hunters.length; i++){
            System.out.println("Hunter " + (i + 1) + " killed " + 
            hunters[i].howManyRabbits() + " rabbits, " + 
            hunters[i].howManyFoxes() + " foxes and " + 
            hunters[i].howManyHunters() + " hunters.");
        }
    }
}

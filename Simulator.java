import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.03.18
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private List<SimulatorView> views;
    // Generates actors, holds actors List and field.
    private PopulationGenerator popGen;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        // An array to hold the different simulator views.
        views = new ArrayList<>();
        // Creates the grid view with desired size.
        SimulatorView view = new GridView(depth, width);
        views.add(view);
        // Creates the graph view with desired size.
        view = new GraphView(500, 150, 500);
        views.add(view);
        
        // Instantiates the field to pass into the new population generator.
        Field field = new Field(depth, width);
        popGen = new PopulationGenerator(field, views);
        
        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; 
                step <= numSteps && views.get(0).isViable(popGen.getField()); 
                step++) {
            simulateOneStep();
            // delay(60);   // uncomment this to run more slowly
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn animals.
        List<Actor> newActors = new ArrayList<>(); 
        // Create an iterator to remove inactive actors.
        Iterator<Actor> it = popGen.getActors().iterator();
        // Let all actors act.
        while(it.hasNext()) {
            Actor actor = it.next();
            actor.act(newActors); 
            if(! actor.isActive()) {
                it.remove();
            }
        }

        // Add the newly born animals to the main lists.
        popGen.addActors(newActors);

        updateViews();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        popGen.clearListOfActors(); 
        for (SimulatorView view : views) {
            view.reset();
        }

        popGen.populate();
        updateViews();
    }

    /**
     * Update all existing views.
     */
    private void updateViews()
    {
        for (SimulatorView view : views) {
            view.showStatus(step, popGen.getField());
        }
    }

    /**
     * Check to see if any of the hunters killed any of the other hunters
     * @return The number of hunters killed by each hunter.
     */
    public void whoKilledTheHunters(){
        popGen.whoKilledTheHunters();
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}

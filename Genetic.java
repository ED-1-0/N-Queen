import java.util.*;


public class Genetic {
    private int cost;
    private long runtime;
    private int sizeOfPuzzle;
    private GeneticBoard initialBoard;
    private GeneticBoard solutionBoard;

    public Genetic(int sizeOfPuzzle) {
        this.sizeOfPuzzle = sizeOfPuzzle;
        int populationSize = 20;

        PriorityQueue<GeneticBoard> population = new PriorityQueue<>(generatePopulation(populationSize));
        ArrayList<GeneticBoard> newPopulation;
        ArrayList<GeneticBoard> topXPercent;
        Random rand = new Random();

        double percentToGrab = .30;
        int elites = Math.toIntExact(Math.round(population.size() * percentToGrab));

        long startTimeOut = System.currentTimeMillis();
        long timeOut = startTimeOut + 2 * 1000;

        GeneticBoard current = population.peek();
        initialBoard = population.peek();

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < timeOut && !current.isSolved()) {
            newPopulation = new ArrayList<>();
            topXPercent = new ArrayList<>();

            for (int i = 0; i < elites; i++) {
                topXPercent.add(population.poll());
            }

            for (int i = 0; i < populationSize; i++) {

                GeneticBoard mom = topXPercent.get(rand.nextInt(topXPercent.size()));
                GeneticBoard dad = topXPercent.get(rand.nextInt(topXPercent.size()));
                GeneticBoard child = generateChild(mom, dad);

                if (child.isSolved()) {
                    current = child;
                    break;
                } else {
                    newPopulation.add(child);
                }

            }

            population = new PriorityQueue<>(newPopulation);
        }

        runtime = System.currentTimeMillis() - startTime;
        solutionBoard = current;
    }

    public int getCost() {
        return cost;
    }

    public long getRuntime() {
        return runtime;
    }

    public boolean isSolved() {
        return solutionBoard.totalNumberOfAttackingQueens() == 0;
    }

    public GeneticBoard generateChild(GeneticBoard mom, GeneticBoard dad) {
        cost += 1;
        Random rand = new Random();
        int[] momBoard = mom.getBoard();
        int[] dadBoard = dad.getBoard();
        int crossover =  rand.nextInt((momBoard.length - 2)) + 2;

        int[] childBoard = new int[momBoard.length];
        for (int i = 0; i < momBoard.length; i++) {
            if (i >= crossover) {
                childBoard[i] = momBoard[i];
            } else {
                childBoard[i] = dadBoard[i];
            }
        }

        return mutate(new GeneticBoard(childBoard));
    }

    public GeneticBoard mutate(GeneticBoard child) {
        Random rand = new Random();
        double mutationRate = .01;

        for (int i = 0; i < child.getSize(); i++) {
            if (rand.nextDouble() <= mutationRate) {
                child = child.moveQueenRandomly(i);
            }
        }

        return child;
    }

    public GeneticBoard getSolutionBoard() {
        return solutionBoard;
    }

    public ArrayList<GeneticBoard> generatePopulation(int numToGenerate) {
        ArrayList<GeneticBoard> population = new ArrayList<>();

        for (int i = 0; i < numToGenerate; i++) {
            population.add(new GeneticBoard(GeneticRunner.generateBoard(sizeOfPuzzle)));
        }

        return population;
    }

    public GeneticBoard getInitialBoard(){
        return initialBoard;
    }
}
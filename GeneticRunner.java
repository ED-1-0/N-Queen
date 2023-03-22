import java.util.Random;

public class GeneticRunner {

    public static int[] generateBoard(int size) {
        int[] generatedBoard = new int[size];
        Random rand = new Random();
        for (int i = 0; i < generatedBoard.length; i++) {
            int n = rand.nextInt(size);
            generatedBoard[i] = n;
        }
        return generatedBoard;
    }

    public static void algorithmAnalysis(boolean useGenetic, int numberOfPuzzles, int sizeOfPuzzle) {
        int solvedProblems, searchCostTotal, runtimesTotal;
        solvedProblems = searchCostTotal = runtimesTotal = 0;
        Genetic geneticAlgorithm;

        for (int i = 1; i <= numberOfPuzzles; i++) {
            if (useGenetic) {
                geneticAlgorithm = new Genetic(sizeOfPuzzle);
                if (geneticAlgorithm.isSolved()) {
                    solvedProblems += 1;
                }
                searchCostTotal += geneticAlgorithm.getCost();
                runtimesTotal += geneticAlgorithm.getRuntime();

            }
        }

        System.out.println("Averages for board of size " + sizeOfPuzzle);
        double percentSolvedPuzzles = (solvedProblems * 1.0 / numberOfPuzzles) * 100.0;
        System.out.println("Percentage of puzzles solved: " + percentSolvedPuzzles + "%");
        System.out.println("Average search cost: " + searchCostTotal/ numberOfPuzzles);
        if (useGenetic) {
            System.out.println("Average runtime: " + (runtimesTotal/ numberOfPuzzles) + "ms");
        } else {
            System.out.println("Average runtime: " + (runtimesTotal/ numberOfPuzzles) * 0.000001 + "ms");
        }
    }

    public static void get3Solutions(int sizeOfPuzzle) {
        GeneticBoard board;
        Genetic genetic;

        System.out.println("Genetic");
        System.out.println("============================================================");
        for (int i = 1; i <= 3; i++) {
            genetic = new Genetic(8);
            System.out.println("Initial board (1st board in pop pq): ");
            System.out.println(genetic.getInitialBoard());

            System.out.println("Solved board: ");
            System.out.println(genetic.getSolutionBoard());

        }

    }
    public static void main(String[] args) 
    {
        long startTime = System.currentTimeMillis();
        get3Solutions(8);
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Total time: " + totalTime + "ms");
    }


}
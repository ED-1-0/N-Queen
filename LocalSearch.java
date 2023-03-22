import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LocalSearch 
{
	public static void main(String args[]) 
    {
		
		System.out.println("Please define n (more than 3) for chessboard size: \n");
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.nextLine());
		sc.close();
		steepestAscentHillClimbingMain(n);
		hillClimbingWithSidewaysMoveMain(n);
		randomRestartHillClimbing(n, false);
		randomRestartHillClimbing(n, true);

	}

	private static void steepestAscentHillClimbingMain(int n) 
    {
		ChessboardState state = new ChessboardState();
		int[] currentBoard = new int[n];
		state.setNoOfTries(0.0f);
		state.setGoalReached(false);
		System.out.println("\nSteepest-ascent Hill Climbing : ");
		for (int i = 0; i < 500; i++) 
        {
			currentBoard = generateChessBoard(n);
			steepestAscentHillClimbing(currentBoard, state, i);

			if (state.isGoalReached()) 
            {
				state.setNoOfTries(state.getNoOfTries() + 1);
				state.setGoalReached(false);
			}
		}
		float successRate = (state.getNoOfTries() / 500) * 100;
		System.out.println("\nSteepest-ascent Hill Climbing Results: ");
		System.out.println("Success Rate:: " + successRate + " %");
		System.out.println("Failure Rate:: " + (100.0f - successRate) + " %");
		System.out.println("Success Average: "
				+ String.format("%.00f", state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()));
		System.out.println("Failure Average: "
				+ String.format("%.00f", state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble()));

	}

	private static void hillClimbingWithSidewaysMoveMain(int n) 
    {
		ChessboardState state = new ChessboardState();
		state.setNoOfTries(0.0f);
		state.setGoalReached(false);
		int[] currentBoard = new int[n];
		System.out.println("\nHill Climbing With Sideways Moves: ");
		for (int i = 0; i < 500; i++) 
        {
			currentBoard = generateChessBoard(n);
			hillClimbingWithSidewaysMove(currentBoard, state, i);

			if (state.isGoalReached()) 
            {
				state.setNoOfTries(state.getNoOfTries() + 1);
				state.setGoalReached(false);
			}
		}
		float successRate = (state.getNoOfTries() / 500) * 100;
		System.out.println("\nHill Climbing With Sideways Moves Results: ");
		System.out.println("Success Rate:: " + successRate + " %");
		System.out.println("Failure Rate:: " + (100.0f - successRate) + " %");
		System.out.println("Success Average: "
				+ String.format("%.00f", state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()));
		System.out.println("Failure Average: " + String.format("%.00f", (state.getFailureSteps().isEmpty() ? 0
				: state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble())));

	}

	private static void randomRestartHillClimbing(int n, boolean sidewayMoves) 
    {
		ChessboardState state = new ChessboardState();
		state.setNoOfTries(1.0f);
		state.setGoalReached(false);
		int[] currentBoard = new int[n];
		int printCount = 0;
		currentBoard = generateChessBoard(n);
		if (sidewayMoves) 
        {
			System.out.println("\nRandom Restart Hill Climbing With Sideways Moves: ");
			hillClimbingWithSidewaysMove(currentBoard, state, printCount);
		} else 
        {
			System.out.println("\nRandom Restart Hill Climbing Without Sideways Moves: ");
			steepestAscentHillClimbing(currentBoard, state, printCount);
		}
		while (!state.isGoalReached()) 
        {
			currentBoard = generateChessBoard(n);
			state.setNoOfTries(state.getNoOfTries() + 1);
			printCount++;
			if (sidewayMoves) 
            {
				hillClimbingWithSidewaysMove(currentBoard, state, printCount);
			} 
            else 
            {
				steepestAscentHillClimbing(currentBoard, state, printCount);
			}
		}
		System.out.println("\nResults: ");
		System.out.println("Average restarts required: " + printCount + 1);
		System.out.println("Average Steps: " + String.format("%.00f",
				(state.getFailureSteps().isEmpty()
						? state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()
						: state.getSuccessSteps().stream().mapToDouble(a -> a).average().getAsDouble()
								+ state.getFailureSteps().stream().mapToDouble(a -> a).average().getAsDouble())));

	}
	
	private static int[] steepestAscentHillClimbing(int[] currentBoard, ChessboardState state, int printCount) 
    {
		int currentThreats = calculateHeuristic(currentBoard);
		int min = 0;
		Integer steps = 0;
		if (printCount < 3) 
        {
			System.out.println("\nAttempt " + (printCount + 1));
			printChessboard(currentBoard);
		}

		while (null != currentBoard && min < currentThreats) 
        {
			int[][] heuristicBoard = calculatePotentialMoves(currentBoard);
			min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
			if (min < currentThreats) 
            {

				currentBoard = traverseToNeighbor(min, printCount, currentBoard, heuristicBoard);
				if(null != currentBoard) 
                {
                    steps++;
                    currentThreats = calculateHeuristic(currentBoard);
                    heuristicBoard = calculatePotentialMoves(currentBoard);
                    min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
				}
			}
		}
		if (0 == currentThreats) 
        {
			state.setGoalReached(true);
			state.getSuccessSteps().add(steps);
		} 
        else 
        {
			state.getFailureSteps().add(steps);
		}
		return currentBoard;
	}
	
	private static int[] hillClimbingWithSidewaysMove(int[] currentBoard, ChessboardState state, int printCount) 
    {
		int currentThreats = calculateHeuristic(currentBoard);
		int min = 0, sidewaysMovesCounter = 0;
		int steps = 0;
		if (printCount < 3) {
			System.out.println("\nAttempt " + (printCount + 1));
			printChessboard(currentBoard);
		}
		while (null!=currentBoard && min <= currentThreats && 100 >= sidewaysMovesCounter) {
			if (0 == currentThreats) 
            {
				state.setGoalReached(true);
				state.getSuccessSteps().add(steps);
				break;
			} 
            else 
            {

				int[][] heuristicBoard = calculatePotentialMoves(currentBoard);
				min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
				if (min <= currentThreats) 
                {
					if (min == currentThreats) 
                    {
						sidewaysMovesCounter++;
					} 
                    else 
                    {
						sidewaysMovesCounter = 0;
					}
					currentBoard = traverseToNeighbor(min, printCount, currentBoard, heuristicBoard);
					if(null!=currentBoard) 
                    {
                        steps++;
                        currentThreats = calculateHeuristic(currentBoard);
                        heuristicBoard = calculatePotentialMoves(currentBoard);
                        min = Arrays.stream(heuristicBoard).flatMapToInt(Arrays::stream).min().getAsInt();
					}
				} 
                else 
                {
					state.getFailureSteps().add(steps);
					break;
				}
			}
		}
		if (0 == currentThreats) 
        {
			state.setGoalReached(true);
			state.getSuccessSteps().add(steps);
		} 
        else 
        {
			state.getFailureSteps().add(steps);
		}
		return currentBoard;
	}
	
	private static int[][] calculatePotentialMoves(int[] currentBoard) 
    {
		
		int[][] heuristicBoard = new int[currentBoard.length][currentBoard.length];
		for (int i = 0; i < currentBoard.length; i++) 
        {
			int[] newBoard = currentBoard.clone();
			for (int j = 0; j < currentBoard.length; j++) 
            {
				newBoard[i] = j;
				heuristicBoard[i][j] = calculateHeuristic(newBoard);
				//assigning maximum value to current queen positions, to avoid repeated state generation
				if(i == currentBoard[j]) 
                {
					heuristicBoard[i][j] = 1000;
				}
			}
		}
		return heuristicBoard;
	}

	private static int[] traverseToNeighbor(int min, int printCount, int[] currentBoard, int[][] heuristicBoard) 
    {
		List<int[]> minimumPositions = new ArrayList<>();
		
		int[] cb = new int[currentBoard.length];
		cb = currentBoard.clone();
		
		for (int i = 0; i < heuristicBoard.length; i++) 
        {
			for (int j = 0; j < heuristicBoard.length; j++) 
            {
				if (min == heuristicBoard[i][j] && currentBoard[j]!=i) 
                {
					int[] position = {i, j};
					minimumPositions.add(position);
				}
			}
		}

		// Randomly choosing value for neighbor from best heuristic
		int[] position = minimumPositions.get(new Random().nextInt(minimumPositions.size()));
		cb[position[0]] = position[1];
		
		//checking for duplicate current state
		while (isArrayEqual(cb, currentBoard)) 
        {
			if (minimumPositions.size() == 1) 
            {
				return null;
			} 
            else 
            {
				minimumPositions.remove(minimumPositions.indexOf(position));
				position = minimumPositions.get(new Random().nextInt(minimumPositions.size()));
				cb[position[0]] = position[1];
			}
		}
		if (printCount < 3) 
        {
			printChessboard(cb);
		}
		return cb;
	}
	
	private static int calculateHeuristic(int[] currentBoard) 
    {
		int threats = 0;
		for (int i = 0; i < currentBoard.length; i++) 
        {
			int currentQueen = currentBoard[i];
			for (int j = i + 1; j < currentBoard.length; j++) 
            {
				int potentialThreatPosition = Math.abs(j - i);
				int distanceInQueens = Math.abs(currentQueen - currentBoard[j]);
				if (currentQueen == currentBoard[j] || potentialThreatPosition == distanceInQueens) 
                {
					threats++;
				}
			}
		}
		return threats;
	}
	
	private static void printChessboard(int[] chessBoard) 
    {
		System.out.println(" ");
		System.out.println(Arrays.toString(chessBoard));
		for (int i = 0; i < chessBoard.length; i++) 
        {
			System.out.println(" ");
			for (int j = 0; j < chessBoard.length; j++) 
            {
				if (i == chessBoard[j]) {
					System.out.print("Q ");
				} else {
					System.out.print("0 ");
				}
			}
		}
	}
	
	private static boolean isArrayEqual(int[] cb, int[] currentBoard) 
    {
		boolean equal = true;
		for (int i = 0; i < currentBoard.length; i++) 
        {
			if(cb[i] != currentBoard[i]) 
            {
				equal = false;
			}
		}
		return equal;
	}

	private static int[] generateChessBoard(int n) 
    {
		return new Random().ints(n, 0, n - 1).toArray();
	}
}
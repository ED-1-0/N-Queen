import java.util.Random;

public class GeneticBoard implements Comparable<GeneticBoard>{
    private int[] board;
    private int size;

    public GeneticBoard(int[] board) {
        this.board = board;
        this.size = board.length;
    }

    public GeneticBoard moveQueenRandomly(int index) {
        int[] successor = board.clone();
        Random rand = new Random();
        int n = rand.nextInt(successor.length);

        while (n == successor[index]) {
            n = rand.nextInt(successor.length);
        }

        successor[index] = n;
        return new GeneticBoard(successor);
    }

    public int totalNumberOfAttackingQueens() {
        int numberOfAttackers = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = i + 1; j < board.length; j++) {
                int elem1 = board[i];
                int elem2 = board[j];

                // Checking if on the same row
                if (elem1 == elem2) {
                    numberOfAttackers++;

                // Finding diagonal rows
                } else {
                    int deltaCol = calculateDifference(i, j);
                    int deltaRow = calculateDifference(elem1, elem2);
                    if (deltaCol == deltaRow) {
                        numberOfAttackers++;
                    }
                }
            }
        }

        return numberOfAttackers;
    }

    public int calculateDifference(int a, int b) {
        return  Math.abs(a - b);
    }

    public boolean isSolved() {
        return totalNumberOfAttackingQueens() == 0;
    }

    @Override
    public String toString() {
        StringBuilder printBoard = new StringBuilder();
        for (int i = board.length - 1; i >= 0; i--){
            if (i < 10) {
                printBoard.append(" ").append(i);
            } else {
                printBoard.append(i);
            }
            for (int j = 0; j < board.length; j++) {
                if (i == board[j]) {
                    printBoard.append("  Q");
                } else {
                    printBoard.append("  .");
                }
            }

            printBoard.append("\n");
            if (i == 0) {
                int rowNum = 0;
                while (rowNum < board.length) {
                    if (rowNum == 0) {
                        printBoard.append("   ");
                    }

                    if (rowNum > 0 && rowNum <= 10)  {
                        printBoard.append("  ").append(rowNum);
                        rowNum++;
                    } else {
                        printBoard.append(" ").append(rowNum);
                        rowNum++;
                    }
                }
            }
        }

        return printBoard.toString();
    }

    public int getSize() {
        return size;
    }

    public int[] getBoard() {
        return board;
    }

    @Override
    public int compareTo(GeneticBoard o) {
        int priority1 =  new GeneticBoard(board).totalNumberOfAttackingQueens();
        int priority2 = o.totalNumberOfAttackingQueens();

        if (priority1 < priority2) {
            return -1;
        } else if (priority1 > priority2) {
            return 1;
        } else {
            return 0;
        }

    }

}
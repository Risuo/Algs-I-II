/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {


    private char[] myBoard;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        n = tiles.length;
        myBoard = new char[n * n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                myBoard[index] = (char) tiles[i][j];
                index++;
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n * n; i++) {
            s.append(String.format("%2d ", (int) myBoard[i]));
            if ((i + 1) % (n) == 0) {
                s.append("\n");
            }
        }
        return s.toString();

        // for (int i = 0; i < n; i++) {
        //     for (int j = 0; j < n; j++) {
        //         s.append(String.format("%2d ", tiles[i][j]));
        //     }
        //     s.append("\n");
        // }
        // return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        int index = 1;
        for (char value : myBoard) {
            if ((int) value != index && (int) value != 0) {
                hamming++;
            }
            index++;
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        int index = 1;
        for (char value : myBoard) {

            if ((int) value != index && (int) value != 0) {
                int currentRow = (int) Math.ceil((double) index / n);
                int currentCol = ((index - 1) % n) + 1;
                int correctRow = (int) Math.ceil((double) (int) value / n);
                int correctCol = (((int) value - 1) % n) + 1;
                manhattan = manhattan + (Math.abs(correctRow - currentRow)) +
                        (Math.abs(correctCol - currentCol));
            }
            index++;
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0 && manhattan() == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {

        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) return false;

        for (int i = 0; i < this.myBoard.length; i++) {
            if (this.myBoard[i] != that.myBoard[i]) {
                return false;
            }
        }
        return true;
    }


    // all neighboring boards

    public Iterable<Board> neighbors() {
        int[] toSwitch = new int[0];
        Stack<Board> neighborStack = new Stack<>();
        int index = 1;
        int blankSpace = 0, currentRow = 0, currentCol = 0;
        for (char value : myBoard) {
            if ((int) value == 0) {
                blankSpace = index;
                currentRow = (int) Math.ceil((double) index / n);
                currentCol = ((index - 1) % n) + 1;
                break;
            }
            index++;
        }

        // StdOut.println("blankSpace: " + blankSpace + " currentRow: " + currentRow +
        //                        " currentCol: " + currentCol);

        // corners
        // top left corner
        if (currentCol == 1 && currentRow == 1) {
            toSwitch = new int[2];
            toSwitch[0] = blankSpace + 1;
            toSwitch[1] = blankSpace + n;
        }
        // bottom left corner
        if (currentCol == 1 && currentRow == n) {
            toSwitch = new int[2];
            toSwitch[0] = blankSpace + 1;
            toSwitch[1] = blankSpace - n;
        }
        // top right corner
        if (currentCol == n && currentRow == 1) {
            toSwitch = new int[2];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace + n;
        }
        // bottom right corner
        if (currentCol == n && currentRow == n) {
            toSwitch = new int[2];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace - n;
        }
        // sides not corners
        // top side
        if (currentRow == 1 && currentCol != 1 && currentCol != n) {
            toSwitch = new int[3];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace + 1;
            toSwitch[2] = blankSpace + n;
        }
        // bottom side
        if (currentRow == n && currentCol != 1 && currentCol != n) {
            toSwitch = new int[3];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace + 1;
            toSwitch[2] = blankSpace - n;
        }
        // left side
        if (currentCol == 1 && currentRow != 1 && currentRow != n) {
            toSwitch = new int[3];
            toSwitch[0] = blankSpace + 1;
            toSwitch[1] = blankSpace + n;
            toSwitch[2] = blankSpace - n;
        }
        // right side
        if (currentCol == n && currentRow != 1 && currentRow != n) {
            toSwitch = new int[3];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace + n;
            toSwitch[2] = blankSpace - n;
        }
        // middle
        if (currentCol != n && currentCol != 1 && currentRow != n
                && currentRow != 1) {
            toSwitch = new int[4];
            toSwitch[0] = blankSpace - 1;
            toSwitch[1] = blankSpace + 1;
            toSwitch[2] = blankSpace - n;
            toSwitch[3] = blankSpace + n;
        }

        for (int value : toSwitch) {
            // StdOut.println("index: " + value + " value: " + (int) this.myBoard[value - 1]);
            char[] copiedArray = Arrays.copyOf(this.myBoard, this.myBoard.length);
            int nonZeroValue = this.myBoard[value - 1];
            int nonZeroIndex = value - 1;
            int zeroIndex = blankSpace - 1;
            // StdOut.println("nonZeroValue: " + nonZeroValue + " nonZeroIndex: " + nonZeroIndex
            //                        + " zeroIndex: " + zeroIndex);
            copiedArray[nonZeroIndex] = (char) 0;
            copiedArray[zeroIndex] = (char) nonZeroValue;
            int[][] twoDArray = new int[n][n];
            int index2 = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    twoDArray[i][j] = copiedArray[index2];
                    index2++;
                }
            }
            Board test = new Board(twoDArray);
            // StdOut.println("New test print here: " + test.toString());
            neighborStack.push(test);
        }
        return neighborStack;
    }

    // // a board that is obtained by exchanging any pair of tiles

    public Board twin() {
        int index = 0;
        int nonZeroIndex;
        int nonZeroRow;
        int nonZeroCol;
        int twinIndex = 0;
        int twinValue = 0;
        int[][] filler = new int[0][0];
        Board twin = new Board(filler);


        for (char value : myBoard) {
            if ((int) value != 0) {
                nonZeroIndex = index;
                nonZeroRow = (int) Math.ceil((double) index / n);
                nonZeroCol = ((index - 1) % n) + 1;
                // up
                if (nonZeroRow > 1 && this.myBoard[index - n] != 0) {
                    twinIndex = index - n;
                    twinValue = this.myBoard[twinIndex];
                    // StdOut.println("within up");
                }
                // down
                else if (nonZeroRow < n && this.myBoard[index + n] != 0) {
                    twinIndex = index + n;
                    twinValue = this.myBoard[twinIndex];
                    //     StdOut.println("within down");
                    //     StdOut.println("index: " + twinIndex + " row: " + twinRow + " col: " + twinCol);
                    //     StdOut.println("twinValue: " + twinValue);
                }
                // left
                else if (nonZeroCol > 1 && this.myBoard[index - 1] != 0) {
                    twinIndex = index - 1;
                    twinValue = this.myBoard[twinIndex];
                    // StdOut.println("within left");
                }
                // right
                else if (nonZeroCol < n && this.myBoard[index + 1] != 0) {
                    twinIndex = index + 1;
                    twinValue = this.myBoard[twinIndex];
                    // StdOut.println("within right");
                }
                char[] twinArray = Arrays.copyOf(this.myBoard, this.myBoard.length);
                int nonZeroValue = this.myBoard[index];

                // StdOut.println("nonZeroValue: " + nonZeroValue + " nonZeroIndex: " + nonZeroIndex);
                // StdOut.println("twinValue: " + twinValue + " twinIndex: " + twinIndex);

                twinArray[nonZeroIndex] = (char) twinValue;
                twinArray[twinIndex] = (char) nonZeroValue;
                int[][] twoDArray = new int[n][n];
                int index2 = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        twoDArray[i][j] = twinArray[index2];
                        index2++;
                    }
                }
                twin = new Board(twoDArray);
                break;
            }
            index++;
        }
        return twin;
    }


    // unit testing (not graded)
    public static void main(String[] args) {


        int[][] arrayOne = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };
        int[][] arrayTwo = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };
        int[][] arrayThree = {
                { 2, 3, 0 },
                { 4, 5, 6 },
                { 7, 8, 1 }
        };
        int[][] testArray = {
                { 1, 0, 3 },
                { 4, 2, 5 },
                { 7, 8, 6 }
        };

        Board first = new Board(arrayOne);
        Board second = new Board(arrayTwo);
        Board third = new Board(arrayThree);
        Board test = new Board(testArray);

        // StdOut.println(first.equals(second));
        // StdOut.println(first.equals(third));
        // StdOut.println(second.equals(third));
        // StdOut.println(second.equals(second));

        // StdOut.println(test.toString());
        Iterable<Board> neighborStack = test.neighbors();
        for (Board board : neighborStack) {
            StdOut.println(board.toString());
        }
        Board twin = test.twin();
        StdOut.println(test.toString());
        StdOut.println(twin.toString());
    }
}

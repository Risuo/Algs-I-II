/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private MyTrieST<Integer> st = new MyTrieST<>();
    private int oneDimBoardlength;
    private int width, height;
    private boolean[] marked;
    private int[] edgeTo, oneDimBoard;
    private char[] oneDimBoardChar;
    private Queue<String> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        String[] workingDictionary = dictionary;

        int index = 0;

        for (String word : workingDictionary) {
            // StdOut.println(word + " " + index);
            st.put(word, index);
            index++;
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        words = new Queue<String>();
        oneDimBoardChar = convertBoard(board);
        Queue<Integer>[] adjacentsTable = makeAdjacentsTable(oneDimBoard);
        marked = new boolean[oneDimBoardlength];
        edgeTo = new int[oneDimBoardlength];
        for (int cell = 0; cell < oneDimBoardlength; cell++) {
            marked = new boolean[oneDimBoardlength];
            edgeTo = new int[oneDimBoardlength];
            myDfs(adjacentsTable, cell, "");
            // StdOut.println("cell " + cell + " : " + adjacentsTable[cell]);
        }
        for (String word : words) {
            st.wordReset(word);
        }
        return words;
    }

    private static void print1DArray(boolean[] array) {
        for (boolean entry : array) {
            StdOut.printf("%b ", entry);
        }
        StdOut.println();
    }

    private void myDfs(Queue<Integer>[] table, int cell, String checking) {
        marked[cell] = true;
        // print1DArray(marked);
        checking += oneDimBoardChar[cell];
        if (oneDimBoardChar[cell] == 'Q') {
            checking += 'U';
        }

        // StdOut.println(
        //         "Checking: " + checking + " last added letter: " + oneDimBoardChar[cell] + " cell: "
        //                 + cell);
        // if (st.hasKeysWithPrefix(checking)) {
        //     StdOut.println("has matching keys");
        // }
        if (st.contains(checking) && st.getScore(checking) >= 1 && st.isNewWord(checking)) {
            // StdOut.println("word! " + checking + " score: " + st.getScore(checking));
            words.enqueue(checking);
            st.oldWord(checking);
        }
        // StdOut.println(checking + " is a word: " + st.contains(checking));
        // if (st.hasKeysWithPrefix(checking)) StdOut.println("true: " + st.keysWithPrefix(checking));
        if (st.hasKeysWithPrefix(checking)) {
            // while (st.hasKeysWithPrefix(checking)) {
            for (int w : table[cell]) {
                if (!marked[w]) {
                    edgeTo[w] = cell;
                    myDfs(table, w, checking);
                }
                // else marked[w] = false;
            }
            // }
            marked[cell] = false;
        }
        else {
            marked[cell] = false;
        }
    }


    private Queue<Integer>[] makeAdjacentsTable(int[] oneDimBoard) {
        Queue<Integer>[] adjacentsTable = new Queue[oneDimBoardlength];
        for (int i = 0; i < oneDimBoardlength; i++) {
            Queue<Integer> adjacents = getAdjacents(oneDimBoard, i);
            adjacentsTable[i] = adjacents;
        }
        return adjacentsTable;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (st.contains(word)) return MyTrieST.scoreWord(word.length());
        else return 0;
        // return st.getScore(word);
    }

    private char[] convertBoard(BoggleBoard board) {
        // long uniqueBoardId = (long) 1 + (long) (Math.random() * ((250000000 - 1) + 1));
        char[] oneDimBoardChar = new char[board.cols() * board.rows()];
        oneDimBoardlength = oneDimBoardChar.length;
        width = board.cols(); // [i]
        height = board.rows(); // [j]
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                oneDimBoardChar[i * board.cols() + j] = board.getLetter(i, j);
            }
        }
        oneDimBoard = new int[oneDimBoardlength];
        return oneDimBoardChar;
    }


    private Queue<Integer> getAdjacents(int[] oneDimBoard, int cell) {
        Queue<Integer> adjacents = new Queue<Integer>();
        // StdOut.println("cell: " + cell + " width: " + width);
        if (cell - width >= 0) adjacents.enqueue(cell - width);               // up
        if (cell % width != 0) adjacents.enqueue(cell - 1);                   // left
        if (((cell + 1) % width) != 0) adjacents.enqueue(cell + 1);           // right
        if (cell + width < (width * height)) adjacents.enqueue(cell + width); // down

        if (((cell - width - 1) >= 0) && (cell % width != 0))
            adjacents.enqueue(cell - width - 1);                              // up-left
        if (((cell - width + 1) >= 0) && ((cell + 1) % width != 0))
            adjacents.enqueue(cell - width + 1);                              // up-right
        if ((cell + width - 1) < (width * height) && (cell % width != 0))
            adjacents.enqueue(cell + width - 1);                              // down-left
        if ((cell + width + 1) < (width * height) && ((cell + 1) % width) != 0)
            adjacents.enqueue(cell + width + 1);                              // down-right
        return adjacents;
    }


    private void wordInDict(String word) {
        boolean inDict = st.contains(word);
    }

    public static void main(String[] args) {
        // In in = new In(args[0]);
        // String[] dictionary = in.readAllStrings();
        // BoggleSolver solver = new BoggleSolver(dictionary);
        // BoggleBoard board1 = new BoggleBoard(3, 3);
        // StdOut.println(board1);
        // solver.getAllValidWords(board1);


        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        BoggleBoard board2 = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            // StdOut.println(word + " " + count);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("words found: " + count);

        StdOut.println(board);

        // int score2 = 0;
        // int count2 = 0;
        // for (String word : solver.getAllValidWords(board2)) {
        //     // StdOut.println(word + " " + count);
        //     score2 += solver.scoreOf(word);
        //     count2++;
        // }
        // StdOut.println("Score = " + score2);
        // StdOut.println("words found: " + count2);
        //
        // StdOut.println(board2);

        StdOut.println(solver.scoreOf("GREMIO"));

        // if (solver.st.size() < 100) {
        //     StdOut.println("keys(\"\"):");
        //     for (String key : solver.st.keys()) {
        //         StdOut.println(key + " " + solver.st.get(key) + " " + solver.st.getScore(key)
        //                                + " " + solver.st.isNewWord(key));
        //     }
        //     StdOut.println();
        // }


        // StdOut.println(solver.st.keysWithPrefix("ABC"));

        // for (String key : boardWords) {
        //     StdOut.println(key + " " + solver.st.contains(key));
        //     if (solver.st.contains(key)) solver.st.oldWord(key);
        // }
        // for (String key : boardWords) {
        //     StdOut.println(key + " " + solver.st.isNewWord(key));
        //     // if (solver.st.contains(key)) solver.st.oldWord(key, solver.st.get(key));
        // }

        // String filename = "board-points100.txt";
        // String filename2 = "board-points3.txt";

        // StdOut.println();

        // StdOut.println(board1.getLetter(0, 0));
        // StdOut.println(board1.getLetter(1, 0));
        // StdOut.println(board1.getLetter(1, 1));


    }

}


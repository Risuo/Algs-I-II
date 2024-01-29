/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private SearchNode goalNode;

    private SearchNode getGoalNode() {
        return goalNode;
    }

    private void setGoalNode(SearchNode goalNode) {
        this.goalNode = goalNode;
    }

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        private int manhattan;
        // private SearchNode grandfatherNode;

        // create the SearchNode from the delMin value from the PriorityQueue
        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.manhattan = board.manhattan();
            // this.grandfatherNode = (previousNode != null) ? previousNode.getPreviousNode() : null;
        }

        public SearchNode getPreviousNode() {
            return previousNode;
        }

        // public SearchNode getGrandfatherNode() {
        //     return grandfatherNode;
        // }

        public int getManhattan() {
            return manhattan;
        }

        public Board getBoard() {
            return this.board;
        }

        public int getPriority() {
            return manhattan + moves;
        }

        public int getMoves() {
            return moves;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int priorityDiff = a.getPriority() - b.getPriority();
            if (priorityDiff != 0) {
                return priorityDiff;
            }
            return Integer.compare(a.getManhattan() - b.getManhattan(), 0);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        int moves = 0;
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        SearchNode firstNode = new SearchNode(initial, moves, null);
        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        pq.insert(firstNode);

        SearchNode firstNodeTwin = new SearchNode(initial.twin(), moves, null);
        MinPQ<SearchNode> pqTwin = new MinPQ<>(new SearchNodeComparator());
        pqTwin.insert(firstNodeTwin);

        // StdOut.println("Initial: " + pq.min().getBoard().toString());
        // StdOut.println("Twin: " + pqTwin.min().getBoard().toString());

        // int steps = 1;

        // || !pqTwin.min().getBoard().isGoal()
        while (!pq.min().getBoard().isGoal() && !pqTwin.min().getBoard().isGoal()) {
            SearchNode minNode = pq.delMin();
            SearchNode grandfatherNode = minNode.getPreviousNode();
            Board minBoard = minNode.getBoard();
            Iterable<Board> neighborStack = minBoard.neighbors();

            SearchNode minNodeTwin = pqTwin.delMin();
            SearchNode grandfatherNodeTwin = minNodeTwin.getPreviousNode();
            Board minBoardTwin = minNodeTwin.getBoard();
            Iterable<Board> neighborStackTwin = minBoardTwin.neighbors();

            // StdOut.println("Hamming & Manhattan Initial: " + minNode.getBoard().hamming() + " - "
            //                        + minNode.getBoard().manhattan());
            // StdOut.println("Hamming & Manhattan Twin: " + minNodeTwin.getBoard().hamming() + " - "
            //                        + minNodeTwin.getBoard().manhattan());
            // StdOut.println("Testing: " + minNodeTwin.getBoard().isGoal());
            // StdOut.println("Hamming: " + (minNodeTwin.getBoard().hamming() == 0));
            // StdOut.println("Manhattan: " + (minNodeTwin.getBoard().manhattan() == 0));


            for (Board board : neighborStack) {
                if (grandfatherNode != null) {
                    if (!board.equals(grandfatherNode.getBoard())) {
                        SearchNode thisNode = new SearchNode(board, minNode.getMoves() + 1,
                                                             minNode);
                        pq.insert(thisNode);
                    }
                }
                else {
                    SearchNode thisNode = new SearchNode(board, minNode.getMoves() + 1, minNode);
                    pq.insert(thisNode);
                }
            }

            for (Board boardTwin : neighborStackTwin) {
                if (grandfatherNodeTwin != null) {
                    if (!boardTwin.equals(grandfatherNodeTwin.getBoard())) {
                        SearchNode thisNodeTwin = new SearchNode(boardTwin,
                                                                 minNodeTwin.getMoves() + 1,
                                                                 minNodeTwin);
                        pqTwin.insert(thisNodeTwin);
                    }
                }
                else {
                    SearchNode thisNodeTwin = new SearchNode(boardTwin, minNodeTwin.getMoves() + 1,
                                                             minNodeTwin);
                    pqTwin.insert(thisNodeTwin);
                }
            }

            // StdOut.println("Steps: " + steps);
            // steps++;
            // for (SearchNode node : pq) {
            //     StdOut.println("Priority: " + node.getPriority());
            //     StdOut.println("Moves: " + node.getMoves());
            //     StdOut.println("Manhattan: " + node.getBoard().manhattan());
            //     StdOut.println(node.getBoard().toString());
            //
            // }

        }
        if (pqTwin.min().getBoard().isGoal()) {
            goalNode = new SearchNode(initial.twin(), -1, null);
        }
        if (pq.min().getBoard().isGoal()) {
            goalNode = pq.min();
        }
        // goalNode = pq.min();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (goalNode.getMoves() != -1);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return getGoalNode().getMoves();
    }

    // sequence of boards in a shortest solution; null if unsolvable

    private Iterable<Board> swim(SearchNode goalNode, Stack<Board> solutionStack) {
        SearchNode currentNode = goalNode;
        while (currentNode != null) {
            solutionStack.push(currentNode.getBoard());
            currentNode = currentNode.previousNode;
        }
        return solutionStack;
    }

    public Iterable<Board> solution() {
        if (goalNode.getMoves() == -1) {
            return null;
        }
        Stack<Board> solutionStack = new Stack<>();
        return swim(goalNode, solutionStack);
    }


    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // StdOut.println("Base Board:");
        // StdOut.print(initial.toString());

        // StdOut.println("hamming: " + initial.hamming());
        // StdOut.println("manhattan: " + initial.manhattan());

        // Iterable<Board> neighborStack = initial.neighbors();
        // StdOut.println("Base Neighbors");
        // for (Board board : neighborStack) {
        //     StdOut.println(board.toString());
        // }


        // StdOut.println("Twin: ");
        // Board twin = initial.twin();
        // StdOut.println(twin.toString());
        // StdOut.println("hamming: " + twin.hamming());
        // StdOut.println("manhattan: " + twin.manhattan());
        //
        // Iterable<Board> neighborStack2 = twin.neighbors();
        // StdOut.println("Twin Neighbors");
        // for (Board board : neighborStack2) {
        //     StdOut.println(board.toString());
        // }


        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            // for (Board board : solver.solution())
            //     StdOut.println(board);
        }
    }

}

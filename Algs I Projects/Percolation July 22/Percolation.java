/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF weightedGrid;
    private int openCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        else {
            weightedGrid = new WeightedQuickUnionUF((n * n) + 2);
            grid = new boolean[n + 1][n + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    grid[i][j] = false;
                }
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // StdOut.println("Within Open. Row: " + row + "Col: " + col);
        if (row < 1 || col < 1 ||
                row > grid.length || col > grid.length) {
            throw new IllegalArgumentException();
        }
        else if (!isOpen(row, col)) {
            // StdOut.println("Made it to open.");
            grid[row][col] = true;
            openCount++;
            boolean isBottom;
            if (row + 1 == grid.length) {
                isBottom = true;
            }
            else {
                isBottom = false;
            }
            checkAdjacents(row, col, isBottom);
        }

    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // StdOut.println("Within isOpen. Row: " + row + "Col: " + col);
        if (row < 1 || col < 1 ||
                row > grid.length - 1 || col > grid.length - 1) {
            throw new IllegalArgumentException();
        }
        else
            return grid[row][col];
        // try {
        //     return grid[row][col];
        // }
        // catch (ArrayIndexOutOfBoundsException e) {
        //     throw new IllegalArgumentException();
        // }
    }


    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 ||
                row > grid.length - 1 || col > grid.length - 1) {
            throw new IllegalArgumentException();
        }
        // else if (row + 1 == grid.length) {
        //     return (isFull(row - 1, col) || isFull(row, col - 1) || isFull(row, col + 1));
        // }
        else
            // StdOut.println(row + "," + col);
            return (weightedGrid.find(convertToWeighted(row, col)) ==
                    weightedGrid.find(0));
    }

    private int convertToWeighted(int row, int col) {
        return (((row - 1) * (grid.length - 1)) + (col - 1) + 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // StdOut.println(weightedGrid.find(0));
        // StdOut.println(weightedGrid.find(grid.length * grid.length + 1));
        return weightedGrid.find(0) == weightedGrid.find(
                ((grid.length - 1) * (grid.length - 1)) + 1);
    }

    // private void printGrid(int arg) {
    //     int count = 0;
    //     for (int i = 0; i < grid.length; i++) {
    //         for (int j = 0; j < grid[i].length; j++) {
    //             StdOut.print("[" + grid[i][j] + "]");
    //             count++;
    //             if (count == arg) {
    //                 StdOut.println("");
    //                 count = 0;
    //             }
    //         }
    //     }
    // }

    private void checkAdjacents(int row, int col, boolean isBottom) {
        // StdOut.println("within check adjacents: " + row + "," + col);
        // top row
        // StdOut.println("Made it to checkAdjacents");
        // if (!percolates() && row == 1) {
        if (row == 1) {
            // linking to virtual top spot, this top row cell is now full
            weightedGrid.union(0, convertToWeighted(row, col));
            try {
                // trying down one from top row
                if (isOpen(row + 1, col)) {
                    weightedGrid.union(0, convertToWeighted(row + 1, col));
                }
            }
            catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                // StdOut.println("Tried this cell: [" + row + 1 + "," + col + "]");
            }
        }
        try {
            // trying up one
            if (isOpen(row - 1, col)) {
                weightedGrid.union(convertToWeighted(row, col),
                                   convertToWeighted(row - 1, col));
            }
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // StdOut.println("Tried this cell: [" + row + "," + (col + 1) + "]");
        }
        try {
            // trying down one
            if (isOpen(row + 1, col)) {
                weightedGrid.union(convertToWeighted(row, col),
                                   convertToWeighted(row + 1, col));
            }
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // StdOut.println("Tried this cell: [" + row + "," + (col + 1) + "]");
        }

        try {
            // trying left one
            if (isOpen(row, col - 1)) {
                weightedGrid.union(convertToWeighted(row, col),
                                   convertToWeighted(row, col - 1));
            }
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // StdOut.println("Tried this cell: [" + row + "," + (col + 1) + "]");
        }
        try {
            // trying right one
            if (isOpen(row, col + 1)) {
                weightedGrid.union(convertToWeighted(row, col),
                                   convertToWeighted(row, col + 1));
            }
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            // StdOut.println("Tried this cell: [" + row + "," + (col + 1) + "]");
        }
        if (isBottom) {
            // linking to virtual bottom spot
            // Implement anti-backwash here. I suspect what's happening

            // StdOut.println("row: " + row + " col: " + col + " " + percolates());
            // if (weightedGrid.find(convertToWeighted(row, col)) ==
            //         weightedGrid.find(0)) {
            //
            //
            // }

            weightedGrid.union(
                    (grid.length - 1) * (grid.length - 1) + 1,
                    convertToWeighted(row, col));

            // StdOut.println("Percolates: " + percolates());

            // weightedGrid.union(
            //         (grid.length - 1) * (grid.length - 1) + 1,
            //         convertToWeighted(row, col));

        }

    }

    public static void main(String[] args) {

        Percolation test = new Percolation(Integer.parseInt(args[0]));
        // test.isFull(5, -1);
        test.isOpen(5, 11);


        // for (int i = 1; i < 20; i++) {

        // Stopwatch watch = new Stopwatch();


        // while (!test.percolates()) {
        //     // StdOut.println("Within the test block.");
        //     int testRow = StdRandom.uniformInt(1, Integer.parseInt(args[0]) + 1);
        //     int testCol = StdRandom.uniformInt(1, Integer.parseInt(args[0]) + 1);
        //     // StdOut.println("testRow: " + testRow + " testCol: " + testCol);
        //     test.open(testRow, testCol);
        //     // StdOut.println((((double) (n - 1) * (n - 1)));)
        //     StdOut.println(test.numberOfOpenSites());

        // test.printGrid(Integer.parseInt(args[0]) + 1);

        //     // StdOut.println("Percolates: " + test.percolates());
        //     StdOut.println("==========");
        //     // test.percolates();
        //     // StdOut.println("Percolates: " + test.percolates());
        //     // test.printGrid(Integer.parseInt(args[0]));
        // }
        // int openSites = test.numberOfOpenSites();
        // int totalSites = Integer.parseInt(args[0]) * Integer.parseInt(args[0]);
        // double proportion = openSites / (double) totalSites;
        // StdOut.println(proportion);


        // StdOut.println("Open Sites: " + openSites);
        // StdOut.println("Out of total sites: " + (totalSites));
        // StdOut.println("Proportion: " + proportion);

        // StdOut.println("Elapsed time: " + watch.elapsedTime());
        // Percolation test2 = new Percolation(3);
        // StdOut.println("percolates: " + test2.percolates());
        // StdOut.println("Testing 1,1");
        // test2.open(1, 1);
        // StdOut.println("is open: " + test2.isOpen(1, 1));
        // StdOut.println("is full: " + test2.isFull(1, 1));
        // StdOut.println("percolates: " + test2.percolates());
        // StdOut.println("Testing 1,2");
        // test2.open(1, 2);
        // StdOut.println("is open: " + test2.isOpen(1, 2));
        // StdOut.println("is full: " + test2.isFull(1, 2));
        // StdOut.println("percolates: " + test2.percolates());
        // StdOut.println("Testing 2,2");
        // test2.open(2, 2);
        // StdOut.println("is open: " + test2.isOpen(2, 2));
        // StdOut.println("is full: " + test2.isFull(2, 2));
        // StdOut.println("percolates: " + test2.percolates());
        // StdOut.println("Testing 3,2");
        // test2.open(3, 2);
        // StdOut.println("is open: " + test2.isOpen(3, 2));
        // StdOut.println("is full: " + test2.isFull(3, 2));
        // StdOut.println("percolates: " + test2.percolates());
        // test2.printGrid(4);
        // StdOut.println(test.weightedGrid.find(0));
        // StdOut.println(
        //        test.weightedGrid.find(((test.grid.length - 1) * (test.grid.length - 1)) + 1));
        // StdOut.println(((test2.grid.length - 1) * (test2.grid.length - 1)) + 1);


    }
}

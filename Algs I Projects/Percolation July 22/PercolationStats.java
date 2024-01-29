/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private double[] resultsArray;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        else
            resultsArray = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation test = new Percolation(n);

            while (!test.percolates()) {
                int testRow = StdRandom.uniformInt(1, n + 1);
                int testCol = StdRandom.uniformInt(1, n + 1);

                test.open(testRow, testCol);
            }
            resultsArray[i] = test.numberOfOpenSites() / (((double) (n) * (n)));
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(resultsArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(resultsArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(resultsArray.length));
    }


    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(resultsArray.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats test = new PercolationStats(Integer.parseInt(args[0]),
                                                     Integer.parseInt(args[1]));

        StdOut.println("mean                    = " + test.mean());
        StdOut.println("stddev                  = " + test.stddev());
        StdOut.println(
                "95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceLo()
                        + "]");


    }

}

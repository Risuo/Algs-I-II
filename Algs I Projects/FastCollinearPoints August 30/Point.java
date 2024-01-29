/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Comparator;
import java.util.InputMismatchException;

public class Point implements Comparable<Point> {

    private int x;
    private int y;

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (this.x == that.x && this.y == that.y) {
            // throw new IllegalArgumentException();
            // StdOut.println("This and That points are equal.");
            return 0;
        }
        if (this.y < that.y || this.y == that.y && this.x < that.x) {
            // StdOut.println("This is less than That (y coordinate) OR x is less and y is equal.");
            return -1;
        }
        else {
            // StdOut.println("This is greater than That (y coordinate)");
            return 1;
        }
    }


    // the slope between this point and that point

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.compareTo(that) == 0) {
            // throw new IllegalArgumentException();

            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
            return 0.0;
        }
        else {
            int num = that.y - this.y;
            int den = that.x - this.x;
            // StdOut.println("num: " + num + " den: " + den + " Slope: " + (double) num / den);
            return (double) num / den;
        }
    }


    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }


    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            double slopeP1 = slopeTo(p1);
            double slopeP2 = slopeTo(p2);

            // The below replaces a slopeP1 < slopeP2 -1; slopeP1 > slopeP2 1; == 0
            // if, else if, else combo .
            // If slopeP1 < p2: return -1
            // If slopeP1 > p2: return 1
            // If slopeP1 = p2: return 0

            return Double.compare(slopeP1, slopeP2);

        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            try {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
            }
            catch (NullPointerException | InputMismatchException e) {
                // throw new IllegalArgumentException();
            }
        }

        // draw the points
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.blue);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // for (Point p : points) {
        //     StdOut.println(p);
        // }
        // StdOut.println();
        // Arrays.sort(points, points[0].slopeOrder());
        // for (Point p : points) {
        //     StdOut.println(p);
        // }

        // print and draw the line segments
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        // for (LineSegment segment : collinear.segments()) {
        //     StdOut.println(segment);
        //     segment.draw();
        // }
        // StdDraw.show();

        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // FastCollinearPoints collinear = new FastCollinearPoints(test);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


        // StdOut.println(points[0].toString() + points[1].toString());
        // StdOut.println("Point 0 compare to Point 1: " + points[0].compareTo(points[1]));
        // StdOut.println(points[0].slopeTo(points[1]));
        // StdOut.println(points[0].compareTo(points[1]));
        // StdOut.println(
        //         "Point 1 compare to Point 2: " + points[1].toString() + points[2].toString());
        // StdOut.println(points[1].slopeTo(points[2]));
        // StdOut.println(points[1].compareTo(points[2]));
        // StdOut.println(
        //         "Point 2 compare to Point 3: " + points[2].toString() + points[3].toString());
        // StdOut.println(points[2].slopeTo(points[3]));
        // StdOut.println(points[2].compareTo(points[3]));
        // StdOut.println(
        //         "Point 3 compare to Point 4: " + points[3].toString() + points[4].toString());
        // StdOut.println(points[3].slopeTo(points[4]));
        // StdOut.println(points[3].compareTo(points[4]));
        // StdOut.println(
        //         "Point 4 compare to Point 0: " + points[4].toString() + points[0].toString());
        // StdOut.println(points[4].slopeTo(points[0]));
        // StdOut.println(points[4].compareTo(points[0]));


    }
}

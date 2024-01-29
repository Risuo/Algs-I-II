/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {


    private SET<Point2D> pointSet;
    private SET<Point2D> rectSet;
    //  private Point2D nearest;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.size() == 0;
    }

    // number of points in the set
    public int size() {
        return pointSet.size();

    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);

    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSet) {
            StdDraw.point(point.x(), point.y());
        }
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        rectSet = new SET<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                rectSet.add(point);
            }
        }
        return rectSet;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        // RectHV canvas = new RectHV(0.0, 0.0, 1.0, 1.0);
        double minimum = 150;
        Point2D minimumPoint = null;
        for (Point2D point : pointSet) {
            // StdOut.println(p.distanceTo(point));
            if (p.distanceSquaredTo(point) < minimum) {
                minimum = p.distanceSquaredTo(point);
                minimumPoint = point;
            }
        }
        return minimumPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        // KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            // kdtree.insert(p);
            brute.insert(p);
        }
        StdOut.println(brute.size());
        for (Point2D point : brute.pointSet) {
            StdOut.println(point.toString());
        }

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

    }
}

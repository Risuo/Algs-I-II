/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;
    // private SET<Point2D> rectSet;

    private static class Node {
        private Point2D point;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private int size;

        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }


    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;

    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, p, 1);
    }

    private Node insert(Node n, Point2D point, int level) {
        if (n == null) {
            size++;
            return new Node(point, new RectHV(0, 0, 1, 1));

        }
        if (n.point.equals(point)) {
            return n;
        }

        if (level % 2 == 0) {
            if (point.y() < n.point.y()) {
                n.lb = insert(n.lb, point, level + 1);
                if (n.lb.rect.equals(root.rect)) {
                    n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(),
                                           n.rect.xmax(), n.point.y());
                }
            }
            else {
                n.rt = insert(n.rt, point, level + 1);
                if (n.rt.rect.equals(root.rect)) {
                    n.rt.rect = new RectHV(n.rect.xmin(), n.point.y(),
                                           n.rect.xmax(), n.rect.ymax());
                }
            }
        }
        else {
            if (point.x() < n.point.x()) {
                n.lb = insert(n.lb, point, level + 1);
                if (n.lb.rect.equals(root.rect)) {
                    n.lb.rect = new RectHV(n.rect.xmin(), n.rect.ymin(),
                                           n.point.x(), n.rect.ymax());
                }
            }
            else {
                n.rt = insert(n.rt, point, level + 1);
                if (n.rt.rect.equals(root.rect)) {
                    n.rt.rect = new RectHV(n.point.x(), n.rect.ymin(),
                                           n.rect.xmax(), n.rect.ymax());
                }
            }
        }
        return n;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        // return pointSet.contains(p);
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return (contains(root, p, 1) != null);
    }

    private Node contains(Node node, Point2D point, int level) {
        if (node == null) {
            return null; // Point not found in the KdTree
        }

        if (point.equals(node.point)) {
            return node; // Point found in the current node
        }

        int cmp = comparePoints(point, node.point, level);

        if (cmp < 0) {
            return contains(node.lb, point, level + 1);
        }
        else {
            return contains(node.rt, point, level + 1);
        }
    }

    private int comparePoints(Point2D a, Point2D b, int level) {
        if (level % 2 == 1) {
            return Double.compare(a.x(), b.x());
        }
        else {
            return Double.compare(a.y(), b.y());
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 1);
    }

    private void draw(Node n, int level) {
        if (n == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.point.draw();
        StdDraw.setPenRadius();

        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.point.y(),
                         n.rect.xmax(), n.point.y());
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), n.rect.ymin(),
                         n.point.x(), n.rect.ymax());
        }

        // StdOut.println("Within draw: " + level);
        draw(n.lb, level + 1);
        draw(n.rt, level + 1);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();
        range(root, rect, points);

        return points;
    }

    private void range(Node node, RectHV rect, List<Point2D> points) {
        if (node == null || !node.rect.intersects(rect)) {
            return;
        }
        if (rect.contains(node.point)) {
            points.add(node.point);
        }
        range(node.lb, rect, points);
        range(node.rt, rect, points);
    }


    // for (Point2D point : pointSet) {
    //     if (rect.contains(point)) {
    //         rectSet.add(point);
    //     }
    // }
    // return rectSet;


    // a nearest neighbor in the set to point p; null if the set is empty
    // public Point2D nearest(Point2D p) {
    //     // RectHV canvas = new RectHV(0.0, 0.0, 1.0, 1.0);
    //     double minimum = 150;
    //     Point2D minimumPoint = null;
    //     for (Point2D point : pointSet) {
    //         // StdOut.println(p.distanceTo(point));
    //         if (p.distanceTo(point) < minimum) {
    //             minimum = p.distanceTo(point);
    //             minimumPoint = point;
    //         }
    //     }
    //     return minimumPoint;
    // }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double best = root.point.distanceSquaredTo(p);
        return nearest(root, p, root.point, best, 1);
    }

    private Point2D nearest(Node node, Point2D p, Point2D champion, double best, int level) {
        if (node == null || best < node.rect.distanceSquaredTo(p)) {
            return champion;
        }
        best = champion.distanceSquaredTo(p);
        double temporary = node.point.distanceSquaredTo(p);
        if (temporary < best) {
            best = temporary;
            champion = node.point;
        }

        if (level % 2 == 0) {
            if (node.point.y() < p.y()) {
                champion = nearest(node.rt, p, champion, best, level + 1);
                if (node.lb != null
                        && node.lb.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(
                        p)) {
                    champion = nearest(node.lb, p, champion, best, level + 1);
                }
            }
            else {
                champion = nearest(node.lb, p, champion, best, level + 1);
                if (node.rt != null
                        && node.rt.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
                    champion = nearest(node.rt, p, champion, best, level + 1);
                }
            }
        }
        else {
            if (node.point.x() < p.x()) {
                champion = nearest(node.rt, p, champion, best, level + 1);
                if (node.lb != null
                        && node.lb.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
                    champion = nearest(node.lb, p, champion, best, level + 1);
                }
            }
            else {
                champion = nearest(node.lb, p, champion, best, level + 1);
                if (node.rt != null
                        && node.rt.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
                    champion = nearest(node.rt, p, champion, best, level + 1);
                }
            }
        }
        return champion;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }
        StdOut.println("brute.size: " + brute.size());
        // for (Point2D point : brute.pointSet) {
        //     StdOut.println(point.toString());
        // }

        // draw the points
        // StdDraw.clear();
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.01);
        StdOut.println("kdtree.size: " + kdtree.size());
        kdtree.draw();

        KdTree test = new KdTree();

        test.insert(new Point2D(0.8, 0.1));
        test.insert(new Point2D(0.7, 0.2));
        test.insert(new Point2D(0.1, 0.9));

        StdOut.println(test.size());
        StdOut.println(test.contains(new Point2D(0.1, 0.9)));

        // brute.draw();
        // StdDraw.show();

    }
}

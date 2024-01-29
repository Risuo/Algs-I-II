/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class LineSegment {

    private Point p;
    private Point q;

    // constructs the line segment between points p and q
    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new IllegalArgumentException("argument to LineSegment constructor is null");
        }
        if (p.equals(q)) {
            throw new IllegalArgumentException(
                    "both arguments to LineSegment constructor are the same point: " + p);
        }
        this.p = p;
        this.q = q;
    }

    // draws this line segment
    public void draw() {
        p.drawTo(q);
    }

    // string representation
    public String toString() {
        return p + " -> " + q;
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported");
    }
}

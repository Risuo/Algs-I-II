/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {

    private int count = 0;

    private LineSegment[] segments;


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point value : points) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }

        segments = new LineSegment[points.length];

        int n = points.length;

        Point[] copyPoints = new Point[n];
        for (int k = 0; k < n; k++) {
            copyPoints[k] = points[k];
        }


        Arrays.sort(copyPoints);
        for (int a = 0; a < n - 1; a++) {
            if (copyPoints[a].compareTo(copyPoints[a + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int m = k + 1; m < n; m++) {
                        if (copyPoints[i].slopeTo(copyPoints[j]) == copyPoints[i].slopeTo(
                                copyPoints[k])
                                && copyPoints[i].slopeTo(copyPoints[j]) == copyPoints[i].slopeTo(
                                copyPoints[m])) {
                            // StdOut.println("Colinears found: ");

                            // StdOut.println(
                            //         points[i] + " " + points[j] + " " + points[k] + " "
                            //                 + points[l]);

                            LineSegment segment = new LineSegment(copyPoints[i], copyPoints[m]);
                            segments[count] = segment;
                            count++;
                        }


                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] copyArr = new LineSegment[count];
        for (int k = 0; k < count; k++) {
            copyArr[k] = segments[k];
        }
        return copyArr;

    }
}

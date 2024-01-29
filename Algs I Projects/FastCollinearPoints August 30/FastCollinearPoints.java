/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public class FastCollinearPoints {

    private int count = 0;
    private LineSegment[] segments;


    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point value : points) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }

        // number of points in the set
        int n = points.length;

        // create a duplicate array copyPoints, of the original
        // array of points
        Point[] copyPoints = new Point[n];
        for (int k = 0; k < n; k++) {
            // StdOut.println(points[k]);
            copyPoints[k] = points[k];
        }

        Arrays.sort(copyPoints);
        for (int a = 0; a < n - 1; a++) {
            // StdOut.println(copyPoints[a] + " " + copyPoints[a + 1]);
            if (copyPoints[a].compareTo(copyPoints[a + 1]) == 0) {
                // StdOut.println("Duplicate");
                throw new IllegalArgumentException();
            }
        }

        if (n < 4) {
            return;
        }

        // array of LineSegments of length n
        segments = new LineSegment[n];


        for (int a = 0; a < n; a++) {

            // create an array of Points to hold the
            // collinear Points as we find them
            Point[] collinearPoints = new Point[n];

            // starting with the first two points, our collinear
            // point count always begins at 2
            int collinearCount = 2;

            // Sort the original list (points) in order of the slope they
            // make with the first point of the list


            // printArray(points);

            // cycle over the points k starting with k=2 points[k] and
            // compare the slope between points[0] and points[k]
            // if the slope is the same as with points[0] and points[1]
            // then add that point to the collinearPoints array


            Arrays.sort(copyPoints, points[a].slopeOrder());
            // set the initial slope
            collinearPoints[0] = copyPoints[0];
            collinearPoints[1] = copyPoints[1];
            double baseSlope = copyPoints[0].slopeTo(copyPoints[1]);
            // StdOut.println(baseSlope + " slope from: " + copyPoints[0] + " to " + copyPoints[1]);
            // StdOut.println(
            //         "Added to collinearPoints: " + collinearPoints[0] + "," + collinearPoints[1]);
            for (int k = 2; k < n; k++) {
                // StdOut.println("k: " + k + copyPoints[k]);
                // StdOut.println("baseSlope: " + baseSlope);
                double testSlope = copyPoints[0].slopeTo(copyPoints[k]);
                // StdOut.println(copyPoints[0] + " " + copyPoints[k] + " " + copyPoints[0].slopeTo(copyPoints[k]));
                LineSegment segment;
                if (baseSlope == testSlope) {
                    // StdOut.println("Equal slopes, adding to k value: " + k);
                    collinearPoints[k] = copyPoints[k];
                    // StdOut.println(
                    //         "added to collinearPoints: " + collinearPoints[k] + "with slope: "
                    //                 + testSlope);
                    collinearCount++;
                    // StdOut.println("collinearCount now: " + collinearCount);
                    if (k == n - 1) {
                        if (collinearCount >= 4) {
                            // StdOut.println("A CollinearPoints over 4, array: ");
                            // printArray(collinearPoints);
                            Point initialPoint = collinearPoints[0];
                            Point[] copyArr2 = new Point[collinearCount];
                            int collinearIndex = 0;
                            for (int b = 0; b < n; b++) {
                                if (collinearPoints[b] != null) {
                                    // StdOut.println("not-null: " + collinearPoints[b]);
                                    copyArr2[collinearIndex] = collinearPoints[b];
                                    collinearIndex++;
                                }
                            }

                            // StdOut.println("A Printing CopyArr2:");
                            // printArray(copyArr2);
                            Arrays.sort(copyArr2);
                            // StdOut.println("A Now Printing Sorted Array:");
                            // printArray(copyArr2);

                            // StdOut.println(copyArr2[0] + " " + initialPoint);
                            if (copyArr2[0] == initialPoint) {
                                segment = new LineSegment(copyArr2[0],
                                                          copyArr2[collinearCount - 1]);
                                segments[count] = segment;
                                count++;
                                if (count == segments.length) {
                                    resize((2 * count));
                                }
                                collinearCount = 2;
                            }
                        }
                    }
                }
                else if (collinearCount >= 4) {
                    // StdOut.println("B CollinearPoints over 4, array: ");
                    // printArray(collinearPoints);
                    Point initialPoint = collinearPoints[0];
                    Point[] copyArr2 = new Point[collinearCount];
                    int collinearIndex = 0;
                    for (int b = 0; b < n; b++) {
                        if (collinearPoints[b] != null) {
                            // StdOut.println("not-null: " + collinearPoints[b]);
                            copyArr2[collinearIndex] = collinearPoints[b];
                            collinearIndex++;
                        }
                    }

                    // StdOut.println("B Printing CopyArr2:");
                    // printArray(copyArr2);
                    Arrays.sort(copyArr2);
                    // StdOut.println("B Now Printing Sorted Array:");
                    // printArray(copyArr2);

                    // StdOut.println(copyArr2[0] + " " + initialPoint);
                    if (copyArr2[0] == initialPoint) {
                        segment = new LineSegment(copyArr2[0], copyArr2[collinearCount - 1]);

                        segments[count] = segment; // This is the 50 out of bounds 50 error happens
                        count++;
                        if (count == segments.length) {
                            resize((2 * count));
                        }
                        collinearCount = 2;
                        k--;
                    }
                    else {
                        baseSlope = copyPoints[0].slopeTo(copyPoints[k]);
                        // StdOut.println(
                        //         "A BaseSlope is now: " + baseSlope + " between: " + copyPoints[0]
                        //                 + " and " + copyPoints[k] + " with k: " + k);
                        collinearPoints = new Point[n];
                        collinearPoints[0] = copyPoints[0];
                        collinearPoints[1] = copyPoints[k];
                        collinearCount = 2;
                    }
                }
                else {
                    baseSlope = copyPoints[0].slopeTo(copyPoints[k]);
                    // StdOut.println("B BaseSlope is now: " + baseSlope + " between: " + copyPoints[0]
                    //                        + " and " + copyPoints[k] + " with k: " + k);
                    // printArray(copyPoints);
                    collinearPoints = new Point[n];
                    collinearPoints[0] = copyPoints[0];
                    collinearPoints[1] = copyPoints[k];
                    collinearCount = 2;
                }
            }
        }
    }

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        int j = 0;
        for (int i = 0; i < count; i++) {
            copy[j] = segments[i];
            j++;
        }
        segments = copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] copyArr = new LineSegment[count];
        // StdOut.println("printing at the bottom");
        // printArray(segments);
        for (int k = 0; k < count; k++) {
            if (segments[k] != null) {
                // StdOut.println("result: " + segments[k]);
                copyArr[k] = segments[k];
            }
        }
        // StdOut.println("Printing Array: ");
        // printArray(copyArr);
        return copyArr;
    }

    private void printArray(Object[] array) {
        for (Object item : array) {
            StdOut.print(item + " ");
        }
        StdOut.println();
    }

}

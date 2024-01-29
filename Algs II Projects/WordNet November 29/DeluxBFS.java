/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class DeluxBFS {

    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] markedV, markedW;
    private int[] edgeToV, edgeToW;
    private int[] distToV, distToW;
    private int sapLength, ancestor;
    private ArrayList<Integer> modifiedCellsV, modifiedCellsW;

    public DeluxBFS(Digraph G, int v, int w) {
        sapLength = INFINITY;
        ancestor = -1;
        markedV = new boolean[G.V()]; // default = false
        markedW = new boolean[G.V()];
        distToV = new int[G.V()]; // default = INFINITY (see below)
        distToW = new int[G.V()];
        edgeToV = new int[G.V()];
        edgeToW = new int[G.V()];
        for (int a = 0; a < G.V(); a++) { // default = INFINITY
            distToV[a] = INFINITY;
            distToW[a] = INFINITY;
        }
        // validateVertices(v, w);
        bfsDelux(G, v, w);
    }

    public DeluxBFS(Digraph G, Iterable<Integer> v, Iterable<Integer> w) {
        sapLength = INFINITY;
        ancestor = -1;
        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        edgeToV = new int[G.V()];
        edgeToW = new int[G.V()];
        for (int a = 0; a < G.V(); a++) {
            distToV[a] = INFINITY;
            distToW[a] = INFINITY;
        }
        try {
            validateVertices(v, w);
        }
        catch (IllegalArgumentException e) {
            if ("zero vertices".equals(e.getMessage())) {
                sapLength = -1;
            }
        }
        bfsDelux(G, v, w);
    }


    private void bfsDelux(Digraph G, int v, int w) {
        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();
        validateVertex(v);
        validateVertex(w);
        markedV[v] = true;
        markedW[w] = true;
        distToV[v] = 0;
        distToW[w] = 0;
        try {
            validateVertex(v);
            validateVertex(w);
            if (v != w) {
                qV.enqueue(v);
                qW.enqueue(w);
                while (!qV.isEmpty() || !qW.isEmpty()) {
                    if (!qV.isEmpty()) {
                        int vV = qV.dequeue();
                        for (int c : G.adj(vV)) {
                            // StdOut.println("test1");
                            if (!markedV[c]) {
                                edgeToV[c] = vV;
                                distToV[c] = distToV[vV] + 1;
                                markedV[c] = true;
                                qV.enqueue(c);
                                // StdOut.println("distToV[c]" + distToV[c] + "," + sapLength);
                                if (distToV[c] > sapLength) {
                                    // StdOut.println("test");
                                    break;
                                }
                                if (markedW[c]) {
                                    if (checkSapLength(distToV[c], distToW[c])) {
                                        ancestor = c;
                                        sapLength = distToV[c] + distToW[c];
                                    }
                                }
                            }
                        }
                    }
                    if (!qW.isEmpty()) {
                        int wW = qW.dequeue();
                        for (int d : G.adj(wW)) {
                            if (!markedW[d]) {
                                edgeToW[d] = wW;
                                distToW[d] = distToW[wW] + 1;
                                markedW[d] = true;
                                qW.enqueue(d);
                                // StdOut.println("distToW[d]" + distToW[d] + "," + sapLength);
                                if (distToW[d] > sapLength) {
                                    // StdOut.println("test");
                                    break;
                                }
                                if (markedV[d]) {
                                    if (checkSapLength(distToV[d], distToW[d])) {
                                        ancestor = d;
                                        sapLength = distToV[d] + distToW[d];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                ancestor = v;
                sapLength = 0;
            }
        }
        catch (IllegalArgumentException e) {
            if ("zero vertices".equals(e.getMessage())) {
                sapLength = -1;
            }
        }
    }

    private void bfsDelux(Digraph G, Iterable<Integer> vSet, Iterable<Integer> wSet) {
        Queue<Integer> qV = new Queue<Integer>();
        Queue<Integer> qW = new Queue<Integer>();
        int a = markedV.length;
        if (vSet == null) {
            throw new IllegalArgumentException();
        }
        if (wSet == null) {
            throw new IllegalArgumentException();
        }

        for (Integer v : vSet) {
            if (v == null) {
                throw new IllegalArgumentException();
            }
            if (v < 0 || v >= a) {
                throw new IllegalArgumentException(
                        "vertex " + v + " is not between 0 and " + (a - 1));
            }
            for (Integer w : wSet) {
                if (v.equals(w)) {
                    sapLength = 0;
                    ancestor = v;
                }
            }
        }

        for (Integer v : vSet) {
            try {
                validateVertex(v);
                qV.enqueue(v);
                markedV[v] = true;
                distToV[v] = 0;
            }
            catch (IllegalArgumentException e) {
                if ("zero vertices".equals(e.getMessage())) {
                    sapLength = -1;
                    break;
                }
            }
        }

        for (Integer w : wSet) {
            if (w == null) {
                throw new IllegalArgumentException();
            }
            if (w < 0 || w >= a) {
                throw new IllegalArgumentException(
                        "vertex " + w + " is not between 0 and " + (a - 1));
            }
            try {
                validateVertex(w);
                qW.enqueue(w);
                markedW[w] = true;
                distToW[w] = 0;
            }
            catch (IllegalArgumentException e) {
                if ("zero vertices".equals(e.getMessage())) {
                    sapLength = -1;
                    break;
                }
            }
        }

        if (sapLength == INFINITY) {

            while (!qV.isEmpty() || !qW.isEmpty()) {
                if (!qV.isEmpty()) {
                    int vV = qV.dequeue();

                    for (int c : G.adj(vV)) {
                        if (!markedV[c]) {
                            edgeToV[c] = vV;
                            distToV[c] = distToV[vV] + 1;
                            markedV[c] = true;
                            qV.enqueue(c);
                            if (distToV[c] > sapLength) {
                                break;
                            }
                            if (markedW[c] && checkSapLength(distToV[c], distToW[c])) {
                                ancestor = c;
                                sapLength = distToV[c] + distToW[c];
                            }
                        }
                    }
                }
                if (!qW.isEmpty()) {
                    int wW = qW.dequeue();

                    for (int d : G.adj(wW)) {
                        if (!markedW[d]) {
                            edgeToW[d] = wW;
                            distToW[d] = distToW[wW] + 1;
                            markedW[d] = true;
                            qW.enqueue(d);
                            if (distToW[d] > sapLength) {
                                break;
                            }
                            if (markedV[d] && checkSapLength(distToV[d], distToW[d])) {
                                ancestor = d;
                                sapLength = distToV[d] + distToW[d];
                            }
                        }
                    }
                }
            }
        }
    }


    public boolean hasPathToV(int v) {
        validateVertex(v);
        return markedV[v];
    }

    public boolean hasPathToW(int w) {
        validateVertex(w);
        return markedW[w];
    }

    public int distToV(int v) {
        validateVertex(v);
        return distToV[v];
    }

    public int distToW(int w) {
        validateVertex(w);
        return distToW[w];
    }

    public int getAncestor() {
        return this.ancestor;
    }

    public int getSapLength() {
        return this.sapLength;
    }

    public Iterable<Integer> getPathToV() {
        Iterable<Integer> pathToV = pathToV(ancestor);
        return pathToV;
    }

    public Iterable<Integer> getPathToW() {
        Iterable<Integer> pathToW = pathToW(ancestor);
        return pathToW;
    }

    public Iterable<Integer> pathToV(int v) {
        validateVertex(v);
        if (!hasPathToV(v)) {
            return null;
        }
        Stack<Integer> pathV = new Stack<Integer>();
        int x;
        for (x = v; distToV[x] != 0; x = edgeToV[x]) {
            pathV.push(x);
        }
        pathV.push(x);
        return pathV;
    }


    public Iterable<Integer> pathToW(int w) {
        validateVertex(w);
        if (!hasPathToW(w)) {
            return null;
        }
        Stack<Integer> pathW = new Stack<Integer>();
        int x;
        for (x = w; distToW[x] != 0; x = edgeToW[x]) {
            pathW.push(x);
        }
        pathW.push(x);
        return pathW;
    }

    private boolean checkSapLength(int v, int w) {
        int test = v + w;
        return test < sapLength;
    }


    private void validateVertex(Integer a) {
        if (a == null) {
            throw new IllegalArgumentException("argument " + a + " is null");
        }
        int v = markedV.length;
        if (a < 0 || a >= v) {
            throw new IllegalArgumentException("vertex " + a + " is not between 0 and " + (v - 1));
        }
    }

    private void validateVertices(int a, int b) {
        int v = markedV.length;
        if (a < 0 || a >= v) {
            throw new IllegalArgumentException("vertex " + a + " is not between 0 and " + (v - 1));
        }
        if (b < 0 || b >= v) {
            throw new IllegalArgumentException("vertex " + b + " is not between 0 and " + (v - 1));
        }
    }

    private void validateVertices(Iterable<Integer> a, Iterable<Integer> b) {
        if (a == null) {
            throw new IllegalArgumentException("argument a is null");
        }
        if (b == null) {
            throw new IllegalArgumentException("argument b is null");
        }
        int vertexCount = 0;
        for (Integer v : a) {
            vertexCount++;
            if (v == null) {
                throw new IllegalArgumentException("vertex in a is null");
            }
            validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
        int vertexCount2 = 0;
        for (Integer v : b) {
            vertexCount2++;
            if (v == null) {
                throw new IllegalArgumentException("vertex in b is null");
            }
            validateVertex(v);
        }
        if (vertexCount2 == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }


    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        int a = 13;
        int b = 16;
        DeluxBFS deluxBFS = new DeluxBFS(G, a, b);
        StdOut.println("ancestor: " + deluxBFS.ancestor + " SAP: " + deluxBFS.sapLength);
        for (int x : deluxBFS.pathToV(deluxBFS.ancestor)) {
            if (x == a) {
                StdOut.print(x);
            }
            else {
                StdOut.print("->" + x);
            }
        }
        StdOut.println();
        for (int x : deluxBFS.pathToW(deluxBFS.ancestor)) {
            if (x == b) {
                StdOut.print(x);
            }
            else {
                StdOut.print("->" + x);
            }
        }

        // StdOut.println();
        // for (int x : deluxBFS.pathToV(0)) {
        //     if (x == a) {
        //         StdOut.print(x);
        //     }
        //     else {
        //         StdOut.print("->" + x);
        //     }
        // }
        // StdOut.println();

        // for (int v = 0; v < G.V(); v++) {
        //     // StdOut.println("v: " + v);
        //     if (deluxBFS.hasPathToV(v) && deluxBFS.hasPathToW(v)) {
        //         // if (deluxBFS.checkSapLength(deluxBFS.distToV(v), deluxBFS.distToW(v))) {
        //
        //         StdOut.printf("v: (tested ancestor vertex) %d to %d (%d) ", a, v,
        //                       deluxBFS.distToV(v));
        //         for (int x : deluxBFS.pathToV(v)) {
        //             if (x == a) {
        //                 StdOut.print(x);
        //             }
        //             else {
        //                 StdOut.print("->" + x);
        //             }
        //         }
        //         StdOut.println();
        //         StdOut.printf("v: (tested ancestor vertex) %d to %d (%d) ", b, v,
        //                       deluxBFS.distToW(v));
        //         for (int x : deluxBFS.pathToW(v)) {
        //             if (x == b) {
        //                 StdOut.print(x);
        //             }
        //             else {
        //                 StdOut.print("->" + x);
        //             }
        //         }
        //         StdOut.println();
        //         // }
        //     }
        // }


    }
}
